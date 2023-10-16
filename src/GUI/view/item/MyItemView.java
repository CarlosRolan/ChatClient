package GUI.view.item;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

import com.chat.Chat;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */

/**
 *
 * @author carlos
 */

public class MyItemView extends javax.swing.JPanel {

	private Border hover = BorderFactory.createLineBorder(Color.DARK_GRAY);

	private final IMyItemViewListener iMyItemViewListener;

	private final String mId;
	private String mTitle;
	private String mSubTitle;
	private boolean mIsChat;

	public static MyItemView createItemView(String id, String nick, String bio, IMyItemViewListener listener) {
		return new MyItemView(id, nick, bio, listener, false);
	}

	public static MyItemView createItemView(Chat chat, IMyItemViewListener listener) {
		String itemId = chat.getChatId();
		String itemTitle = chat.getTitle();
		String itemSubTitle = chat.getDescription();
		return new MyItemView(itemId, itemTitle, itemSubTitle, listener, true);
	
	}

	/**
	 * Creates new form Conversation
	 */
	private MyItemView(String itemId, String itemTitle, String itemSubitle, IMyItemViewListener listener, boolean isChat) {
		iMyItemViewListener = listener;
		mId = itemId;
		mIsChat = isChat;
		mTitle = itemTitle;
		mSubTitle = itemSubitle;
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

		mImage = new java.awt.Canvas();
		mLabel0 = new javax.swing.JLabel();
		mLabel1 = new javax.swing.JLabel();

		setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY));

		mImage.setBackground(new java.awt.Color(0, 153, 153));
		mImage.setMaximumSize(new java.awt.Dimension(64, 64));
		mImage.setMinimumSize(new java.awt.Dimension(32, 32));
		mImage.setPreferredSize(new java.awt.Dimension(64, 64));

		mLabel0.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N

		mLabel0.setText(mTitle);
		mLabel1.setText(mSubTitle);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(mImage,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(
										javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(mLabel0)
										.addComponent(mLabel1))
								.addContainerGap()));
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(layout.createParallelGroup(
										javax.swing.GroupLayout.Alignment.LEADING,
										false)
										.addComponent(mImage,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGroup(layout.createSequentialGroup()
												.addComponent(mLabel0)
												.addGap(0, 0, 0)
												.addComponent(mLabel1)))
								.addContainerGap()));
		addMouseListener(mouseListener);
	}// </editor-fold>

	// Variables declaration - do not modify
	private java.awt.Canvas mImage;
	private javax.swing.JLabel mLabel0;
	private javax.swing.JLabel mLabel1;
	// End of variables declaration

	/* IMPLEMENTATIONs */

	private final MouseListener mouseListener = new MouseListener() {

		@Override
		public void mouseClicked(MouseEvent e) {

			System.out.println("Btn clicked " + e.getButton());
			switch (e.getButton()) {
				case MouseEvent.BUTTON1:
					iMyItemViewListener.onItemRightClick(mId, mTitle, mSubTitle, mIsChat);
					break;

				case MouseEvent.BUTTON2:
					break;

			}

		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {

		}

		@Override
		public void mousePressed(MouseEvent e) {

		}

		@Override
		public void mouseReleased(MouseEvent e) {

		}

	};

	public interface IMyItemViewListener {
		void onItemRightClick(String itemId, String itemTitle, String itemSubtitle, boolean isChat);
	}

}