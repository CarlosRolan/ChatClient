package GUI.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.ref.WeakReference;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;

import com.chat.Chat;
import com.chat.Member;
import com.data.MSG;

import GUI.GUI;
import GUI.GUI.IGUIListener;
import GUI.view.components.MyTreeView;
import GUI.view.components.MyTreeView.ITreeViewListener;
import GUI.view.components.MyUserPicker;
import GUI.view.components.item.ItemView;
import GUI.view.components.item.ItemView.IMyItemViewListener;
import GUI.view.components.panels.PConv;
import GUI.view.components.panels.PConv.IConvListener;
import GUI.view.components.panels.PTabbs;

/**
 *
 * @author carlos
 */
public class MainView extends JFrame {

	private WeakReference<MainView> mWeakReference;

	/**
	 * Creates new form ChatClient
	 */
	public MainView() {
		mWeakReference = new WeakReference<MainView>(this);
		initComponents();
		GUI.getInstance().setUpdateListener(iUpdateListener);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		setTitle(GUI.getInstance().getSession().getNick());

		mScrollPane = new javax.swing.JScrollPane();
		mTreeView = new MyTreeView(iTreeViewListener);
		mTabbs = new PTabbs();

		mMenuBar = new MenuMain();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		mScrollPane.setViewportView(mTreeView);

		setJMenuBar(mMenuBar);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(mScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 131,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(mTabbs)
								.addContainerGap()));
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(mScrollPane)
										.addComponent(mTabbs))
								.addContainerGap()));

		mScrollPane.getAccessibleContext().setAccessibleName("panel_list");

		mScrollPane.setVisible(false);

		pack();
	}// </editor-fold>

	// Variables declaration - do not modify
	private MenuMain mMenuBar;
	private javax.swing.JScrollPane mScrollPane;
	private PTabbs mTabbs;
	private MyTreeView mTreeView;
	// End of variables declaration

	/* IMPLEMENTATIONs */

	private final IMyItemViewListener iMyItemViewListener = new IMyItemViewListener() {

		@Override
		public void onRightClick(String itemId, String itemTitle, String itemSubtitle) {

			PConv instance = GUI.getInstance().getPanelInstance(itemId);

			if (instance == null) {
				System.out.println("Single COnv is null");
				instance = PConv.createSingleConv(itemId, itemTitle, itemSubtitle, iConvListener);
				System.out.println("Creating new PConv of type Syngle");
				GUI.getInstance().addPanelInstance(instance);
			}

			if (!instance.isOpen()) {
				try {
					ConversationView.showOnWindow(instance);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void onNewMsg(String id, boolean isChat) {

			ItemView itemView = null;
			if (isChat) {
				itemView = mTabbs.getChatsTab().getItemView(id);
			} else {
				itemView = mTabbs.getUsersTab().getItemView(id);
			}

			if (itemView != null) {
				itemView.oneMoreMsg();
			}

		}

		@Override
		public void onRightClick(Chat chat) {
			PConv instance = GUI.getInstance().getPanelInstance(chat.getChatId());

			if (instance == null) {
				System.out.println("is null");
				instance = PConv.createChatInstance(chat, iConvListener);
				GUI.getInstance().addPanelInstance(instance);
			}

			if (!instance.isOpen()) {
				try {
					ConversationView.showOnWindow(instance);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	};

	private final IConvListener iConvListener = new IConvListener() {

		@Override
		public void onMsgSent(String convId, String convTitle, String convSubTitle, String line, boolean isChat) {
			if (isChat) {
				String chatId = convId;
				String emisroId = GUI.getInstance().getSession().getConId();
				String emisorNick = GUI.getInstance().getSession().getNick();
				GUI.getInstance().getSession().sendMsgToChat(chatId, emisroId, emisorNick, line);
			} else {
				GUI.getInstance().getSession().sendToSingle(convId, convTitle, line);
			}
		}

		@Override
		public void onMsgRecieved(MSG msgRecieved, boolean isChat) {

			String convId;
			String convTitle;
			String convSubtitle;
			String line;

			if (isChat) {
				/*
				 * CHAT
				 * toChat.setAction(MSG_FROM_CHAT);
				 * toChat.setEmisor(currentChat.getChatId());
				 * toChat.setReceptor(currentChat.getTitle());
				 * toChat.setParameter(0, currentChat.getDescription());
				 * toChat.setBody(line);
				 */
				convId = msgRecieved.getEmisor();
				convTitle = msgRecieved.getReceptor();
				convSubtitle = msgRecieved.getParameter(0);
				line = msgRecieved.getBody();
			} else {

				/*
				 * SINGLE
				 * directMSG.setAction(MSG_FROM_SINGLE);
				 * directMSG.setEmisor(emisorId);
				 * directMSG.setReceptor(receptorId);
				 * directMSG.setParameter(0, emisorNick);
				 * directMSG.setBody(text);
				 */
				convId = msgRecieved.getEmisor();
				convTitle = msgRecieved.getParameter(0);
				convSubtitle = "bio";
				line = msgRecieved.getBody();
			}

			// Nunca va aa ser null
			PConv instance = GUI.getInstance().getPanelInstance(convId);

			if (instance == null) {
				instance = PConv.createSingleConv(convId, convTitle, convSubtitle, iConvListener);
			}

			if (!instance.isOpen()) {
				iMyItemViewListener.onNewMsg(msgRecieved.getEmisor(), isChat);
			}

			instance.addLine(line);

		}

	};

	// TreeViewListener
	private final ITreeViewListener iTreeViewListener = new ITreeViewListener() {

		@Override
		public void onSingleRightClick(int selRow, TreePath selPath) {

			String userPathTag = selPath.getPathComponent(1).toString();
			String[] receptorInfo = selPath.getLastPathComponent().toString().split(Member.SEPARATOR);

			String receptorId = receptorInfo[0];
			String receptorNick = receptorInfo[1];

			if ("Users".equals(userPathTag)) {
				for (int i = 0; i < receptorInfo.length; i++) {
					System.out.println("INFO [" + i + "]" + receptorInfo[i]);
				}

			} else {

			}
		}
	};
	private final IGUIListener iUpdateListener = new IGUIListener() {

		@Override
		public void updateUsers() {
			// por lo que dijo mario
			List<String> temp = GUI.getInstance().getUserRefList();
			mTabbs.refreshUsersTab(temp, iMyItemViewListener);
		}

		@Override
		public void updateChats() {
			// por lo que dijo mario
			List<String> temp = GUI.getInstance().getChatRefList();
			mTabbs.refresChatsTab(temp, iMyItemViewListener);
		}

		@Override
		public void onMessageReceived(MSG msgReceived, boolean isChat) {
			if (isChat) {
				mTabbs.addNotificationOnChatsTab();
			} else {
				mTabbs.addNotificationOnUsersTab();
			}
			iConvListener.onMsgRecieved(msgReceived, isChat);
		}

		@Override
		public void addedToChat(MSG chatInfo) {
			/*
			 * respond = new MSG(MSG.Type.REQUEST);
			 * respond.setAction(REQ_INIT_CHAT);
			 * respond.setEmisor(chat.getChatId());
			 * respond.setReceptor(chat.getTitle());
			 * respond.setBody(chat.getDescription());
			 * respond.setParameters(chat.getMembersRef());
			 */

			Chat chat = Chat.instanceChat(chatInfo);
			PConv instance = GUI.getInstance().getPanelInstance(chat.getChatId());

			if (instance == null) {
				instance = PConv.createChatInstance(chat, iConvListener);
				GUI.getInstance().addPanelInstance(instance);
			}
		}

	};

	public class MenuMain extends JMenuBar {

		private final String ITEM_0 = "Profile";

		private final String ITEM_1 = "New Chat..";

		private final String ITEM_2 = "Settings";
		private final String ITEM_2_0 = "Tree View";
		private final String ITEM_2_1 = "Tab View";

		public final String[] MENU_ITEMS = { ITEM_0, ITEM_1, ITEM_2 };

		public MenuMain() {
			initComponents();
		}

		private void initComponents() {
			initItems();
		}

		private void initItems() {

			JMenuItem iProfile = new JMenuItem(MENU_ITEMS[0]);
			iProfile.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					itemAction0();
				}

			});
			JMenuItem iNewChat = new JMenuItem(MENU_ITEMS[1]);
			iNewChat.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					actionNewChat();
				}

			});
			JMenu iSettings = new JMenu(MENU_ITEMS[2]);
			iSettings.add(new JMenuItem(ITEM_2_0));
			iSettings.add(new JMenuItem(ITEM_2_1));

			add(iProfile);
			add(iNewChat);
			add(iSettings);

		}

		private void itemAction0() {

		}

		public void enableTabView() {
			mScrollPane.setVisible(false);
			mTabbs.setVisible(true);
		}

		public void enableTreeView() {
			mScrollPane.setVisible(true);
			mTabbs.setVisible(false);
		}

		private void actionNewChat() {

			int addingMembers = 0;
			String newChatTitle = JOptionPane.showInputDialog("Title of the chat");
			String newChatDesc = null;

			if (!"".equals(newChatTitle) && newChatTitle != null) {
				newChatDesc = JOptionPane.showInputDialog("Chat's description");
				if ("".equals(newChatDesc)) {
					newChatDesc = "Empty description";
				}
				if (newChatDesc != null) {
					addingMembers = JOptionPane.showConfirmDialog(null, "Adding new MEMBERs");
					switch (addingMembers) {
						case JOptionPane.CANCEL_OPTION:
							break;
						case JOptionPane.OK_OPTION:
							// Por lo que diojo MArio
							List<String> tempConRefs = GUI.getInstance().getUserRefList();

							// IMPORTANT Notice that the picker is a modal Jdialog and no a Jframe in a
							// diferent Thread
							MyUserPicker.selectMembers(mWeakReference.get(), true, tempConRefs, newChatTitle,
									newChatDesc);
							break;
						case JOptionPane.NO_OPTION:
							boolean created = GUI.getInstance().getSession().createNewChat(newChatTitle, newChatDesc,
									null);
							if (created) {
								JOptionPane.showMessageDialog(MainView.this, "Chat created");
							} else {
								JOptionPane.showMessageDialog(MainView.this, "A chat with that name already exits");
							}
							break;

						case JOptionPane.CLOSED_OPTION:
							break;
						default:
							break;
					}
				}
			}
		}
	}

}
