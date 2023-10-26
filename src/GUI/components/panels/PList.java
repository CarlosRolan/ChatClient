package GUI.components.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import GUI.components.item.ItemAddUser;
import GUI.components.item.ItemMember;
import GUI.components.item.ItemView;
import GUI.components.item.ItemView.IMyItemViewListener;

public class PList extends JPanel {

    private volatile JPanel mContainer;
    private JScrollPane mScrollPane;

    private List<ItemView> mItemsView;

    public ItemView getItemView(String itemId) {
        for (ItemView itemRef : mItemsView) {
            if (itemId.equals(itemRef.getItemId()))
                return itemRef;
        }

        return null;
    }

    /* For shopwing the chat list */
    public PList() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        mContainer = new JPanel(new GridBagLayout());
        mScrollPane = new JScrollPane(mContainer);
        add(mScrollPane);
    }

    public void addItemMember(ItemMember view) {
        mContainer.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mContainer.add(view, gbc, 0);
        validate();
        repaint();
    }

    public void addUserToSelect(ItemAddUser view) {
        mContainer.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mContainer.add(view, gbc, 0);
        validate();
        repaint();
    }

    public void refreshList(List<String> list, IMyItemViewListener itemListener, boolean isChatList) {
        mContainer.removeAll();
        GridBagConstraints invisible = new GridBagConstraints();
        invisible.gridwidth = GridBagConstraints.BASELINE;
        invisible.weightx = 1;
        invisible.weighty = 1;
        mContainer.add(new JPanel(), invisible, 0);
        mItemsView = new ArrayList<>();
        for (String iRef : list) {
            ItemView userItem = ItemView.createItem(iRef, itemListener, isChatList);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.weightx = 1;
            //gbc.weighty = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            mContainer.add(userItem, gbc, 0);
            mItemsView.add(userItem);
        }
        validate();
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }

}
