package GUI.view.components;

import java.awt.event.MouseAdapter;

import javax.swing.ButtonGroup;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MainMenuBar extends JMenuBar {

    private final String ITEM_0 = "Profile";

    private final String ITEM_1 = "New Chat..";

    private final String ITEM_2 = "Settings";
    private final String ITEM_2_0 = "Tree View";
    private final String ITEM_2_1 = "Tab View";

    private final IMenuBarListener iListener;

    private ButtonGroup btnGroup = new ButtonGroup();

    public final String[] MENU_ITEMS = { ITEM_0, ITEM_1, ITEM_2 };

    public MainMenuBar(IMenuBarListener listener) {
        iListener = listener;
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
                            iListener.onItemClick1();
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
      
    }

    private void iAction2() {

    }

    public interface IMenuBarListener {
        void onItemClick1();
        void enableTreeView();

        void enableTabView();
    }

}
