package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import controller.ScheduleDao;
import controller.SportsTeamDao;
import model.Schedule;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.util.Calendar;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import java.awt.Font;
import java.awt.FlowLayout;

public class SportsFootball {
    private static final String[] COLUMN_NAMES = { "상대할 팀", "경기 일정" };

	private JFrame frame;
	private JPanel panelCal;
	
	private JPanel panelCalButton;
	private JButton btnPrevMonth;
	private JTextPane txtpnMonth;
	private JButton btnNextMonth;
	protected JPanel panelCalMain;
	
	protected Calendar cal = Calendar.getInstance();
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
	private DefaultTableModel tableModel;
	private JScrollPane scrollPane;
	private JTable table;

	private String selectedTeam;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
				    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); // Swing 디자인을 LookAndFeel 테마로 변경
				    SportsFootball window = new SportsFootball();
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
	public SportsFootball() {
		initialize();
		initializeTable();
//		createCalendar();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 506, 489);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		panelCal = new JPanel();
		panelCal.setBounds(12, 43, 469, 396);
		frame.getContentPane().add(panelCal);
		panelCal.setLayout(null);
		
		panelCalButton = new JPanel();
		panelCalButton.setBounds(0, 0, 469, 37);
		panelCal.add(panelCalButton);
		panelCalButton.setBorder(border);
		
		panelCalMain = new JPanel();
		panelCalMain.setBounds(0, 37, 469, 359);
		panelCal.add(panelCalMain);
		panelCalMain.setLayout(new BorderLayout(0, 0));
		
		scrollPane = new JScrollPane();
		panelCalMain.add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
        table.setFont(new Font("굴림", Font.BOLD, 20));
        tableModel = new DefaultTableModel(null, COLUMN_NAMES);
        table.setModel(tableModel);
		scrollPane.setViewportView(table);

		btnPrevMonth = new JButton("Prev");
		btnPrevMonth.setFont(new Font("굴림", Font.PLAIN, 17));
		btnPrevMonth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cal.add(Calendar.MONTH, -1);
                updateMonthText(); // 월 텍스트 업데이트
                initializeTable();
			}
		});
		panelCalButton.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panelCalButton.add(btnPrevMonth);
		
		btnNextMonth = new JButton("Next");
		btnNextMonth.setFont(new Font("굴림", Font.PLAIN, 17));
		btnNextMonth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cal.add(Calendar.MONTH, 1); // 다음 달로 이동
                updateMonthText(); // 월 텍스트 업데이트
                initializeTable();
            }
        });
        
        txtpnMonth = new JTextPane();
        txtpnMonth.setFont(new Font("굴림", Font.BOLD, 18));
        panelCalButton.add(txtpnMonth);
        panelCalButton.add(btnNextMonth);
        
        // 텍스트 필드에 월 표시
        updateMonthText();

        panelCalButton.add(btnNextMonth);
        
        panelTeam = new JPanel();
        panelTeam.setBounds(12, 10, 384, 33);
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
            	selectedTeam = (String) comboBoxTeam.getSelectedItem();
                initializeTable(); // 팀 선택 시 테이블 초기화
            }
        });
        List<String> teamNames = teamDao.getTeamsByFootball("EPL(영국 축구)");
        COLUMN_TEAMS = teamNames.toArray(new String[0]);
        final DefaultComboBoxModel<String> comboBoxModel = 
                new DefaultComboBoxModel<>(COLUMN_TEAMS);
        comboBoxTeam.setModel(comboBoxModel);
        panelTeam.add(comboBoxTeam);
        // 콤보박스 초기 선택 없음
        comboBoxTeam.setSelectedItem(null);
        
        btnBack = new JButton("Back");
        btnBack.setFont(new Font("굴림", Font.PLAIN, 12));
        btnBack.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		SportsMain sam = new SportsMain();
        		sam.setVisible(true);
				frame.setVisible(false);
        	}
        });
        btnBack.setBounds(396, 10, 85, 33);
        frame.getContentPane().add(btnBack);
	}
	

	public void setVisible(boolean b) {
		frame.setVisible(b);
	}
	
    // 월 텍스트 업데이트 메서드
    private void updateMonthText() {
        int month = cal.get(Calendar.MONTH) + 1; // 1부터 시작하도록 보정
        txtpnMonth.setText(month + "월");
    }
    
	private void initializeTable() {
	    if (selectedTeam != null) { // 팀이 선택된 경우에만 테이블 초기화
        // DAO를 사용해서 DB 테이블에서 검색.
        List<Schedule> schedule = scheduleDao.getOtherTeamAndCreatedDate(selectedTeam);
        resetTable(schedule); // 테이블 리셋.
	    }
    }
	
	private void resetTable(List<Schedule> schedule) {
	    // 테이블 모델 초기화
	    tableModel = new DefaultTableModel(null, COLUMN_NAMES);

	    // 선택된 월을 가져오기
	    int selectedMonth = cal.get(Calendar.MONTH) + 1;

	 // TableCellRenderer 설정
	    table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
	        @Override
	        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

	            // 상대할 팀 열의 인덱스를 확인하여 emblem_path에 해당하는 이미지를 가져옵니다.
	            if (column == 0) {
	                String teamName = (String) value; // 상대할 팀 이름
	                String emblemPath = scheduleDao.getOtherTeamEmblemPath(teamName); // 상대할 팀의 emblem_path
	                if (emblemPath != null && !emblemPath.isEmpty()) {
	                    // ImageIcon 생성
	                    ImageIcon originalIcon = new ImageIcon(emblemPath);
	                    // 이미지 크기 조정
	                    Image scaledImage = originalIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
	                    ImageIcon scaledIcon = new ImageIcon(scaledImage);
	                    // JLabel에 이미지 설정
	                    label.setIcon(scaledIcon);
	                } else {
	                    // emblemPath가 없는 경우 아무 이미지도 설정하지 않습니다.
	                    label.setIcon(null);
	                }
	            } else {
	                // 다른 열은 기본 렌더링을 그대로 사용합니다.
	                label.setIcon(null);
	            }

	            return label;
	        }
	    });


	    // 검색한 내용을 JTable에 보여줌 - JTable의 테이블 모델을 재설정.
	    for (Schedule f : schedule) {
	        // 경기 날짜를 가져와서 월을 비교
	        String date = f.getDate();
	        
	        // "월일" 형식으로 저장되어 있다고 가정하고 처리
	        String[] parts = date.split(" ");
	        if (parts.length == 2) {
	            String[] monthDay = parts[0].split("월");
	            int gameMonth = Integer.parseInt(monthDay[0]);

	            // txtpnMonth의 월과 일치하는 경우에만 테이블에 추가
	            if (gameMonth == selectedMonth) {
	                // DB 테이블에서 검색한 레코드를 JTable에서 사용할 행 데이터로 변환.
	                Object[] row = { f.getOtherTeam(), date };
	                tableModel.addRow(row); // 테이블 모델에 행 데이터를 추가.
	            }
	        } else {
	            System.out.println("잘못된 날짜 형식입니다: " + date);
	        }
	    }
	    
	    table.setModel(tableModel); // JTable의 모델을 다시 세팅.
	    // 테이블의 행 높이 설정
	    table.setRowHeight(80); // 원하는 행 높이 값으로 설정
	}

}