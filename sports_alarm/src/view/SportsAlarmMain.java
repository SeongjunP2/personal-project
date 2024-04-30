package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.Font;
import javax.swing.SwingConstants;

public class SportsAlarmMain {

	private JFrame frame;
	private JButton btnFootball;
	private JButton btnBaseball;
	private JLabel lblTitle;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SportsAlarmMain window = new SportsAlarmMain();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SportsAlarmMain() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 515, 565);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		btnFootball = new JButton("football");
		btnFootball.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SportsAlarmFootball saf = new SportsAlarmFootball();
				saf.setVisible(true);
				frame.setVisible(false);
			}
		});
		
		btnBaseball = new JButton("baseball");
		btnBaseball.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SportsAlarmBaseball sab = new SportsAlarmBaseball();
				sab.setVisible(true);
				frame.setVisible(false);
			}
		});
		frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		
		lblTitle = new JLabel("AlarmProgram");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("굴림", Font.BOLD, 12));
		frame.getContentPane().add(lblTitle);
		frame.getContentPane().add(btnFootball);
		frame.getContentPane().add(btnBaseball);
	}
}
