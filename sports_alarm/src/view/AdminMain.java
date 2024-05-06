package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AdminMain extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnSchedule;
	private JButton btnEditTeam;
	private JButton btnMain;

	/**
	 * Launch the application.
	 */
	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminMain frame = new AdminMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public AdminMain() {
		initialize();
	}

	/**
	 * Create the frame.
	 */
	public void initialize() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btnSchedule = new JButton("Edit Schedule");
		btnSchedule.setBounds(5, 140, 424, 80);
		contentPane.add(btnSchedule);
		
		btnEditTeam = new JButton("Edit Team");
		btnEditTeam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AdminEditTeam admin = new AdminEditTeam();
				admin.setVisible(true);
				dispose();
			}
		});
		btnEditTeam.setBounds(5, 50, 424, 80);
		contentPane.add(btnEditTeam);
		
		btnMain = new JButton("Home");
		btnMain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SportsAlarmMain sam = new SportsAlarmMain();
				sam.setVisible(true);
				dispose();
			}
		});
		btnMain.setBounds(332, 10, 97, 23);
		contentPane.add(btnMain);
	}
	
}
