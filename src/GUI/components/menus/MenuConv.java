package GUI.components.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.chat.Chat;

import GUI.components.panels.PChat;
import GUI.components.panels.PConv;
import GUI.components.panels.PSingle;

public class MenuConv extends JMenuBar {

    private final String ITEM_0 = "Show info";

    private final String ITEM_1 = "Leave chat";

    private final String ITEM_2 = "Settings";

    private final String ITEM_ADMIN_2_0 = "Edit Members";
    private final String ITEM_ADMIN_2_1 = "Edit Tittle";
    private final String ITEM_ADMIN_2_2 = "Edit Description";
    private final String ITEM_ADMIN_2_3 = "Delete chat";

    private final IMenuConvListener mListener;

    public MenuConv(PConv instance, IMenuConvListener menuConvListener) {
        mListener = menuConvListener;
        if (instance instanceof PChat) {
            initComponents((PChat) instance);
        } else if (instance instanceof PSingle) {
            initComponents((PSingle) instance);
        }
    }

    // for PChat instance
    private void initComponents(PChat instance) {
        JMenuItem item0 = new JMenuItem(ITEM_0);
        add(item0);

        JMenuItem item1 = new JMenuItem(ITEM_1);
        add(item1);

        initAdmintItems(instance.hasAdminRights(), instance);
    }

    // for PSingle instance
    private void initComponents(PSingle instance) {
        JMenuItem item0 = new JMenuItem(ITEM_0);
        add(item0);
        JMenuItem item1 = new JMenuItem(ITEM_1);
        add(item1);
    }

    // if Client is Admin in a PChat instance
    private void initAdmintItems(boolean isAdmin, PChat instance) {

        JMenu item2 = new JMenu();
        item2.setText(ITEM_2);
        if (isAdmin) {
            JMenuItem editMembers = new JMenuItem(ITEM_ADMIN_2_0);
            editMembers.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    iEditMembers(instance);
                }

            });

            item2.add(editMembers);

            JMenuItem editTitle = new JMenuItem(ITEM_ADMIN_2_1);
            editTitle.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    iEditTitle(instance);
                }

            });
            item2.add(editTitle);

            JMenuItem editDesc = new JMenuItem(ITEM_ADMIN_2_2);
            editDesc.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {

                }

            });
            item2.add(editDesc);
            JMenuItem deleteChat = new JMenuItem(ITEM_ADMIN_2_3);
            deleteChat.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {

                }

            });

        }
        add(item2);

    }

    /* Only for PChat */
    private void iLeaveChat() {

    }

    /* Only for PChat admin */
    private void iEditMembers(PChat instance) {
        mListener.editMembers(instance);
    }

    private void iEditTitle(PChat instance) {
        String updatedTitle = JOptionPane
                .showInputDialog("Write the NEW TITLE for the CHAT " + instance.getTitle());
        instance.editChatTitle(updatedTitle);
        mListener.editChat(instance);
    }

    /* For both PSingle and PHhat */
    private void showConvInfo() {

    }

    public interface IMenuConvListener {
        void editChat(PChat instance);

        void editMembers(PChat instance);

        void deleteChat(Chat deleted);
    }

}
