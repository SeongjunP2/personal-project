package view;

import java.awt.*;

import java.awt.event.*;

import javax.swing.AbstractAction;

import javax.swing.JComponent;

import javax.swing.JFrame;

import javax.swing.JRootPane;

import javax.swing.KeyStroke;

import javax.swing.UIManager;

public class StartSystemTray extends JFrame {

	final String MY_FUNC_KEY = "key";

	TrayIcon trayIcon;

	SystemTray tray;

	StartSystemTray() {

		super("시스템트레이 테스트");

		System.out.println("인스턴스 생성");

		try {

			System.out.println("look and feel 세팅");

			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		} catch (Exception e) {

			System.out.println("look and feel 실패");

		}

		if (SystemTray.isSupported()) {

			System.out.println("시스템트레이에 추가");

			tray = SystemTray.getSystemTray();

			Image image = Toolkit.getDefaultToolkit().getImage("c://penguine.png");

			ActionListener exitListener = new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					System.out.println("끝내기");

					System.exit(0);

				}

			};

			final JRootPane ROOT_PANE = this.getRootPane();

			ROOT_PANE.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)

					.put(KeyStroke.getKeyStroke((char) KeyEvent.VK_U, InputEvent.CTRL_DOWN_MASK), MY_FUNC_KEY);

			ROOT_PANE.getActionMap().put(MY_FUNC_KEY, new AbstractAction() {

				@Override

				public void actionPerformed(ActionEvent e) {

					System.out.println("되라");

				}

			});

			PopupMenu popup = new PopupMenu();

			MenuItem defaultItem = new MenuItem("Exit");

			defaultItem.addActionListener(exitListener);

			popup.add(defaultItem);

			defaultItem = new MenuItem("Open");

			defaultItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					setVisible(true);

					setExtendedState(JFrame.NORMAL);

				}

			});

			popup.add(defaultItem);

			trayIcon = new TrayIcon(image, "일정 알람 프로그램", popup);

			trayIcon.setImageAutoSize(true);

		} else {

			System.out.println("시스템트레이 접근 실패");

		}

		addWindowStateListener(new WindowStateListener() {

			public void windowStateChanged(WindowEvent e) {

				if (e.getNewState() == ICONIFIED) {

					try {

						tray.add(trayIcon);

						setVisible(false);

						System.out.println("시스템트레이에 들어감");

					} catch (AWTException ex) {

						System.out.println("시스템트레이 접근 실패");

					}

				}

				if (e.getNewState() == 7) {

					try {

						tray.add(trayIcon);

						setVisible(false);

						System.out.println("시스템트레이에 들어감");

					} catch (AWTException ex) {

						System.out.println("시스템트레이 접근 실패");

					}

				}

				if (e.getNewState() == MAXIMIZED_BOTH) {

					tray.remove(trayIcon);

					setVisible(true);

					System.out.println("최대화");

				}

				if (e.getNewState() == NORMAL) {

					tray.remove(trayIcon);

					setVisible(true);

					System.out.println("돌아오기");

				}

			}

		});

		setIconImage(Toolkit.getDefaultToolkit().getImage("penguin.png"));

		setVisible(true);

		setSize(300, 300);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public static void main(String[] args) {

		new StartSystemTray();

	}

}
