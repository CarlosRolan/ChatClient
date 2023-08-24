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

    private static DefaultMutableTreeNode root = new DefaultMutableTreeNode("");

    private DefaultMutableTreeNode chatsNode = new DefaultMutableTreeNode("Chats");
    private DefaultMutableTreeNode usersNode = new DefaultMutableTreeNode("Users");

    private List<String> list;

    private final Events eListener;

    public TreeViewUsers(Events eventsListener) {
        super(root);
        eListener = eventsListener;
        init();
    }

    public void init() {
        list = new ArrayList<>();
        
        root.add(chatsNode);
        root.add(usersNode);

        setRootVisible(false);

        // ADDING MOUSE EVENTS LISTENER
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getButton() == MouseEvent.BUTTON1) {
                    int selRow = getRowForLocation(me.getX(), me.getY());
                    TreePath selPath = getPathForLocation(me.getX(), me.getY());
                    eListener.onRightClick(selRow, selPath);
                }

            }
        });

    }

    /**
     * 
     */
    public void updateClients() {

        usersNode.removeAllChildren();
        // Por lo que te dijo Mario
        list = AppState.getInstance().getOnlineUsersList();

        for (String con : list) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(con, editable);
            usersNode.add(node);
        }

        DefaultTreeModel model = (DefaultTreeModel) getModel();

        model.insertNodeInto(usersNode, root, root.getChildCount() - 1);
        model.reload(root);

        expandRow(1);

    }

    public interface Events {
        void onRightClick(int selRow, TreePath selPath);
    }

}
