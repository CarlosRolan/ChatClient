package GUI.view.panels;

import java.util.List;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import GUI.view.item.MyItemView.IMyItemViewListener;

public class PTabbs extends JTabbedPane {

    public final int INDEX_USERS = 0;
    public final int INDEX_CHATS = 1;

    private final String TAB_USERS = "USUARIOS";
    private final String TAB_CHATS = "CHATS";
    private final String TAB_3 = "X";

    private PList pUsersTab;
    private PList pChatsTab;
    private javax.swing.JPanel pTab3;

    public PTabbs() {
        initComponents();
    }

    private void initComponents() {
        pUsersTab = new PList();
        pChatsTab = new PList();
        pTab3 = new javax.swing.JPanel();

        addTab(TAB_USERS, pUsersTab);
        addTab(TAB_CHATS, pChatsTab);

        javax.swing.GroupLayout tab3Layout = new javax.swing.GroupLayout(pTab3);
        pTab3.setLayout(tab3Layout);
        tab3Layout.setHorizontalGroup(
                tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 337, Short.MAX_VALUE));
        tab3Layout.setVerticalGroup(
                tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 381, Short.MAX_VALUE));

        addTab(TAB_3, pTab3);

        addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int i = getSelectedIndex();

                switch (i) {
                    case INDEX_USERS:
                        setTitleAt(i, TAB_USERS);
                        break;

                    case INDEX_CHATS:
                        setTitleAt(i, TAB_CHATS);
                        break;

                    default:
                        break;
                }
            }
        });
    }

    public void refreshUsersTab(List<String> updatedConRefList, IMyItemViewListener itemListener) {
        pUsersTab.refreshConList(updatedConRefList, itemListener);
        addNotificationOnUsersTab();
    }

    public void refresChatsTab(List<String> updatedChatRefList, IMyItemViewListener itemListener) {
        pChatsTab.refreshChatList(updatedChatRefList, itemListener);
        addNotificationOnChatsTab();
    }

    public void addNotificationOnUsersTab() {
        int i = getSelectedIndex();

        if (i != INDEX_USERS) {
            setTitleAt(INDEX_USERS, TAB_USERS + "!");
        }
    }

    public void addNotificationOnChatsTab() {
        int i = getSelectedIndex();

        if (i != INDEX_CHATS) {
            setTitleAt(INDEX_CHATS, TAB_CHATS + "!");
        }
    }

}
