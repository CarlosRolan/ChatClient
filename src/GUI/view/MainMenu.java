package GUI.view;

import javax.swing.JFrame;
import javax.swing.tree.TreePath;

import com.chat.Chat;

import GUI.AppState;
import GUI.AppState.IUpdate;
import GUI.SwingUtils;
import GUI.components.TreeViewUsers;
import GUI.components.TreeViewUsers.ITreeViewListener;
import GUI.view.panels.ConversationPanel;
import GUI.view.panels.ConversationPanel.IChatLListener;

/**
 *
 * @author carlos
 */
public class MainMenu extends javax.swing.JFrame {

	public void setTabView() {

	}

	/**
	 * Creates new form MainMenu
	 */
	public MainMenu() {
		initComponents();
		AppState.getInstance().setOnUpdate(iUpdateListener);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		panel_list = new javax.swing.JScrollPane();
		tree_users = new TreeViewUsers(iTreeViewListener);
		jMenuBar1 = new javax.swing.JMenuBar();
		jMenu1 = new javax.swing.JMenu();
		jMenu2 = new javax.swing.JMenu();
		jMenu3 = new javax.swing.JMenu();
		p_chat_panel = new ConversationPanel();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		panel_list.setViewportView(tree_users);

		jMenu1.setText("Profile");
		jMenuBar1.add(jMenu1);

		jMenu2.setText("New Chat...");
		jMenu2.setToolTipText("");
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
								.addComponent(panel_list, javax.swing.GroupLayout.PREFERRED_SIZE, 200,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(p_chat_panel, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addContainerGap()));
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
										.addComponent(p_chat_panel, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(panel_list, javax.swing.GroupLayout.DEFAULT_SIZE, 361,
												Short.MAX_VALUE))
								.addContainerGap()));

		panel_list.getAccessibleContext().setAccessibleName("panel_list");
		p_chat_panel.getAccessibleContext().setAccessibleName("p_chat_panel");

		pack();
	}// </editor-fold>

	// Variables declaration - do not modify
	private javax.swing.JMenu jMenu1;
	private javax.swing.JMenu jMenu2;
	private javax.swing.JMenu jMenu3;
	private javax.swing.JMenuBar jMenuBar1;
	private javax.swing.JScrollPane panel_list;
	private TreeViewUsers tree_users;
	private ConversationPanel p_chat_panel;
	// End of variables declaration

	private final ITreeViewListener iTreeViewListener = new ITreeViewListener() {

		@Override
		public void onSingleRightClick(int selRow, TreePath selPath) {

			String userPathTag = selPath.getPathComponent(1).toString();

			if ("Users".equals(userPathTag)) {
				String[] receptorInfo = selPath.getLastPathComponent().toString().split("_");
				for (int i = 0; i < receptorInfo.length; i++) {
					System.out.println("INFO [" + i + "]" + receptorInfo[i]);
				}

				SwingUtils.executeOnSwingThread(new Runnable() {

					@Override
					public void run() {
						JFrame conversation = new JFrame(selPath.toString());
						conversation.add(new ConversationPanel(receptorInfo[0], receptorInfo[1], iChatListener));
						conversation.setResizable(false);
						conversation.pack();
						conversation.setVisible(true);
					}
				});
			}
		}

	};
	private final IUpdate iUpdateListener = new IUpdate() {

		@Override
		public void onUpdate() {
			tree_users.update();
		}

		@Override
		public void onNewChat(Chat newChat) {

		}

		@Override
		public void onMessageReceived(String emisorId) {
			tree_users.addAlertOnRef(emisorId);
		}

	};

	private final IChatLListener iChatListener = new IChatLListener() {

		@Override
		public void onMessageRecieved() {

		}

		@Override
		public void onMessageRead() {

		}

		@Override
		public void onMessageSent() {

		}

		@Override
		public void onMessageWritten() {

		}

	};

}
