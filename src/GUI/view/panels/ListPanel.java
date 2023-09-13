package GUI.view.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.chat.Chat;

import GUI.SwingUtils;
import GUI.view.item.ChatItem;

public class ListPanel extends JPanel {

    public static void showSelectorOnWindow(List<String> users) {
        SwingUtils.executeOnSwingThread(new Runnable() {

            @Override
            public void run() {
                JFrame selView = new JFrame();
                ListPanel listPanel = new ListPanel(users);
                selView.add(listPanel);
                selView.setResizable(false);
                selView.pack();
                selView.setVisible(true);
            }

        });
    }

    public static void showChatListOnWindow(List<String> users, List<String> chats) {
        SwingUtils.executeOnSwingThread(new Runnable() {

            @Override
            public void run() {
                JFrame selView = new JFrame();
                ListPanel listPanel = new ListPanel();
                selView.add(listPanel);
                selView.setResizable(false);
                selView.pack();
                selView.setVisible(true);
            }

        });
    }

    private JPanel listPanel;

    /* For shopwing the chat list */
    public ListPanel() {
        initComponents();
    }

    /* for showing the userList */
    private ListPanel(List<String> users, List<String> chats) {
        initComponents();
        for (String string : users) {
            addSelectionOp(string);
        }
        for (String string : chats) {
            addSelectionOp(string);
        }
    }

    /* for showing the userList */
    private ListPanel(List<String> users) {
        initComponents();
        for (String string : users) {
            addSelectionOp(string);
        }
    }

    public void initComponents() {
        setLayout(new BorderLayout());
        listPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        // REMAINDER places it below last, and BASELINE above
        gbc.gridwidth = GridBagConstraints.BASELINE;
        gbc.weightx = 1;
        gbc.weighty = 1;
        listPanel.add(new JPanel(), gbc);

        add(new JScrollPane(listPanel));
    }

    public void addChatItem(Chat chat) {
        ChatItem chatItem = new ChatItem(chat);
        GridBagConstraints gbc = new GridBagConstraints();
        // REMAINDER places it below last, and BASELINE above
        gbc.gridwidth = GridBagConstraints.BASELINE;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        listPanel.add(chatItem, gbc, 0);

        add(new JScrollPane(listPanel));

        validate();
        repaint();
    }

    public void addSelectionOp(String ref) {
        JCheckBox ckSelector = new JCheckBox(ref);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        listPanel.add(ckSelector, gbc, 0);

        validate();
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }

    public interface IListener {
        void addChat(Chat chat);
    }
}
