package GUI.view.components;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class MyTreeView extends JTree {

    public static String MSG_NOTIFICATION = "(!)";
    public final static char MSG_ALERT_NUM = MSG_NOTIFICATION.charAt(1);

    private static DefaultMutableTreeNode root = new DefaultMutableTreeNode("");

    private DefaultMutableTreeNode chatsNode = new DefaultMutableTreeNode("Chats");
    private DefaultMutableTreeNode usersNode = new DefaultMutableTreeNode("Users");

    private final List<String> mTreeConList;
    private final List<String> mTreeChatList;

    private final ITreeViewListener iTreeListener;

    private boolean hasNewMsg(TreePath conRef) {
        return hasNewMsg(conRef.getLastPathComponent().toString());
    }

    private boolean hasNewMsg(String conRef) {
        return conRef.endsWith(MSG_NOTIFICATION);
    }

    public MyTreeView(ITreeViewListener eventsListener) {
        super(root);
        iTreeListener = eventsListener;
        mTreeConList = new ArrayList<>();
        mTreeChatList = new ArrayList<>();
        initMyTreeView();
    }

    public void initMyTreeView() {

        root.add(chatsNode);
        root.add(usersNode);

        setRootVisible(false);

        // ADDING MOUSE EVENTS LISTENER
        addMouseListener(iMouseAdapter);

    }

    public void refreshTreeView() {
        chatsNode.removeAllChildren();
        usersNode.removeAllChildren();

        int i = 0;

        for (String chatRef : mTreeChatList) {
            System.out.println("CHAT_REF[" + i + "]" + chatRef);
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(chatRef, editable);
            chatsNode.add(node);
            i++;
        }

        i = 0;

        for (String conRef : mTreeConList) {
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
        expandRow(getRowCount() - 1);

        synchronized (getTreeLock()) {
            validateTree();
        }

    }

    public void update(List<String> updatedConRefsList, List<String> updatedChatRefs) {
        updateCon(updatedConRefsList);
        updateChats(updatedChatRefs);
        refreshTreeView();
    }

    public void updateCon(List<String> updated) {
        for (int i = 0; i < updated.size(); i++) {
            String conRef = null;
            try {
                conRef = updated.get(i);
                if (hasNewMsg(conRef)) {
                    String alertedRef = conRef + MSG_NOTIFICATION;
                    mTreeConList.set(i, alertedRef);
                } else {
                    mTreeConList.set(i, conRef);
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("New user connected");
                mTreeConList.add(conRef);
            }
        }
    }

    public void updateChats(List<String> updated) {

        for (int i = 0; i < updated.size(); i++) {
            try {
                String chatRef = updated.get(i);
                mTreeChatList.set(i, chatRef);
            } catch (IndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(this, "New chat added");
                mTreeChatList.add(updated.get(i));
            }
        }
    }

    private void hideAlertOnRef(String conId) {

        System.out.println("ID =" + conId);

        for (int i = 0; i < mTreeConList.size(); i++) {
            String conRef = mTreeConList.get(i);
            if (conRef.startsWith(conId) && conRef.endsWith(MSG_NOTIFICATION)) {
                conRef = conRef.substring(0, conRef.length() - MSG_NOTIFICATION.length());
                mTreeConList.set(i, conRef);
                break;
            }
        }
    }

    public void addAlertOnRef(String conId) {

        for (int i = 0; i < mTreeConList.size(); i++) {
            String conRef = mTreeConList.get(i);
            if (conRef.startsWith(conId) && !conRef.endsWith(MSG_NOTIFICATION)) {
                mTreeConList.set(i, conRef + MSG_NOTIFICATION);
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

                    iTreeListener.onSingleRightClick(selRow, selPath);

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
