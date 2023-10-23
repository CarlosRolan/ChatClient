package GUI.view.components;

import java.awt.Component;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import GUI.view.components.item.MyNewPanel;

@SuppressWarnings("serial")
public class MyList extends JList<JPanel> {

    /* STATIC */

    public static MyList createList(List<String> selection) {

        DefaultListModel<JPanel> model = new DefaultListModel<JPanel>();
        for (String iMemberRef : selection) {
            MyNewPanel itemPanel = new MyNewPanel(iMemberRef);
            model.addElement(itemPanel);
        }
        return new MyList(model);
    }

    protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

    private MyList(ListModel<JPanel> model) {
        setCellRenderer(new CellRenderer());
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setModel(model);
    }

    protected class CellRenderer implements ListCellRenderer<JPanel> {
        public Component getListCellRendererComponent(
                JList<? extends JPanel> list, JPanel value, int index,
                boolean isSelected, boolean cellHasFocus) {
            JPanel checkbox = value;

            return checkbox;
        }
    }
}