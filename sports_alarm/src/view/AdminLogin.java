package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class AdminLogin {

	private JFrame frame;
	private JTextField textFieldPassword;
	private JPanel panelTitle;
	private JLabel lblTitle;
	private JButton btnBack;
	private JPanel panel;
	private JLabel lblId;
	private JLabel lblPassword;
	private JTextField textFieldId;
	private JButton btnLogin;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminLogin window = new AdminLogin();
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
	public AdminLogin() {
		initialize();
	}
	
	public void setVisible(boolean b) {
		frame.setVisible(b);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		panelTitle = new JPanel();
		panelTitle.setBounds(0, 0, 434, 92);
		frame.getContentPane().add(panelTitle);
		panelTitle.setLayout(null);
		
		lblTitle = new JLabel("AdminLogin");
		lblTitle.setBounds(178, 47, 67, 15);
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		panelTitle.add(lblTitle);
		
		btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
        		SportsAlarmMain sam = new SportsAlarmMain();
        		sam.setVisible(true);
				frame.setVisible(false);
			}
		});
		btnBack.setBounds(337, 10, 97, 23);
		panelTitle.add(btnBack);
		
		panel = new JPanel();
		panel.setBounds(0, 91, 434, 130);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		lblId = new JLabel("Id");
		lblId.setBounds(0, 0, 132, 65);
		lblId.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblId);
		
		lblPassword = new JLabel("Password");
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword.setBounds(0, 65, 132, 65);
		panel.add(lblPassword);
		
		textFieldId = new JTextField();
		textFieldId.setBounds(130, 0, 304, 65);
		panel.add(textFieldId);
		textFieldId.setColumns(10);
		
		textFieldPassword = new JTextField();
		textFieldPassword.setColumns(10);
		textFieldPassword.setBounds(130, 65, 304, 65);
		panel.add(textFieldPassword);
		
		btnLogin = new JButton("login");
		
		// Login 버튼을 엔터로 클릭하기 위한 코드(작동안됨)
//		btnLogin.addKeyListener(new KeyAdapter() { 
//			@Override
//			public void keyPressed(KeyEvent e) {
//				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//					login();
//                }
//			}
//		});
		
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
		
		btnLogin.setBounds(162, 231, 97, 23);
		frame.getContentPane().add(btnLogin);
	}
	
	private void login() {
		if (textFieldId.getText().equals("adminid") && textFieldPassword.getText().equals("adminpassword")) {
			AdminMain adminMain = new AdminMain();
			adminMain.setVisible(true);
			frame.setVisible(false);
		} else {
			JOptionPane.showMessageDialog(null, "id 또는 비밀번호를 확인해주세요!", "경고", JOptionPane.WARNING_MESSAGE);
		}
	}
}
