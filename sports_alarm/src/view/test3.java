package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class test3 {
    private JFrame frame;
    private JPanel imagePanel;
    private JButton attachButton;

    public test3() {
        frame = new JFrame("이미지 첨부 예제");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        imagePanel = new JPanel();
        frame.getContentPane().add(imagePanel, BorderLayout.CENTER);
        
        attachButton = new JButton("이미지 첨부");
        attachButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                attachImage();
            }
        });
        frame.getContentPane().add(attachButton, BorderLayout.SOUTH);
        
        frame.setSize(400, 300);
        frame.setVisible(true);
    }

    private void attachImage() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            ImageIcon icon = new ImageIcon(selectedFile.getPath());
            JLabel label = new JLabel(icon);
            imagePanel.removeAll();
            imagePanel.add(label);
            frame.revalidate();
            frame.repaint();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new test3();
            }
        });
    }
}
