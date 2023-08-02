package GUI;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Panel;
import java.awt.TextField;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;



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
		
		JLabel lb_ChatGroup_name = new JLabel("ChatGroup_Name");
		lb_ChatGroup_name.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel_north.add(lb_ChatGroup_name);
		
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
