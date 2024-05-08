package view;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import controller.ScheduleDao;
import controller.SportsTeamDao;
import model.Schedule;


public class AdminScheduleCreateFrame extends JFrame {
	ArrayList<String> teamList = new ArrayList<String>(SportsTeamDao.getInstance().getTeamNames());
	int teamSize = teamList.size();
	int otherTeamSize = teamList.size();
	private final String[] COLUMN_TEAM = teamList.toArray(new String[teamSize]);
	private final String[] COLUMN_OTHERTEAM = teamList.toArray(new String[otherTeamSize]);
	
    public interface CreateNotify {
        void notifyCreateSuccess();
    }
    
	private static final long serialVersionUID = 1L;
	
	private ScheduleDao dao = ScheduleDao.getInstance();
    private CreateNotify app;

	private JPanel contentPane;
	
	private Component parent;
	private JLabel lblTeam;
	private JLabel lblSchedule;
	private JLabel lblOtherTeam;
	private JButton btnCancel;
	private JButton btnSave;
	private JComboBox<String> comboBoxTeam;
	private JTextField textFieldDate;
	private JComboBox<String> comboBoxOtherTeam;
	
	private boolean comboBoxTeamSelected = false; // comboBoxTeam이 선택되었는지 여부를 나타내는 플래그
    
	/**
	 * Launch the application.
	 */
	public static void showCreateScheduleFrame(Component parent, CreateNotify app) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminScheduleCreateFrame frame = new AdminScheduleCreateFrame(parent, app);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
    public AdminScheduleCreateFrame(Component parent, CreateNotify app) {
		this.parent = parent;
        this.app = app;

        initialize();
    }

    public void initialize() {
        setTitle("일정 생성");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
		int x = 0;
		int y = 0;
		if (parent != null) {
			x = parent.getX(); // 부모 컴포넌트의 x 좌표
			y = parent.getY(); // 부모 컴포넌트의 y 좌표
		}
		setBounds(x, y, 470, 340);
		
		if (parent == null) {
			setLocationRelativeTo(null); // 화면 중앙에서 JFrame을 띄움.
		}

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblTeam = new JLabel("팀");
		lblTeam.setFont(new Font("굴림", Font.BOLD, 17));
		lblTeam.setBounds(12, 33, 414, 46);
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
		comboBoxTeam.setBounds(90, 33, 336, 46);
		contentPane.add(comboBoxTeam);
		
		lblOtherTeam = new JLabel("상대할 팀");
		lblOtherTeam.setFont(new Font("굴림", Font.BOLD, 17));
		lblOtherTeam.setBounds(12, 103, 77, 46);
		contentPane.add(lblOtherTeam);
		
		comboBoxOtherTeam = new JComboBox<>();
        final DefaultComboBoxModel<String> comboBoxModelOtherTeam = 
                new DefaultComboBoxModel<>(COLUMN_OTHERTEAM);
        comboBoxOtherTeam.setModel(comboBoxModelOtherTeam);
		comboBoxOtherTeam.setFont(new Font("D2Coding", Font.PLAIN, 15));
		comboBoxOtherTeam.setBounds(90, 103, 336, 46);
		contentPane.add(comboBoxOtherTeam);
		
		lblSchedule = new JLabel("일정");
		lblSchedule.setFont(new Font("굴림", Font.BOLD, 17));
		lblSchedule.setBounds(12, 165, 410, 46);
		contentPane.add(lblSchedule);
		
		btnSave = new JButton("작성완료");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createNewSchedule();
			}
		});
		btnSave.setFont(new Font("굴림", Font.BOLD, 17));
		btnSave.setBounds(12, 234, 193, 49);
		contentPane.add(btnSave);
		
		textFieldDate = new JTextField();
		textFieldDate.setFont(new Font("굴림", Font.PLAIN, 15));
		textFieldDate.setColumns(10);
		textFieldDate.setBounds(90, 165, 336, 46);
		contentPane.add(textFieldDate);
		
		btnCancel = new JButton("취소");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("굴림", Font.BOLD, 17));
		btnCancel.setBounds(249, 234, 193, 49);
		contentPane.add(btnCancel);
		
		
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
    
	private void createNewSchedule() {
        // comboBoxTeam이 선택되지 않은 경우
        if (!comboBoxTeamSelected) {
            JOptionPane.showMessageDialog(AdminScheduleCreateFrame.this, "팀을 선택하세요!", "경고", JOptionPane.WARNING_MESSAGE);
            return;
        }
		
		// DAO 메서드를 사용해서 DB 테이블에 insert.
		String team = (String) comboBoxTeam.getSelectedItem();
		String otherTeam = (String) comboBoxOtherTeam.getSelectedItem();
		String date = textFieldDate.getText();
		if (team.equals("") || otherTeam.equals("") || date.equals("")) {
			JOptionPane.showMessageDialog(
					AdminScheduleCreateFrame.this,
					"팀, 상대 팀, 날짜는 반드시 입력해야 합니다!",
					"경고",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		Schedule schedule = new Schedule(0, team, otherTeam, date);
		int result = dao.create(schedule);
		if (result == 1) {
			// TODO: BlogMain 프레임에게 테이블 삽입 성공을 알려줌.
			app.notifyCreateSuccess();
			dispose(); // 현재 창 닫기.
		} else {
			JOptionPane.showMessageDialog(AdminScheduleCreateFrame.this, "INSERT 실패");
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
