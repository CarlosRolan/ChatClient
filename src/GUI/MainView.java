package GUI;

import java.awt.EventQueue;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import java.awt.List;
import java.awt.Panel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import java.awt.TextField;
import javax.swing.JFormattedTextField;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JToolBar;
import java.awt.Scrollbar;
import java.awt.FlowLayout;
import java.awt.ScrollPane;
import javax.swing.JDesktopPane;

public class MainView extends JFrame {

	private JPanel contentPane;

	private JButton btnSend;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainView frame = new MainView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 679, 493);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		Panel panel_north = new Panel();
		contentPane.add(panel_north, BorderLayout.NORTH);
		
		JLabel lb_chat_name = new JLabel("Chat_Name");
		lb_chat_name.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel_north.add(lb_chat_name);
		
		Panel panel_east = new Panel();
		contentPane.add(panel_east, BorderLayout.EAST);
		
		Panel panel_shouth = new Panel();
		contentPane.add(panel_shouth, BorderLayout.SOUTH);
		
		TextField edTxtInput = new TextField();
		edTxtInput.setText("Write your message");
		edTxtInput.setColumns(50);
		panel_shouth.add(edTxtInput);
		
		JButton btnSend = new JButton("SEND");
		panel_shouth.add(btnSend);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.WEST);
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
	}


}
