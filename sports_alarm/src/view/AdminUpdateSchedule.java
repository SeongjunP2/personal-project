package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextPane;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Calendar;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JComboBox;

public class AdminUpdateSchedule {

	private JFrame frame;
	private JPanel panelCal;
	
	private JPanel panelCalButton;
	private JButton btnPrevMonth;
	private JTextPane txtpnMonth;
	private JButton btnNextMonth;
	protected JPanel panelCalMain;
	
	protected Calendar cal = Calendar.getInstance();
	private JPanel panel;
	private JTextField textDate;
	private JPanel panel_1;
	private JComboBox comboBoxTeam;
	private JLabel lblNewLabel;
	
	protected String[] week = {"일", "월", "화", "수", "목", "금", "토"};
	private JButton btnBack;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminUpdateSchedule window = new AdminUpdateSchedule();
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
	public AdminUpdateSchedule() {
		initialize();
		createCalendar();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 646, 489);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		panelCal = new JPanel();
		panelCal.setBounds(12, 43, 606, 396);
		frame.getContentPane().add(panelCal);
		panelCal.setLayout(new BorderLayout(0, 0));
		
		panelCalButton = new JPanel();
		panelCal.add(panelCalButton, BorderLayout.NORTH);
		
		panelCalMain = new JPanel();
		panelCal.add(panelCalMain, BorderLayout.CENTER);
		panelCalMain.setLayout(new GridLayout(0, 7, 0, 0)); // panelCalMain에 7만큼만 출력
		
		btnPrevMonth = new JButton("Prev");
		btnPrevMonth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cal.add(Calendar.MONTH, -1);
				createCalendar();
			}
		});
		panelCalButton.add(btnPrevMonth);
		
		btnNextMonth = new JButton("Next");
		btnNextMonth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cal.add(Calendar.MONTH, 1); // 다음 달로 이동
                createCalendar();
            }
        });
        
        txtpnMonth = new JTextPane();
        txtpnMonth.setText(Integer.toString(cal.get(Calendar.MONTH) + 1));
        panelCalButton.add(txtpnMonth);
        panelCalButton.add(btnNextMonth);
        
        panel = new JPanel();
        panelCal.add(panel, BorderLayout.EAST);
        
        textDate = new JTextField();
        panel.add(textDate);
        textDate.setColumns(10);
        
        panel_1 = new JPanel();
        panel_1.setBounds(12, 10, 519, 33);
        frame.getContentPane().add(panel_1);
        
        lblNewLabel = new JLabel("Team");
        panel_1.add(lblNewLabel);
        
        comboBoxTeam = new JComboBox();
        panel_1.add(comboBoxTeam);
        
        btnBack = new JButton("Back");
        btnBack.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		SportsAlarmMain sam = new SportsAlarmMain();
        		sam.setVisible(true);
				frame.setVisible(false);
        	}
        });
        btnBack.setBounds(533, 10, 85, 33);
        frame.getContentPane().add(btnBack);
	}

	public void setVisible(boolean b) {
		frame.setVisible(b);
	}
	
    private void createCalendar() {
        panelCalMain.removeAll(); // 이전에 생성된 캘린더 버튼들을 제거
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        cal.set(Calendar.DAY_OF_MONTH, 1); // 달의 첫째 날로 설정

        for (String day : week) {
            JLabel dayLabel = new JLabel(day);
            panelCalMain.add(dayLabel); // 요일 표시를 위한 레이블 추가
        }

        int startDay = cal.get(Calendar.DAY_OF_WEEK); // 첫째 날의 요일
        int monthDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH); // 해당 달의 총 일 수

        for (int i = 1; i < startDay; i++) {
            panelCalMain.add(new JLabel("")); // 첫째 날 이전의 빈 레이블 추가
        }

        for (int i = 1; i <= monthDay; i++) {
            JButton dayButton = new JButton(Integer.toString(i));
            dayButton.addActionListener(new ActionListener() {
            	public void actionPerformed(ActionEvent e) {
            		textDate.setText(
            				year + "년 " + 
            				(month + 1) + "월 " + 
            				dayButton.getText() + "일 ");
            	}
            });
            panelCalMain.add(dayButton);
        }
        
        txtpnMonth.setText(Integer.toString(month + 1) + "월"); // 현재 월 갱신

        frame.revalidate();
        frame.repaint();
    }
}
