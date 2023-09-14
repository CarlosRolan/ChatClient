package GUI.view.components;

import java.awt.event.MouseAdapter;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import GUI.GUI;

public class MyMenuBar extends JMenuBar {

    private final String ITEM_0 = "Profile";

    private final String ITEM_1 = "New Chat..";

    private final String ITEM_2 = "Settings";

    public final String[] MENU_ITEMS = { ITEM_0, ITEM_1, ITEM_2 };

    public MyMenuBar() {
        initComponents();
    }

    private void initComponents() {
        initItems();
    }

    private void initItems() {

        for (int i = 0; i < MENU_ITEMS.length; i++) {

            JMenuItem item = new JMenuItem(MENU_ITEMS[i]);
            MouseAdapter mAdapter = null;

            switch (i) {

                case 0:
                    mAdapter = new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            iAction0();
                        }
                    };
                    break;

                case 1:
                    mAdapter = new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            iAction1();
                        }
                    };
                    break;

                case 2:
                    mAdapter = new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            iAction2();
                        }
                    };
                    break;

                default:
                    break;
            }

            item.addMouseListener(mAdapter);
            add(item);
        }
    }

    private void iAction0() {

    }

    private void iAction1() {
        String chatTitle = JOptionPane.showInputDialog("Title of the chat");
        String chatDesc = null;

        if (chatTitle != null) {
            chatDesc = JOptionPane.showInputDialog("Chat's description");
            if (chatDesc != null) {
                MyListPicker.showPicker(GUI.getInstance().getConRefList());
                GUI.getInstance().pClientCon.newChat(chatTitle, chatDesc, GUI.getInstance().getChatRefList().size());
            }
        }
    }

    private void iAction2() {

    }

}
