package view;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import controller.SportsTeamDao;
import model.SportsTeam;

public class AdminDetailsFrame extends JFrame {
    
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
    private JTextField textTitle;
    private JLabel lblContent;
    private JButton btnUpdate;
    private JButton btnCancel;
    private JTextField textField;
    private JTextField textField_1;

    /**
     * Launch the application.
     */
    public static void showAdminDetailsFrame(Component parent, int teamId, UpdateNotify app) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AdminDetailsFrame frame = new AdminDetailsFrame(parent, teamId, app);
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
    public AdminDetailsFrame(Component parent, int teamId, UpdateNotify app) {
        this.parent = parent;
        this.teamId = teamId;
        this.app = app;
        
        initialize();
        initializeBlog();
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
        setBounds(x, y, 448, 351);
        
        if (parent == null) {
            setLocationRelativeTo(null);
        }
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        lblId = new JLabel("팀 생성번호");
        lblId.setFont(new Font("D2Coding", Font.BOLD, 20));
        lblId.setBounds(12, 10, 121, 48);
        contentPane.add(lblId);
        
        textId = new JTextField();
        textId.setEditable(false);
        textId.setFont(new Font("D2Coding", Font.PLAIN, 28));
        textId.setBounds(145, 10, 277, 48);
        contentPane.add(textId);
        textId.setColumns(10);
        
        lblTitle = new JLabel("리그");
        lblTitle.setFont(new Font("D2Coding", Font.BOLD, 20));
        lblTitle.setBounds(12, 68, 121, 48);
        contentPane.add(lblTitle);
        
        textTitle = new JTextField();
        textTitle.setFont(new Font("D2Coding", Font.PLAIN, 20));
        textTitle.setColumns(10);
        textTitle.setBounds(145, 68, 277, 48);
        contentPane.add(textTitle);
        
        lblContent = new JLabel("팀 이름");
        lblContent.setFont(new Font("D2Coding", Font.BOLD, 20));
        lblContent.setBounds(12, 126, 121, 48);
        contentPane.add(lblContent);
        
        btnUpdate = new JButton("업데이트");
        btnUpdate.addActionListener((e) -> updateTeam());
        
        textField = new JTextField();
        textField.setText((String) null);
        textField.setFont(new Font("D2Coding", Font.PLAIN, 20));
        textField.setColumns(10);
        textField.setBounds(145, 126, 277, 48);
        contentPane.add(textField);
        btnUpdate.setFont(new Font("D2Coding", Font.PLAIN, 28));
        btnUpdate.setBounds(12, 252, 176, 48);
        contentPane.add(btnUpdate);
        
        btnCancel = new JButton("취소");
        btnCancel.addActionListener((e) -> dispose());
        btnCancel.setFont(new Font("D2Coding", Font.PLAIN, 28));
        btnCancel.setBounds(246, 252, 176, 48);
        contentPane.add(btnCancel);
        
        JLabel lblContent_1 = new JLabel("팀 로고");
        lblContent_1.setFont(new Font("D2Coding", Font.BOLD, 20));
        lblContent_1.setBounds(12, 184, 121, 48);
        contentPane.add(lblContent_1);
        
        textField_1 = new JTextField();
        textField_1.setText((String) null);
        textField_1.setFont(new Font("D2Coding", Font.PLAIN, 20));
        textField_1.setColumns(10);
        textField_1.setBounds(145, 184, 277, 48);
        contentPane.add(textField_1);
    }
    
    private void initializeBlog() {
        SportsTeam footballTeam = dao.read(teamId);
        if (footballTeam == null) return;
        
        textId.setText(teamId + "");
        textTitle.setText(footballTeam.getLeague());
    }
    
    private void updateTeam() {
        String league = textTitle.getText();
        String team = textTitle.getText();
        String emblemPath = textTitle.getText();
        if (league.equals("") || team.equals("") || emblemPath.equals("")) {
            JOptionPane.showMessageDialog(this, 
                    "리그, 팀 이름, 팀 로고는 반드시 입력, 지정해야 합니다.", 
                    "경고", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        SportsTeam footballTeam = new SportsTeam(teamId, league, team, emblemPath);
        int result = dao.update(footballTeam);
        if (result == 1) {
            app.notifyUpdateSuccess();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "업데이트 실패");
        }
    }
}