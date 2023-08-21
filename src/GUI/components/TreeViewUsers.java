package GUI.components;

import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class TreeViewUsers extends JTree {

    private static DefaultMutableTreeNode root = new DefaultMutableTreeNode("");

    private DefaultMutableTreeNode chatsNode = new DefaultMutableTreeNode("Chats");
    private DefaultMutableTreeNode usersNode = new DefaultMutableTreeNode("Users");

    public TreeViewUsers() {
        super(root);
        init();
    }

    public void init() {
        root.add(chatsNode);
        root.add(usersNode);
    }

    public void updateClients(List<String> updateConnections) {
        for (String con : updateConnections) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(con, editable);
            usersNode.add(node);
        }
        revalidate();
        repaint();
    }

}
