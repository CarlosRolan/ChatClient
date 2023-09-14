package GUI.view.components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

/**
 *
 * @author carlos
 */
public class MyListPicker extends javax.swing.JDialog {

    private volatile JPanel mainList;

    private final List<String> mOpList;
    private List<String> selectedOptions;

    public static void showPicker(List<String> options) {

        MyListPicker dialog = new MyListPicker(new javax.swing.JFrame(), true, options);
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                dialog.dispose();
            }
        });
        dialog.setVisible(true);
    }

    public List<String> getSelected() {
        return selectedOptions;
    }

    /**
     * Creates new form newJDialog
     */
    public MyListPicker(java.awt.Frame parent, boolean modal, List<String> options) {
        super(parent, modal);
        mOpList = options;
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jButton1.setText("jButton1");

        jButton1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
            
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1)
                                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 388,
                                                Short.MAX_VALUE))
                                .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1)
                                .addContainerGap()));

        mainList = new JPanel(new GridBagLayout());
        initOptions();

        pack();
    }// </editor-fold>

    public void initOptions() {
        for (String string : mOpList) {
            JCheckBox checkBox = new JCheckBox(string);

            checkBox.addItemListener(new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (ItemEvent.SELECTED == e.getStateChange()) {
                        selectedOptions.add(checkBox.getText());
                    }
                }
            });
            GridBagConstraints gbc = new GridBagConstraints();
            // REMAINDER places it below last, and BASELINE above
            gbc.gridwidth = GridBagConstraints.BASELINE;
            gbc.weightx = 1;
            gbc.weighty = 1;
            mainList.add(checkBox, gbc);
        }
    }

    // Variables declaration - do not modify
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration

}
