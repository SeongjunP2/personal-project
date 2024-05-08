package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import controller.ScheduleDao;
import controller.SportsTeamDao;
import model.Schedule;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.Calendar;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.Font;
import javax.swing.SwingConstants;

public class SportsAlarmFootballbck {

	private JFrame frame;
	private JPanel panelCal;
	
	private JPanel panelCalButton;
	private JButton btnPrevMonth;
	private JTextPane txtpnMonth;
	private JButton btnNextMonth;
	protected JPanel panelCalMain;
	
	protected Calendar cal = Calendar.getInstance();
	private JPanel panelContent;
	private JTextField textDate;
	private JPanel panelTeam;
	private JComboBox<String> comboBoxTeam;
	private JLabel lblNewLabel;
	
	protected String[] week = {"일", "월", "화", "수", "목", "금", "토"};
	private JButton btnBack;
	
    private SportsTeamDao teamDao = SportsTeamDao.getInstance();
    private ScheduleDao scheduleDao = ScheduleDao.getInstance();
    private String[] COLUMN_TEAMS;
	
	//파라미터: 색상, 선 두께, border의 모서리를 둥글게 할 것인지
	private LineBorder border = new LineBorder(Color.black, 1, true); 
	private JButton btnSchedule;
	private JScrollPane scrollPane;
	private JTable tableSchedule;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
				    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); // Swing 디자인을 LookAndFeel 테마로 변경
					SportsAlarmFootballbck window = new SportsAlarmFootballbck();
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
	public SportsAlarmFootballbck() {
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
		panelCal.setLayout(null);
		
		panelCalButton = new JPanel();
		panelCalButton.setBounds(0, 0, 606, 37);
		panelCal.add(panelCalButton);
		panelCalButton.setBorder(border);
		
		panelCalMain = new JPanel();
		panelCalMain.setBounds(0, 37, 398, 359);
		panelCal.add(panelCalMain);
		panelCalMain.setLayout(new GridLayout(0, 7, 0, 0)); // panelCalMain에 7만큼만 출력
		
		btnPrevMonth = new JButton("Prev");
		btnPrevMonth.setFont(new Font("굴림", Font.PLAIN, 14));
		btnPrevMonth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cal.add(Calendar.MONTH, -1);
				createCalendar();
			}
		});
		panelCalButton.add(btnPrevMonth);
		
		btnNextMonth = new JButton("Next");
		btnNextMonth.setFont(new Font("굴림", Font.PLAIN, 14));
		btnNextMonth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cal.add(Calendar.MONTH, 1); // 다음 달로 이동
                createCalendar();
            }
        });
        
        txtpnMonth = new JTextPane();
        txtpnMonth.setFont(new Font("굴림", Font.PLAIN, 14));
        txtpnMonth.setText(Integer.toString(cal.get(Calendar.MONTH) + 1));
        panelCalButton.add(txtpnMonth);
        panelCalButton.add(btnNextMonth);
        
        panelContent = new JPanel();
        panelContent.setBounds(398, 37, 208, 359);
        panelCal.add(panelContent);
        panelContent.setBorder(border);
        panelContent.setLayout(new BorderLayout()); // panelContent의 레이아웃을 BorderLayout으로 설정

        textDate = new JTextField();
        textDate.setHorizontalAlignment(SwingConstants.CENTER);
        textDate.setFont(new Font("굴림", Font.PLAIN, 13));
        panelContent.add(textDate, BorderLayout.NORTH); // textDate를 BorderLayout의 NORTH에 배치

        btnSchedule = new JButton("알람 추가");
        btnSchedule.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 일정 작성 창 띄우기
//                AddScheduleFrame.ShowAddScheduleFrame(frame, SportsAlarmFootball.this);
            }
        });
        btnSchedule.setFont(new Font("Gulim", Font.PLAIN, 12));
        panelContent.add(btnSchedule, BorderLayout.SOUTH); // btnSchedule를 BorderLayout의 SOUTH에 배치
        
        scrollPane = new JScrollPane();
        scrollPane.setBounds(6, 38, 196, 311);
        panelContent.add(scrollPane);
        
        tableSchedule = new JTable();
        scrollPane.setViewportView(tableSchedule);
        
        panelTeam = new JPanel();
        panelTeam.setBounds(12, 10, 519, 33);
        frame.getContentPane().add(panelTeam);
        
        lblNewLabel = new JLabel("팀선택");
        lblNewLabel.setFont(new Font("굴림", Font.BOLD, 18));
        panelTeam.add(lblNewLabel);
        
        comboBoxTeam = new JComboBox<>();
        comboBoxTeam.setFont(new Font("굴림", Font.PLAIN, 16));
        // comboBoxTeam의 선택 이벤트 처리
        comboBoxTeam.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 선택된 팀 가져오기
                String selectedTeam = (String) comboBoxTeam.getSelectedItem();
                
                // 선택된 팀의 일정 가져오기
                List<Schedule> teamSchedules = scheduleDao.getTeamSchedules(selectedTeam);
                
                // 가져온 일정의 날짜와 버튼의 년, 월, 일 값을 비교하여 버튼 색상 변경
                for (Schedule schedule : teamSchedules) {
                    int scheduleYear = schedule.getYear();
                    int scheduleMonth = schedule.getMonth();
                    int scheduleDay = schedule.getDay();
                    
                    // 모든 버튼을 가져와서 해당하는 년, 월, 일 값과 비교
                    Component[] components = panelCalMain.getComponents();
                    for (Component component : components) {
                        if (component instanceof JButton) {
                            JButton btn = (JButton) component;
                            int btnYear = (int) btn.getClientProperty("year");
                            int btnMonth = (int) btn.getClientProperty("month");
                            int btnDay = (int) btn.getClientProperty("day");
                            
                            // 버튼의 년, 월, 일 값과 일정의 날짜가 일치하는 경우 버튼 색상 변경
                            if (btnYear == scheduleYear && btnMonth == scheduleMonth && btnDay == scheduleDay) {
                                btn.setBackground(Color.RED); // 예시로 색상을 빨간색으로 변경
                            }
                        }
                    }
                }
            }
        });
        List<String> teamNames = teamDao.getTeamsByFootball("EPL(영국 축구)");
        COLUMN_TEAMS = teamNames.toArray(new String[0]);
        final DefaultComboBoxModel<String> comboBoxModel = 
                new DefaultComboBoxModel<>(COLUMN_TEAMS);
        comboBoxTeam.setModel(comboBoxModel);
        panelTeam.add(comboBoxTeam);
        
        btnBack = new JButton("Back");
        btnBack.setFont(new Font("굴림", Font.PLAIN, 12));
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
            dayLabel.setBorder(border);
            dayLabel.setFont(new Font("굴림", Font.BOLD, 17));
            dayLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panelCalMain.add(dayLabel); // 요일 표시를 위한 레이블 추가
        }

        int startDay = cal.get(Calendar.DAY_OF_WEEK); // 첫째 날의 요일
        int monthDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH); // 해당 달의 총 일 수

        for (int i = 1; i < startDay; i++) {
            panelCalMain.add(new JLabel("")); // 첫째 날 이전의 빈 레이블 추가
        }

        for (int i = 1; i <= monthDay; i++) {
            JButton btnDay = new JButton(Integer.toString(i));
            // 각 버튼의 년, 월, 일 값을 저장
            btnDay.putClientProperty("year", year);
            btnDay.putClientProperty("month", month);
            btnDay.putClientProperty("day", i);
            
            // 버튼 클릭 이벤트 처리
            btnDay.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // 선택된 날짜 정보를 텍스트 필드에 표시
                    textDate.setText(
                        year + "년 " + 
                        (month + 1) + "월 " + 
                        btnDay.getText() + "일 ");
                }
            });
            panelCalMain.add(btnDay);
        }
        
        txtpnMonth.setText(Integer.toString(month + 1) + "월"); // 현재 월 갱신

        frame.revalidate();
        frame.repaint();
    }
}