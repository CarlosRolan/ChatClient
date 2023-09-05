package GUI.components;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import GUI.AppState;

public class TreeViewUsers extends JTree {

    public static String MSG_NOTIFICATION = "(!)";
    public final static char MSG_ALERT_NUM = MSG_NOTIFICATION.charAt(1);

    private static DefaultMutableTreeNode root = new DefaultMutableTreeNode("");

    private DefaultMutableTreeNode chatsNode = new DefaultMutableTreeNode("Chats");
    private DefaultMutableTreeNode usersNode = new DefaultMutableTreeNode("Users");

    private List<String> conRefList;
    private List<String> chatsRefList;

    private final ITreeViewListener eListener;

    private boolean hasNewMsg(TreePath conRef) {
        return hasNewMsg(conRef.getLastPathComponent().toString());
    }

    private boolean hasNewMsg(String conRef) {
        return conRef.endsWith(MSG_NOTIFICATION);
    }

    public TreeViewUsers(ITreeViewListener eventsListener) {
        super(root);
        eListener = eventsListener;

        init();
    }

    public void init() {
        conRefList = new ArrayList<>();
        chatsRefList = new ArrayList<>();
        root.add(chatsNode);
        root.add(usersNode);

        setRootVisible(false);

        // ADDING MOUSE EVENTS LISTENER
        addMouseListener(iMouseAdapter);

    }

    private void refreshTreeView() {
        chatsNode.removeAllChildren();
        usersNode.removeAllChildren();

        int i = 0;

        for (String chatRef : chatsRefList) {
            System.out.println("CHAT_REF[" + i + "]" + chatRef);
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(chatRef, editable);
            chatsNode.add(node);
            i++;
        }

        i = 0;

        for (String conRef : conRefList) {
            System.out.println("CON_REF[" + i + "]" + conRef);
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(conRef, editable);
            usersNode.add(node);
            i++;
        }

        DefaultTreeModel model = (DefaultTreeModel) getModel();

        model.insertNodeInto(chatsNode, root, root.getChildCount() - 2);
        model.insertNodeInto(usersNode, root, root.getChildCount() - 1);

        model.reload(root);

        expandRow(0);
        expandRow(1);

        synchronized (getTreeLock()) {
            validateTree();
        }

    }

    /**
     * 
     */
    public void update() {
        // Por lo que te dijo Mario
        List<String> tempList = AppState.getInstance().getOnlineUsersList();

        for (int i = 0; i < tempList.size(); i++) {

            try {
                String conRef = conRefList.get(i);

                if (hasNewMsg(conRef)) {
                    String alertedRef = tempList.get(i) + MSG_NOTIFICATION;
                    conRefList.set(i, alertedRef);
                } else {
                    conRefList.set(i, tempList.get(i));
                }
            } catch (IndexOutOfBoundsException e) {
                conRefList.add(tempList.get(i));
            }
            refreshTreeView();
        }
    }

    private void hideAlertOnRef(String conId) {
        System.out.println("ID =" + conId);

        for (int i = 0; i < conRefList.size(); i++) {

            String conRef = conRefList.get(i);

            if (conRef.startsWith(conId) && conRef.endsWith(MSG_NOTIFICATION)) {
                conRef = conRef.substring(0, conRef.length() - MSG_NOTIFICATION.length());
                conRefList.set(i, conRef);
                break;
            }
        }
    }

    public void addAlertOnRef(String conId) {

        for (int i = 0; i < conRefList.size(); i++) {

            String conRef = conRefList.get(i);

            if (conRef.startsWith(conId) && !conRef.endsWith(MSG_NOTIFICATION)) {
                conRefList.set(i, conRef + MSG_NOTIFICATION);
                break;
            }
        }

        refreshTreeView();
    }

    private final MouseAdapter iMouseAdapter = new MouseAdapter() {

        public void mouseClicked(final MouseEvent me) {
            if (me.getButton() == MouseEvent.BUTTON1) {
                try {
                    final int selRow = getRowForLocation(me.getX(), me.getY());
                    final TreePath selPath = getPathForLocation(me.getX(), me.getY());

                    hideAlertOnRef(selPath.getLastPathComponent().toString());
                    refreshTreeView();

                    eListener.onSingleRightClick(selRow, selPath);

                } catch (NullPointerException e) {
                    System.out.println("NO PATH SELECTED");
                }

            }
        }

    };

    public interface ITreeViewListener {
        void onSingleRightClick(int selRow, TreePath selPath);
    }

}
