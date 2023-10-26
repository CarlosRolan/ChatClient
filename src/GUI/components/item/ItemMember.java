package GUI.components.item;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import com.chat.Member;

public class ItemMember extends JPanel {

    public static ItemMember createPickerInstance(Member member, boolean inChat, IIemMemberListener listener) {
        return new ItemMember(member, inChat, listener);
    }

    public static ItemMember createManagingInstance(Member member, IIemMemberListener listener) {
        return new ItemMember(member, listener);
    }

    private final IIemMemberListener mListener;
    private Member mMember;

    /**
     * Creates new form MemberItem
     */
    public ItemMember(Member member, boolean inChat, IIemMemberListener listener) {
        mListener = listener;
        mMember = member;
        initComponents(inChat);
    }

    public ItemMember(Member member, IIemMemberListener listener) {
        mListener = listener;
        mMember = member;
        initComponents();
    }

    private void setBtnCkBox() {
        mCkBox = new JCheckBox();
        mCkBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    mListener.addMember(mMember);
                } else {
                    mListener.deleteMember(mMember);
                }
            }

        });
    }

    private void setBtn(boolean inChat) {
        if (mBtn == null) {
            mBtn = new javax.swing.JButton();
        }

        if (inChat) {
            mBtn.setText("x");
            mBtn.setBackground(Color.RED);
            mBtn.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    mListener.deleteMember(mMember);
                    setBtn(false);
                }

            });
        } else {
            mBtn.setText("+");
            mBtn.setBackground(Color.GREEN);
            mBtn.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    mListener.addMember(mMember);
                    setBtn(true);
                }

            });
        }
    }

    private void setComboBox() {
        if (mComboBox == null) {
            mComboBox = new javax.swing.JComboBox<>();
        }

        mComboBox.setModel(
                new javax.swing.DefaultComboBoxModel<>(new String[] { "ADMIN", "REGULAR" }));

        if (mMember.isAdmin()) {
            mComboBox.setSelectedIndex(0);
        } else {
            mComboBox.setSelectedIndex(1);
        }

        mComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedItem = (String) mComboBox.getSelectedItem();

                switch (selectedItem) {
                    case "ADMIN":
                        mMember.makeAdmin();
                        break;
                    case "REGULAR":
                        mMember.makeRegular();
                        break;
                    default:
                        mMember.makeRegular();
                        break;
                }
                mListener.editRights(mMember);
            }
        });
    }

    // Chat creation
    private void initComponents() {

        mBtn = new javax.swing.JButton();
        mComboBox = new javax.swing.JComboBox<>();
        mCkBox = new JCheckBox();
        mLabel0 = new javax.swing.JLabel();

        mLabel0.setText(mMember.getNick());

        final javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(mCkBox, javax.swing.GroupLayout.PREFERRED_SIZE, 25,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(mLabel0)
                                .addGap(18, 18, 18)
                                .addComponent(mComboBox, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(mBtn)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(mCkBox, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(mLabel0)
                                                        .addComponent(mComboBox,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(mBtn))
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap()));
    }

    // Managing members
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents(boolean inChat) {

        mBtn = new javax.swing.JButton();
        mComboBox = new javax.swing.JComboBox<>();
        mCkBox = new JCheckBox();
        mLabel0 = new javax.swing.JLabel();

        mLabel0.setText(mMember.getNick());

        final javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(mCkBox, javax.swing.GroupLayout.PREFERRED_SIZE, 25,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(mLabel0)
                                .addGap(18, 18, 18)
                                .addComponent(mComboBox, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(mBtn)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(mCkBox, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(mLabel0)
                                                        .addComponent(mComboBox,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(mBtn))
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap()));
    }// </editor-fold>

    // Variables declaration - do not modify
    private javax.swing.JButton mBtn;
    private javax.swing.JCheckBox mCkBox;
    private javax.swing.JComboBox<String> mComboBox;
    private javax.swing.JLabel mLabel0;
    // End of variables declaration

    public interface IIemMemberListener {
        void addMember(Member member);

        void deleteMember(Member member);

        void editRights(Member member);
    }
}
