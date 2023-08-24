package GUI.view.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.chat.Chat;

import GUI.components.ChatItem;

public class ChatListPanel extends JPanel {

    private JPanel mainList;

    public ChatListPanel() {
        initComponents();
    }

    public void initComponents() {
        setLayout(new BorderLayout());
        mainList = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        // REMAINDER places it below last, and BASELINE above
        gbc.gridwidth = GridBagConstraints.BASELINE;
        gbc.weightx = 1;
        gbc.weighty = 1;
        mainList.add(new JPanel(), gbc);

        add(new JScrollPane(mainList));
    }

    public void addChatItem(Chat chat) {
        ChatItem chatItem = new ChatItem(chat);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainList.add(chatItem, gbc, 0);

        validate();
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }
}