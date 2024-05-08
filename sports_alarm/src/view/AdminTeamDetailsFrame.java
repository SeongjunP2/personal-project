package view;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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

import controller.SportsTeamDao;
import model.SportsTeam;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;

public class AdminTeamDetailsFrame extends JFrame {
	private static final String[] COLUMN_LEAGUES = { "EPL(영국 축구)", "KBO(한국 프로야구)" };
    
    public interface UpdateNotify {
        public void notifyUpdateSuccess();
    }

    private static final long serialVersionUID = 1L;
    
    private SportsTeamDao dao = SportsTeamDao.getInstance();
    private Component parent;
    private int teamId;
    private UpdateNotify app;
    
    private JPanel contentPane;
    private JLabel lblId;
    private JTextField textId;
    private JLabel lblTitle;
    private JLabel lblContentTeam;
    private JButton btnUpdate;
    private JButton btnCancel;
    private JTextField textTeam;
    private JComboBox<String> comboBoxLeague;
    private JLabel lblContentEmblem;
	private JButton btnEmblem;
	private JTextArea textEmblem;

    /**
     * Launch the application.
     */
    public static void showAdminDetailsFrame(Component parent, int teamId, UpdateNotify app) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AdminTeamDetailsFrame frame = new AdminTeamDetailsFrame(parent, teamId, app);
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
    public AdminTeamDetailsFrame(Component parent, int teamId, UpdateNotify app) {
        this.parent = parent;
        this.teamId = teamId;
        this.app = app;
        
        initialize();
        initializeTeam();
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
        
        lblId = new JLabel("팀 생성번호");
        lblId.setFont(new Font("굴림", Font.BOLD, 19));
        lblId.setBounds(12, 10, 121, 48);
        contentPane.add(lblId);
        
        textId = new JTextField();
        textId.setEditable(false);
        textId.setFont(new Font("D2Coding", Font.PLAIN, 28));
        textId.setBounds(145, 10, 277, 48);
        contentPane.add(textId);
        textId.setColumns(10);
        
        lblTitle = new JLabel("리그");
        lblTitle.setFont(new Font("굴림", Font.BOLD, 19));
        lblTitle.setBounds(12, 68, 121, 48);
        contentPane.add(lblTitle);
        
		comboBoxLeague = new JComboBox<>();
        final DefaultComboBoxModel<String> comboBoxModel = 
                new DefaultComboBoxModel<>(COLUMN_LEAGUES);
        comboBoxLeague.setModel(comboBoxModel);
        comboBoxLeague.setFont(new Font("D2Coding", Font.PLAIN, 15));
		comboBoxLeague.setBounds(145, 70, 346, 46);
		contentPane.add(comboBoxLeague);
        
        lblContentTeam = new JLabel("팀 이름");
        lblContentTeam.setFont(new Font("굴림", Font.BOLD, 19));
        lblContentTeam.setBounds(12, 126, 121, 48);
        contentPane.add(lblContentTeam);
        
        btnUpdate = new JButton("업데이트");
        btnUpdate.addActionListener((e) -> updateTeam());
        btnUpdate.setFont(new Font("굴림", Font.BOLD, 22));
        btnUpdate.setBounds(12, 252, 176, 48);
        contentPane.add(btnUpdate);
        
        textTeam = new JTextField();
        textTeam.setText((String) null);
        textTeam.setFont(new Font("굴림", Font.PLAIN, 18));
        textTeam.setColumns(10);
        textTeam.setBounds(145, 126, 346, 48);
        contentPane.add(textTeam);
        
        btnCancel = new JButton("취소");
        btnCancel.addActionListener((e) -> dispose());
        btnCancel.setFont(new Font("굴림", Font.BOLD, 22));
        btnCancel.setBounds(315, 252, 176, 48);
        contentPane.add(btnCancel);
        
        lblContentEmblem = new JLabel("팀 로고");
        lblContentEmblem.setFont(new Font("굴림", Font.BOLD, 19));
        lblContentEmblem.setBounds(12, 184, 121, 48);
        contentPane.add(lblContentEmblem);
        
		// 파일 첨부 버튼 추가
		btnEmblem = new JButton("첨부파일");
		btnEmblem.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        // 파일 선택 대화상자 열기
		        JFileChooser fileChooser = new JFileChooser();
		        fileChooser.setFileFilter(new FileNameExtensionFilter("이미지 파일", "jpg", "jpeg", "png", "gif")); // 이미지 파일 필터 설정
		        int result = fileChooser.showOpenDialog(AdminTeamDetailsFrame.this); // 파일 선택 대화상자를 모달로 열기
		        
		        if (result == JFileChooser.APPROVE_OPTION) { // 사용자가 파일을 선택했을 때
		            File selectedFile = fileChooser.getSelectedFile(); // 선택한 파일 가져오기
		            String emblemPath = selectedFile.getAbsolutePath(); // 선택한 파일의 경로 가져오기
		            
		            // 파일 경로를 emblemPath 변수에 저장
		            textEmblem.setText(emblemPath);
		        }
		    }
		});
		btnEmblem.setFont(new Font("굴림", Font.PLAIN, 13));
		btnEmblem.setBounds(145, 184, 89, 46);
		contentPane.add(btnEmblem);
		
		// 첨부파일 경로를 표시하는 텍스트 필드를 초기화하고 추가합니다.
		textEmblem = new JTextArea();
		textEmblem.setLineWrap(true); // 텍스트가 영역을 벗어나면 자동으로 줄 바꿈
		textEmblem.setWrapStyleWord(true); // 단어 단위로 줄 바꿈
		textEmblem.setFont(new Font("굴림", Font.PLAIN, 12));
		textEmblem.setColumns(10);
		textEmblem.setBounds(239, 186, 252, 46);
		contentPane.add(textEmblem);
    }
    
    private void initializeTeam() {
        SportsTeam sportsTeams = dao.read(teamId);
        if (sportsTeams == null) return;
        
        textId.setText(teamId + "");
        // 팀의 리그 정보를 콤보박스에 설정
        comboBoxLeague.setSelectedItem(sportsTeams.getLeague());
        textTeam.setText(sportsTeams.getTeam());
    }
    
    private void updateTeam() {
		// DAO 메서드를 사용해서 DB 테이블에 insert.
		String league = (String) comboBoxLeague.getSelectedItem();
		String team = textTeam.getText();
	    String emblemPath = textEmblem.getText(); // textEmblem 텍스트 필드의 내용을 emblemPath 변수에 저장
        if (league.equals("") || team.equals("") || emblemPath.equals("")) {
            JOptionPane.showMessageDialog(this, 
                    "리그, 팀 이름, 팀 로고는 반드시 입력, 지정해야 합니다.", 
                    "경고", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        SportsTeam sportsTeams = new SportsTeam(teamId, league, team, emblemPath);
        int result = dao.update(sportsTeams);
        if (result == 1) {
            app.notifyUpdateSuccess();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "업데이트 실패");
        }
    }
}