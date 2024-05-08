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

public class AdminTeamCreateFrame extends JFrame {
	private static final String[] COLUMN_LEAGUES = { "EPL(영국 축구)", "KBO(한국 프로야구)" };
	
	public interface CreateNotify {
		void notifyCreateSuccess();
	}


	private static final long serialVersionUID = 1L;
	private SportsTeamDao dao = SportsTeamDao.getInstance();
	private CreateNotify app;
	
	private JPanel contentPane;
	
	private Component parent;
	private JLabel lblLeague;
	private JTextArea textEmblem;
	private JLabel lblWriter;
	private JLabel lblTeam;
	private JButton btnCancel;
	private JButton btnSave;
	private JTextField textTeam;
	private JComboBox<String> comboBoxLeague;
	private JButton btnEmblem;
	
	/**
	 * Launch the application.
	 */
	public static void showAdminCreateFrame(Component parent, CreateNotify app) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminTeamCreateFrame frame = new AdminTeamCreateFrame(parent, app);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public AdminTeamCreateFrame(Component parent, CreateNotify app) {
		this.parent = parent;
		this.app = app;
		
		initialize();
	}

	/**
	 * Create the frame.
	 */
	public void initialize() {
		setTitle("새로운 팀 생성");
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
		
		lblLeague = new JLabel("리그");
		lblLeague.setFont(new Font("굴림", Font.BOLD, 17));
		lblLeague.setBounds(12, 33, 414, 46);
		contentPane.add(lblLeague);
		
		comboBoxLeague = new JComboBox<>();
        final DefaultComboBoxModel<String> comboBoxModel = 
                new DefaultComboBoxModel<>(COLUMN_LEAGUES);
        comboBoxLeague.setModel(comboBoxModel);
        comboBoxLeague.setFont(new Font("D2Coding", Font.PLAIN, 15));
		comboBoxLeague.setBounds(80, 33, 346, 46);
		contentPane.add(comboBoxLeague);
		
		lblTeam = new JLabel("팀 이름");
		lblTeam.setFont(new Font("굴림", Font.BOLD, 17));
		lblTeam.setBounds(12, 103, 67, 46);
		contentPane.add(lblTeam);
		
		textTeam = new JTextField();
		textTeam.setFont(new Font("굴림", Font.PLAIN, 15));
		textTeam.setColumns(10);
		textTeam.setBounds(80, 106, 346, 44);
		contentPane.add(textTeam);
		
		lblWriter = new JLabel("로고");
		lblWriter.setFont(new Font("굴림", Font.BOLD, 17));
		lblWriter.setBounds(12, 165, 410, 46);
		contentPane.add(lblWriter);
		
		btnSave = new JButton("작성완료");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createNewTeam();
			}
		});
		btnSave.setFont(new Font("굴림", Font.BOLD, 17));
		btnSave.setBounds(12, 234, 193, 49);
		contentPane.add(btnSave);
	    
	    // 파일 첨부 버튼 추가
	    btnEmblem = new JButton("첨부파일");
	    btnEmblem.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            // 파일 선택 대화상자 열기
	            JFileChooser fileChooser = new JFileChooser();
	            fileChooser.setFileFilter(new FileNameExtensionFilter("이미지 파일", "jpg", "jpeg", "png", "gif")); // 이미지 파일 필터 설정
	            int result = fileChooser.showOpenDialog(AdminTeamCreateFrame.this); // 파일 선택 대화상자를 모달로 열기
	            
	            if (result == JFileChooser.APPROVE_OPTION) { // 사용자가 파일을 선택했을 때
	                File selectedFile = fileChooser.getSelectedFile(); // 선택한 파일 가져오기
	                String emblemPath = selectedFile.getAbsolutePath(); // 선택한 파일의 경로 가져오기
	                
	                // 파일 경로를 emblemPath 변수에 저장
	                textEmblem.setText(emblemPath);
	            }
	        }
	    });
	    btnEmblem.setFont(new Font("굴림", Font.PLAIN, 13));
	    btnEmblem.setBounds(80, 166, 89, 46);
	    contentPane.add(btnEmblem);
		
	    textEmblem = new JTextArea();
	    textEmblem.setLineWrap(true); // 텍스트가 영역을 벗어나면 자동으로 줄 바꿈
	    textEmblem.setWrapStyleWord(true); // 단어 단위로 줄 바꿈
	    textEmblem.setFont(new Font("굴림", Font.PLAIN, 12));
	    textEmblem.setColumns(10);
	    textEmblem.setBounds(174, 165, 252, 46);
	    contentPane.add(textEmblem);
		
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
	}
	
	private void createNewTeam() {
		// DAO 메서드를 사용해서 DB 테이블에 insert.
		String league = (String) comboBoxLeague.getSelectedItem();
		String team = textTeam.getText();
	    String emblemPath = textEmblem.getText(); // textEmblem 텍스트 필드의 내용을 emblemPath 변수에 저장
		if (league.equals("") || team.equals("") || emblemPath.equals("")) {
			JOptionPane.showMessageDialog(
					AdminTeamCreateFrame.this,
					"리그, 팀 이름, 팀 로고는 반드시 입력, 지정해야 합니다.",
					"경고",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		SportsTeam sportsTeam = new SportsTeam(0, league, team, emblemPath);
		int result = dao.create(sportsTeam);
		if (result == 1) {
			// TODO: BlogMain 프레임에게 테이블 삽입 성공을 알려줌.
			app.notifyCreateSuccess();
			dispose(); // 현재 창 닫기.
		} else {
			JOptionPane.showMessageDialog(AdminTeamCreateFrame.this, "INSERT 실패");
		}
	}
	
}
