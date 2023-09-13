package GUI.view;

import javax.swing.DefaultListModel;
import javax.swing.tree.TreePath;

import com.chat.Chat;
import com.data.MSG;

import GUI.GUI;
import GUI.GUI.IGUIListener;
import GUI.SwingUtils;
import GUI.view.components.MyTreeView;
import GUI.view.components.MyTreeView.ITreeViewListener;
import GUI.view.panels.ConversationPanel;
import GUI.view.panels.ConversationPanel.IChatLListener;
import GUI.view.panels.ListPanel;
import controller.manager.FileManager;

/**
 *
 * @author carlos
 */
public class MainMenu extends javax.swing.JFrame {

	/**
	 * Creates new form MainMenu
	 */
	public MainMenu() {
		initComponents();
		GUI.getInstance().setOnUpdate(iUpdateListener);
	}

	private void initComponents() {

		panel_list = new javax.swing.JScrollPane();
		tree_users = new MyTreeView(iTreeViewListener);
		fragment = new ListPanel();
		jMenuBar1 = new javax.swing.JMenuBar();
		jMenu1 = new javax.swing.JMenu();
		jMenu2 = new javax.swing.JMenu();
		jMenu3 = new javax.swing.JMenu();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		panel_list.setViewportView(tree_users);

		javax.swing.GroupLayout fragemntLayout = new javax.swing.GroupLayout(fragment);
		fragment.setLayout(fragemntLayout);
		fragemntLayout.setHorizontalGroup(
				fragemntLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGap(0, 221, Short.MAX_VALUE));
		fragemntLayout.setVerticalGroup(
				fragemntLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGap(0, 0, Short.MAX_VALUE));

		jMenu1.setText("Profile");
		jMenuBar1.add(jMenu1);

		jMenu2.setText("New Chat...");
		jMenu2.setToolTipText("");
		jMenu2.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				menuItem2Click();
			}
		});
		jMenuBar1.add(jMenu2);

		jMenu3.setText("Settings");
		jMenu3.setActionCommand("");
		jMenuBar1.add(jMenu3);

		setJMenuBar(jMenuBar1);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(panel_list, javax.swing.GroupLayout.PREFERRED_SIZE, 131,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(fragment, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addContainerGap()));
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(panel_list, javax.swing.GroupLayout.DEFAULT_SIZE, 361,
												Short.MAX_VALUE)
										.addComponent(fragment, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addContainerGap()));

		panel_list.getAccessibleContext().setAccessibleName("panel_list");

		pack();
	}// </editor-fold>

	// Variables declaration - do not modify
	private javax.swing.JMenu jMenu1;
	private javax.swing.JMenu jMenu2;
	private javax.swing.JMenu jMenu3;
	private javax.swing.JMenuBar jMenuBar1;
	private ListPanel fragment;
	private javax.swing.JScrollPane panel_list;
	private MyTreeView tree_users;
	// End of variables declaration

	private void menuItem2Click() {

		/*String chatTitle = JOptionPane.showInputDialog("Title of the chat");
		String chatDesc = null;
		if (chatTitle != null) {
			chatDesc = JOptionPane.showInputDialog("Chat's description");
			if (chatDesc != null) {
				//ListPanel.showSelectorOnWindow(GUI.getInstance().getConRefList());
			}
		}*/

		GUI.getInstance().pClientCon.newChat("title", "desc", GUI.getInstance().getChatRefList().size());

	}

	private final ITreeViewListener iTreeViewListener = new ITreeViewListener() {

		@Override
		public void onSingleRightClick(int selRow, TreePath selPath) {

			String userPathTag = selPath.getPathComponent(1).toString();
			String[] receptorInfo = selPath.getLastPathComponent().toString().split("_");

			if ("Users".equals(userPathTag)) {

				for (int i = 0; i < receptorInfo.length; i++) {
					System.out.println("INFO [" + i + "]" + receptorInfo[i]);
				}
			}

			String receptorId = receptorInfo[0];
			String receptorNick = receptorInfo[1];

			// showOnWindow
			ConversationPanel.showOnWindow(receptorId, receptorNick, iChatListener);
		}

	};
	private final IGUIListener iUpdateListener = new IGUIListener() {

		@Override
		public void onUpdate() {
			tree_users.update();
		}

		@Override
		public void onNewChat(Chat chat) {
			fragment.addChatItem(chat);
			tree_users.updateChats();
			tree_users.refreshTreeView();
		}

		@Override
		public void onMessageReceived(MSG msg) {
			tree_users.addAlertOnRef(msg.getEmisor());
			iChatListener.onMessageRecieved(msg);
		}

	};

	private final IChatLListener iChatListener = new IChatLListener() {

		@Override
		public void onMessageRead() {
			// TODO double check

		}

		@Override
		public void onMessageSent(String nick, String dateTime, String text, DefaultListModel<String> listModel) {

			FileManager.getInstance().saveHistory(nick, dateTime, text);
			SwingUtils.executeOnSwingThread(new Runnable() {

				@Override
				public void run() {
					String msgAdded = "[" + dateTime + "]" + nick + ":" + text;
					listModel.addElement(msgAdded);
				}
			});

		}

		@Override
		public void onMessageWritten() {
			// TODO sent CHECK

		}

	};

}
