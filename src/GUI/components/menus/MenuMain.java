package GUI.components.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenuMain extends JMenuBar {

    private final String ITEM_0 = "Profile";

    private final String ITEM_1 = "New Chat..";

    private final String ITEM_2 = "Settings";
    private final String ITEM_2_0 = "Tree View";
    private final String ITEM_2_1 = "Tab View";
    private final IMainMenuListener mListener;

    public final String[] MENU_ITEMS = { ITEM_0, ITEM_1, ITEM_2 };

    public MenuMain(IMainMenuListener listener) {
        mListener = listener;
        initComponents();
    }

    private void initComponents() {
        initItems();
    }

    private void initItems() {

        JMenuItem iProfile = new JMenuItem(MENU_ITEMS[0]);
        iProfile.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

            }

        });
        JMenuItem iNewChat = new JMenuItem(MENU_ITEMS[1]);
        iNewChat.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mListener.createNewChat();
            }

        });
        JMenu iSettings = new JMenu(MENU_ITEMS[2]);
        // TODO settings items
        iSettings.add(new JMenuItem(ITEM_2_0));
        iSettings.add(new JMenuItem(ITEM_2_1));

        add(iProfile);
        add(iNewChat);
        add(iSettings);

    }

    public interface IMainMenuListener {
        void goToProfile();

        void createNewChat();

        void showTreeView();

        void showListView();
    }
}