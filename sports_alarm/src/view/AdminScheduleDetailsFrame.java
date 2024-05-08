package view;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import controller.ScheduleDao;
import controller.SportsTeamDao;
import model.Schedule;
import model.SportsTeam;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;

public class AdminScheduleDetailsFrame extends JFrame {
	ArrayList<String> teamList = new ArrayList<String>(SportsTeamDao.getInstance().getTeamNames());
	int teamSize = teamList.size();
	int otherTeamSize = teamList.size();
	private final String[] COLUMN_TEAM = teamList.toArray(new String[teamSize]);
	private final String[] COLUMN_OTHERTEAM = teamList.toArray(new String[otherTeamSize]);
    
    public interface UpdateNotify {
        public void notifyUpdateSuccess();
    }

    private static final long serialVersionUID = 1L;
    
    private ScheduleDao dao = ScheduleDao.getInstance();
    private Component parent;
    private int teamId;
    private UpdateNotify app;
    
    private JPanel contentPane;
    private JLabel lblId;
    private JTextField textId;
    private JLabel lblTeam;
    private JLabel lblOtherTeam;
    private JButton btnUpdate;
    private JButton btnCancel;
    private JComboBox<String> comboBoxTeam;
    private JLabel lblContentSchedule;
	private JComboBox<String> comboBoxOtherTeam;
	private JTextField textSchedule;
	private JTextField textFieldDate;
	
	private boolean comboBoxTeamSelected = false; // comboBoxTeam이 선택되었는지 여부를 나타내는 플래그

    /**
     * Launch the application.
     */
    public static void showAdminDetailsFrame(Component parent, int teamId, UpdateNotify app) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AdminScheduleDetailsFrame frame = new AdminScheduleDetailsFrame(parent, teamId, app);
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
    public AdminScheduleDetailsFrame(Component parent, int teamId, UpdateNotify app) {
        this.parent = parent;
        this.teamId = teamId;
        this.app = app;
        
        initialize();
        initializeSchedule();
    }
    
    private void initialize() {
        setTitle("팀 상세정보");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        int x = 0;
        int y = 0;
        if (parent != null) {
            x = parent.getX();
            y = parent.getY();
        }
        setBounds(x, y, 513, 351);
        
        if (parent == null) {
            setLocationRelativeTo(null);
        }
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        lblId = new JLabel("일정 생성번호");
        lblId.setFont(new Font("굴림", Font.BOLD, 19));
        lblId.setBounds(12, 10, 129, 48);
        contentPane.add(lblId);
        
        textId = new JTextField();
        textId.setEditable(false);
        textId.setFont(new Font("D2Coding", Font.PLAIN, 28));
        textId.setBounds(145, 10, 277, 48);
        contentPane.add(textId);
        textId.setColumns(10);
        
        lblTeam = new JLabel("팀");
        lblTeam.setFont(new Font("굴림", Font.BOLD, 19));
        lblTeam.setBounds(12, 68, 121, 48);
        contentPane.add(lblTeam);
        
		comboBoxTeam = new JComboBox<>();
		// comboBoxTeam의 선택 이벤트 처리
		comboBoxTeam.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        if (comboBoxTeam.getSelectedIndex() != -1) {
		            comboBoxTeamSelected = true; // comboBoxTeam이 선택되었음을 표시
		            comboBoxOtherTeam.setEnabled(true); // comboBoxOtherTeam 활성화
		            
		            // 선택된 팀의 리그 가져오기
		            String selectedTeam = (String) comboBoxTeam.getSelectedItem();
		            String selectedTeamLeague = SportsTeamDao.getInstance().getTeamLeague(selectedTeam);
		            
		            // 선택된 팀의 리그에 속하는 팀들로 comboBoxOtherTeam을 업데이트
		            updateOtherTeamComboBox(selectedTeamLeague);
		        }
		    }
		});
        final DefaultComboBoxModel<String> comboBoxModelTeam = 
                new DefaultComboBoxModel<>(COLUMN_TEAM);
        comboBoxTeam.setModel(comboBoxModelTeam);
        comboBoxTeam.setFont(new Font("D2Coding", Font.PLAIN, 15));
		comboBoxTeam.setBounds(145, 70, 346, 46);
		contentPane.add(comboBoxTeam);
        
        lblOtherTeam = new JLabel("상대 팀");
        lblOtherTeam.setFont(new Font("굴림", Font.BOLD, 19));
        lblOtherTeam.setBounds(12, 126, 121, 48);
        contentPane.add(lblOtherTeam);
        
        btnUpdate = new JButton("업데이트");
        btnUpdate.addActionListener((e) -> updateTeam());
        btnUpdate.setFont(new Font("굴림", Font.BOLD, 22));
        btnUpdate.setBounds(12, 252, 176, 48);
        contentPane.add(btnUpdate);
        
        btnCancel = new JButton("취소");
        btnCancel.addActionListener((e) -> dispose());
        btnCancel.setFont(new Font("굴림", Font.BOLD, 22));
        btnCancel.setBounds(315, 252, 176, 48);
        contentPane.add(btnCancel);
        
        lblContentSchedule = new JLabel("일정");
        lblContentSchedule.setFont(new Font("굴림", Font.BOLD, 19));
        lblContentSchedule.setBounds(12, 184, 121, 48);
        contentPane.add(lblContentSchedule);
		
		comboBoxOtherTeam = new JComboBox<>();
        final DefaultComboBoxModel<String> comboBoxModelOtherTeam = 
                new DefaultComboBoxModel<>(COLUMN_OTHERTEAM);
        comboBoxOtherTeam.setModel(comboBoxModelOtherTeam);
		comboBoxOtherTeam.setFont(new Font("D2Coding", Font.PLAIN, 15));
		comboBoxOtherTeam.setBounds(145, 126, 346, 46);
		contentPane.add(comboBoxOtherTeam);
		
		textSchedule = new JTextField();
		textSchedule.setText((String) null);
		textSchedule.setFont(new Font("D2Coding", Font.PLAIN, 20));
		textSchedule.setColumns(10);
		textSchedule.setBounds(145, 184, 346, 48);
		contentPane.add(textSchedule);
		
        // comboBoxTeam의 선택 이벤트 처리
        comboBoxTeam.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (comboBoxTeam.getSelectedIndex() != -1) {
                    comboBoxTeamSelected = true; // comboBoxTeam이 선택되었음을 표시
                    comboBoxOtherTeam.setEnabled(true); // comboBoxOtherTeam 활성화
                }
            }
        });

        // 초기에 comboBoxOtherTeam를 비활성화
        comboBoxOtherTeam.setEnabled(false);
    }
    
    private void initializeSchedule() {
        Schedule schedule = dao.read(teamId);
        if (schedule == null) return;
        
        textId.setText(teamId + "");
        // 팀의 리그 정보를 콤보박스에 설정
        comboBoxTeam.setSelectedItem(schedule.getTeam());
        comboBoxOtherTeam.setSelectedItem(schedule.getOtherTeam());
        textSchedule.setText(schedule.getDate());
    }
    
    private void updateTeam() {
        // comboBoxTeam이 선택되지 않은 경우
        if (!comboBoxTeamSelected) {
            JOptionPane.showMessageDialog(AdminScheduleDetailsFrame.this, "팀을 선택하세요!", "경고", JOptionPane.WARNING_MESSAGE);
            return;
        }
    	
		String team = (String) comboBoxTeam.getSelectedItem();
		String otherTeam = (String) comboBoxOtherTeam.getSelectedItem();
		String date = textFieldDate.getText();
        if (team.equals("") || otherTeam.equals("") || date.equals("")) {
            JOptionPane.showMessageDialog(this, 
                    "팀, 상대 팀, 일정은 반드시 입력, 지정해야 합니다!", 
                    "경고", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
		Schedule schedule = new Schedule(0, team, otherTeam, date);
        int result = dao.update(schedule);
        if (result == 1) {
            app.notifyUpdateSuccess();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "업데이트 실패");
        }
    }
    
	// comboBoxOtherTeam을 선택된 팀의 리그에 속하는 팀들로 업데이트하는 메서드
	private void updateOtherTeamComboBox(String selectedTeamLeague) {
		ArrayList<String> otherTeamList = new ArrayList<>();
		String selectedTeam = (String) comboBoxTeam.getSelectedItem(); // 선택된 팀
		for (String team : teamList) {
			if (!team.equals(selectedTeam)) { // 선택된 팀이 아닌 경우에만 추가
				String teamLeague = SportsTeamDao.getInstance().getTeamLeague(team);
				if (teamLeague.equals(selectedTeamLeague)) {
					otherTeamList.add(team);
				}
			}
		}
		// comboBoxOtherTeam의 모델 업데이트
		DefaultComboBoxModel<String> otherTeamComboBoxModel = new DefaultComboBoxModel<>(
				otherTeamList.toArray(new String[0]));
		comboBoxOtherTeam.setModel(otherTeamComboBoxModel);
	}
}