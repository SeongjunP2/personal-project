package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import controller.SportsTeamDao;
import model.SportsTeam;
import view.AdminTeamCreateFrame.CreateNotify;
import view.AdminTeamDetailsFrame.UpdateNotify;

import java.awt.Font;
import java.awt.Image;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AdminTeamEdit implements CreateNotify, UpdateNotify {
    private static final String[] SEARCH_TYPES  = { 
            "리그", "팀 이름", "리그+팀 이름" 
    };
    private static final String[] COLUMN_NAMES = {
            "번호", "리그", "팀 이름", "로고"
    };

    private JFrame frame;
    private JPanel searchPanel;
    private JComboBox<String> comboBox;
    private JTextField textSearchKeyword;
    private JButton btnSearch;
    private JPanel buttonPanel;
    private JButton btnReadAll;
    private JButton btnCreate;
    private JButton btnDetails;
    private JButton btnDelete;
    private JScrollPane scrollPane;
    private JTable table;
    private DefaultTableModel tableModel;
    
    private SportsTeamDao dao = SportsTeamDao.getInstance();
    private JButton btnBack;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
				    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); // Swing 디자인을 LookAndFeel 테마로 변경
                	AdminTeamEdit window = new AdminTeamEdit();
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
    public AdminTeamEdit() {
        initialize();
        initializeTable();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 482, 552);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("스포츠 팀 편집");
        
        searchPanel = new JPanel();
        frame.getContentPane().add(searchPanel, BorderLayout.SOUTH);
        
        comboBox = new JComboBox<>();
        final DefaultComboBoxModel<String> comboBoxModel = 
                new DefaultComboBoxModel<>(SEARCH_TYPES);
        
        btnReadAll = new JButton("새로고침");
        searchPanel.add(btnReadAll);
        btnReadAll.addActionListener((e) -> initializeTable());
        btnReadAll.setFont(new Font("굴림", Font.BOLD, 18));
        comboBox.setModel(comboBoxModel);
        comboBox.setFont(new Font("굴림", Font.PLAIN, 15));
        searchPanel.add(comboBox);
        
        textSearchKeyword = new JTextField();
        textSearchKeyword.setFont(new Font("D2Coding", Font.PLAIN, 18));
        searchPanel.add(textSearchKeyword);
        textSearchKeyword.setColumns(10);
        
        btnSearch = new JButton("검색");
        btnSearch.addActionListener((e) -> search());
        btnSearch.setFont(new Font("굴림", Font.BOLD, 18));
        searchPanel.add(btnSearch);
        
        buttonPanel = new JPanel();
        frame.getContentPane().add(buttonPanel, BorderLayout.NORTH);
        
        btnCreate = new JButton("팀 생성");
        btnCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 새 팀 작성 창 띄우기
                AdminTeamCreateFrame.showAdminCreateFrame(frame, AdminTeamEdit.this);
            }
        });
        btnCreate.setFont(new Font("굴림", Font.BOLD, 18));
        buttonPanel.add(btnCreate);
        
        btnDetails = new JButton("상세보기");
        btnDetails.addActionListener((e) -> showDetailsFrame());
        btnDetails.setFont(new Font("굴림", Font.BOLD, 18));
        buttonPanel.add(btnDetails);
        
        btnDelete = new JButton("삭제");
        btnDelete.addActionListener((e) -> deleteTeam());
        btnDelete.setFont(new Font("굴림", Font.BOLD, 18));
        buttonPanel.add(btnDelete);
        
        btnBack = new JButton("Back");
        btnBack.setFont(new Font("굴림", Font.PLAIN, 12));
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
        		AdminMain adm = new AdminMain();
        		adm.setVisible(true);
				frame.setVisible(false);
			}
		});
        buttonPanel.add(btnBack);
        
        scrollPane = new JScrollPane();
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        table = new JTable();
        table.setFont(new Font("굴림", Font.PLAIN, 12));
        tableModel = new DefaultTableModel(null, COLUMN_NAMES);
        table.setModel(tableModel);
        scrollPane.setViewportView(table);
    }
    
    private void showDetailsFrame() {
        int index = table.getSelectedRow(); // 테이블에서 선택된 행의 인덱스
        if (index == -1) { // JTable에서 선택된 행이 없을 때
            JOptionPane.showMessageDialog(
                    frame, 
                    "상세보기할 행을 먼저 선택하세요.", 
                    "경고", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        Integer id = (Integer) tableModel.getValueAt(index, 0);
        
        AdminTeamDetailsFrame.showAdminDetailsFrame(frame, id, AdminTeamEdit.this);
        
    }

	private void search() {
		int type = comboBox.getSelectedIndex(); // 콤보박스에서 선택된 아이템의 인덱스
		String keyword = textSearchKeyword.getText(); // 검색어
		if (keyword.equals("")) {
			JOptionPane.showMessageDialog(frame, "검색어를 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
			textSearchKeyword.requestFocus(); //-> 검색어 입력 JTextField에 포커스를 줌(커서 깜박깜박).
			// 검색어를 입력하지 않은 채로 검색버튼 클릭할 시, 경고 팝업 띄우고 JTextField에 커서 포커스 부여
			
			return;
		}
		
		// DAO 메서드를 호출해서 검색 결과를 가져옴.
		List<SportsTeam> sportsTeams = dao.search(type, keyword);
		resetTable(sportsTeams); // 테이블 리셋.
	}

	private void initializeTable() {
        // DAO를 사용해서 DB 테이블에서 검색.
        List<SportsTeam> sportsTeams = dao.read();
        resetTable(sportsTeams); // 테이블 리셋.
        
    }
	
	private void resetTable(List<SportsTeam> sportsTeams) {
		// 검색한 내용을 JTable에 보여줌 - JTable의 테이블 모델을 재설정.
		tableModel = new DefaultTableModel(null, COLUMN_NAMES); // 테이블 모델 리셋.
		for (SportsTeam f : sportsTeams) {
			// DB 테이블에서 검색한 레코드를 JTable에서 사용할 행 데이터로 변환.
			Object[] row = { f.getId(), f.getLeague(), f.getTeam(),
					// 이미지 파일을 ImageIcon으로 변환하여 삽입
					new ImageIcon(f.getEmblemPath()) // 이미지 파일 경로를 ImageIcon으로 변환
			};
			tableModel.addRow(row); // 테이블 모델에 행 데이터를 추가.
		}
		table.setModel(tableModel); // JTable의 모델을 다시 세팅.
		// 테이블의 행 높이 설정
		table.setRowHeight(80); // 원하는 행 높이 값으로 설정
        
        // 이미지를 표시하기 위한 TableCellRenderer 정의
        table.getColumnModel().getColumn(3).setCellRenderer(new ImageIconCellRenderer());
	}
	
    private void deleteTeam() {
		int index = table.getSelectedRow(); // 테이블에서 선택된 행의 인덱스
		if (index == -1) { // JTable에서 선택된 행이 없을때
			JOptionPane.showMessageDialog(
					frame, 
					"삭제할 행을 선택하세요.", 
					"경고", 
					JOptionPane.WARNING_MESSAGE);
			return;
			
		}
		
		int confirm = JOptionPane.showConfirmDialog(
				frame, 
				"정말 삭제할까요?", 
				"삭제 확인",
				JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			// 선택된 행에서 블로그 번호(id)를 읽음
			Integer id = (Integer) tableModel.getValueAt(index, 0);
			// DAO의 delete 메서드 호출.
			int result = dao.delete(id);
			if (result == 1) {
				initializeTable(); // 테이블을 새로고침
				JOptionPane.showMessageDialog(frame, "삭제되었습니다!");
			} else {
				JOptionPane.showMessageDialog(frame, "삭제 실패!");
			}
		}
	}

	@Override
	public void notifyCreateSuccess() {
		// 테이블에 insert 성공했을 때 BlogCreateFrame에 호출하는 메서드.
		initializeTable();
		JOptionPane.showMessageDialog(frame, "새 팀 등록이 등록되었습니다!");
		
	}
	
    @Override
    public void notifyUpdateSuccess() {
        // 테이블에 update 성공했을 때 BlogDetailsFrame이 호출하는 메서드.
        initializeTable();
        JOptionPane.showMessageDialog(frame, "팀 정보가 수정되었습니다!");
    }
    
	public void setVisible(boolean b) {
		frame.setVisible(b);
	}
	
	// TableCellRenderer를 상속받은 ImageIconCellRenderer 클래스 정의
	class ImageIconCellRenderer extends DefaultTableCellRenderer {
	    @Override
	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	        JLabel label = new JLabel();
	        // 이미지 아이콘을 레이블에 설정
	        label.setIcon((ImageIcon) value);
	        // 이미지 크기를 조절하여 레이블에 설정
	        Image image = ((ImageIcon) value).getImage();
	        Image scaledImage = image.getScaledInstance(table.getRowHeight(), table.getRowHeight(), Image.SCALE_SMOOTH);
	        label.setIcon(new ImageIcon(scaledImage));
	        // 레이블을 중앙 정렬
	        label.setHorizontalAlignment(JLabel.CENTER);
	        return label;
	    }
	}
    
}