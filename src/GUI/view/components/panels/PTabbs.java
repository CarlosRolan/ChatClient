package GUI.view.components.panels;

import java.util.List;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import GUI.view.components.item.ItemView.IMyItemViewListener;

public class PTabbs extends JTabbedPane {

    public final int INDEX_USERS = 0;
    public final int INDEX_CHATS = 1;

    private final String TAB_USERS = "USUARIOS";
    private final String TAB_CHATS = "CHATS";
    private final String TAB_3 = "X";

    private PList mUsersTab;
    private PList mChatsTab;
    private javax.swing.JPanel mTab3;

    /* GETTERs */
    public PList getUsersTab() {
        return mUsersTab;
    }

    public PList getChatsTab() {
        return mChatsTab;
    }

    public PTabbs() {
        initComponents();
    }

    private void initComponents() {
        mUsersTab = new PList();
        mChatsTab = new PList();
        mTab3 = new javax.swing.JPanel();

        addTab(TAB_USERS, mUsersTab);
        addTab(TAB_CHATS, mChatsTab);

        javax.swing.GroupLayout tab3Layout = new javax.swing.GroupLayout(mTab3);
        mTab3.setLayout(tab3Layout);
        tab3Layout.setHorizontalGroup(
                tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 337, Short.MAX_VALUE));
        tab3Layout.setVerticalGroup(
                tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 381, Short.MAX_VALUE));

        addTab(TAB_3, mTab3);

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
        mUsersTab.refreshList(updatedConRefList, itemListener, false);
        addNotificationOnUsersTab();
    }

    public void refresChatsTab(List<String> updatedChatRefList, IMyItemViewListener itemListener) {
        mChatsTab.refreshList(updatedChatRefList, itemListener, true);
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
