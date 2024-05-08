package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

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
		
		btnSchedule = new JButton("일정 관리");
		btnSchedule.setFont(new Font("굴림", Font.BOLD, 20));
		btnSchedule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AdminScheduleEdit admin = new AdminScheduleEdit();
				admin.setVisible(true);
				dispose();
			}
		});
		btnSchedule.setBounds(5, 140, 424, 80);
		contentPane.add(btnSchedule);
		
		btnEditTeam = new JButton("팀 관리");
		btnEditTeam.setFont(new Font("굴림", Font.BOLD, 20));
		btnEditTeam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AdminTeamEdit admin = new AdminTeamEdit();
				admin.setVisible(true);
				dispose();
			}
		});
		btnEditTeam.setBounds(5, 50, 424, 80);
		contentPane.add(btnEditTeam);
		
		btnMain = new JButton("Home");
		btnMain.setFont(new Font("굴림", Font.BOLD, 12));
		btnMain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SportsMain sam = new SportsMain();
				sam.setVisible(true);
				dispose();
			}
		});
		btnMain.setBounds(332, 10, 97, 23);
		contentPane.add(btnMain);
	}
	
}
