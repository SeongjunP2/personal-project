package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class AdminLogin {

	private JFrame frame;
	private JPanel panelTitle;
	private JLabel lblTitle;
	private JButton btnBack;
	private JPanel panel;
	private JLabel lblId;
	private JLabel lblPassword;
	private JTextField textFieldId;
	private JButton btnLogin;
	
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
				    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); // Swing 디자인을 LookAndFeel 테마로 변경
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
		
		lblTitle = new JLabel("관리자 로그인");
		lblTitle.setFont(new Font("굴림", Font.BOLD, 15));
		lblTitle.setBounds(0, 0, panelTitle.getWidth(), panelTitle.getHeight());
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setVerticalAlignment(SwingConstants.CENTER);
		panelTitle.add(lblTitle);
		
		btnBack = new JButton("Back");
		btnBack.setFont(new Font("Gulim", Font.PLAIN, 12));
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
        		SportsMain sam = new SportsMain();
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
		lblId.setFont(new Font("굴림", Font.PLAIN, 14));
		lblId.setBounds(0, 0, 132, 65);
		lblId.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblId);
		
		lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("굴림", Font.PLAIN, 14));
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword.setBounds(0, 65, 132, 65);
		panel.add(lblPassword);
		
		textFieldId = new JTextField();
		textFieldId.setFont(new Font("Gulim", Font.PLAIN, 12));
		textFieldId.setBounds(130, 0, 304, 65);
		panel.add(textFieldId);
		textFieldId.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Gulim", Font.PLAIN, 12));
		passwordField.addActionListener(new ActionListener() { // textFieldPassword(비밀번호 입력하는 텍스트필드)에서 enter키 입력하면 로그인 작동
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
		passwordField.setBounds(130, 65, 304, 65);
		panel.add(passwordField);
		
		btnLogin = new JButton("login");
		
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
		
		btnLogin.setBounds(162, 231, 97, 23);
		frame.getContentPane().add(btnLogin);
	}
	
	private void login() {
		if (textFieldId.getText().equals("adminid") && passwordField.getText().equals("adminpassword")) {
			AdminMain adminMain = new AdminMain();
			adminMain.setVisible(true);
			frame.setVisible(false);
		} else {
			JOptionPane.showMessageDialog(null, "id 또는 비밀번호를 확인해주세요!", "경고", JOptionPane.WARNING_MESSAGE);
		}
	}
}
