package GUI.view.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.chat.Chat;
import com.chat.Member;

import GUI.view.item.MyItemView;

public class PList extends JPanel {

    private volatile JPanel mainList;
    private JScrollPane scroll;

    /* For shopwing the chat list */
    public PList() {
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
        scroll = new JScrollPane(mainList);
        add(scroll);
    }

    public void refreshChatList(List<String> chaList) {
        for (String string : chaList) {
            System.out.println("CHAT REF on ITEM " + string);
            Chat fromRef = Chat.initChat(string);
            MyItemView userItem = MyItemView.createChatItem(fromRef);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.weightx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            mainList.add(userItem, gbc, 0);
        }
        revalidate();
        repaint();
    }

    public void refreshConList(List<String> onlineList) {
        for (String string : onlineList) {
            System.out.println(string);
            String[] data = string.split(Member.SEPARATOR);
            MyItemView userItem = MyItemView.createUserItem(data[1], "bio");
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.weightx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            mainList.add(userItem, gbc, 0);
        }
        revalidate();
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }

}
