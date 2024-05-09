package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.JPanel;

public class SportsMain {

	private JFrame frame;
	private JButton btnFootball;
	private JButton btnBaseball;
	private JLabel lblTitle;
	private JPanel panelTitle;
	private JButton btnAdminPage;
	private JLabel lblIconAlarm;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
				    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); // Swing 디자인을 LookAndFeel 테마로 변경
					SportsMain window = new SportsMain();
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
	public SportsMain() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 515, 565);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
//		ImageIcon icon = new ImageIcon("images/epl.png");
//		Image img = icon.getImage();
//		Image changeImg = img.getScaledInstance(230, 180, Image.SCALE_SMOOTH);
//		ImageIcon changeIcon = new ImageIcon(changeImg);
		
		btnFootball = new JButton(setImageSize("EPL/epl.png", 230, 180));
		btnFootball.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SportsFootball saf = new SportsFootball();
				saf.setVisible(true);
				frame.setVisible(false);
			}
		});
		
		btnBaseball = new JButton(setImageSize("KBO/kbo.png", 230, 180));
		btnBaseball.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SportsBaseball sab = new SportsBaseball();
				sab.setVisible(true);
				frame.setVisible(false);
			}
		});
		frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		
		panelTitle = new JPanel();
		frame.getContentPane().add(panelTitle);
		panelTitle.setLayout(null);
		
		lblIconAlarm = new JLabel(setImageSize("alarm.png", 30, 30));
		lblIconAlarm.setBounds(119, 58, 57, 46);
		panelTitle.add(lblIconAlarm);
		
		lblTitle = new JLabel("스포츠 중계 일정");
		lblTitle.setBounds(147, 58, 221, 46);
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setVerticalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("굴림", Font.BOLD, 22));
		panelTitle.add(lblTitle);
		
		btnAdminPage = new JButton("팀, 일정 관리");
		btnAdminPage.setFont(new Font("굴림", Font.PLAIN, 16));
		btnAdminPage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AdminMain admin = new AdminMain();
				admin.setVisible(true);
				frame.setVisible(false);
			}
		});
		btnAdminPage.setBounds(349, 10, 138, 23);
		panelTitle.add(btnAdminPage);
		frame.getContentPane().add(btnFootball);
		frame.getContentPane().add(btnBaseball);
	}
	
	public void setVisible(boolean b) {
		frame.setVisible(b);
	}
	
	public ImageIcon setImageSize(String imgUrl , int x, int y) {
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(imgUrl));
		Image img = icon.getImage();
		Image changeImg = img.getScaledInstance(x, y, Image.SCALE_SMOOTH);
		ImageIcon changeIcon = new ImageIcon(changeImg);
		return changeIcon;
	}
}
