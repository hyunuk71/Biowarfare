package gamePlay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by hyunuk71@gmail.com on 23/07/2018
 * Github : http://github.com/hyunuk71
 */
public class GamePage extends JPanel {
	JPanel gamePanel;
	JPanel infoPanel;
	JPanel timerPanel;

	JPanel chatPanel;
	JScrollPane sp;
	JTextArea displayChat;
	JTextField inputField;
	JButton sendBtn;

	JLabel currentTurnInfo;
	JLabel currentTurnCount;
	JLabel p1CellInfo;
	JLabel p2CellInfo;
	JLabel selectedCellInfo;
	JButton btns[][];

	public GamePage() {
		gamePanel = new JPanel();

		btns = new JButton[GameFrame.LINE_COUNT][GameFrame.LINE_COUNT];
		setLayout(null);

		gamePanel.setLayout(new GridLayout(GameFrame.LINE_COUNT,GameFrame.LINE_COUNT));
		for(int y = 0; y < GameFrame.LINE_COUNT; y++){
			for (int x = 0; x < GameFrame.LINE_COUNT; x++) {
				btns[y][x] = new JButton();
				gamePanel.add(btns[y][x]);
			}
		}

		gamePanel.setSize(500, 500);
		gamePanel.setLocation(0, 0);
		add(gamePanel);

		infoPanel = new JPanel();
		infoPanel.setLayout(null);
		infoPanel.setBounds(500, 0, 200, 500);
		currentTurnInfo = new JLabel();
		currentTurnInfo.setBounds(10, 100, 200, 25);
		currentTurnCount = new JLabel();
		currentTurnCount.setBounds(10, 120, 200, 25);
		infoPanel.add(currentTurnInfo);
		infoPanel.add(currentTurnCount);

		p1CellInfo = new JLabel();
		p2CellInfo = new JLabel();
		p1CellInfo.setBounds(0, 150, 200, 25);
		p2CellInfo.setBounds(0, 180, 200, 25);
		infoPanel.add(p1CellInfo);
		infoPanel.add(p2CellInfo);
		selectedCellInfo = new JLabel();
		selectedCellInfo.setBounds(0, 210, 200, 25);
		infoPanel.add(selectedCellInfo);
		add(infoPanel);

		chatPanel = new JPanel();
		chatPanel.setLayout(null);
		chatPanel.setBounds(0, 500, 700, 200);
		inputField = new JTextField();
		inputField.setBounds(0, 140, 500, 25);
		sendBtn = new JButton("Send");
		sendBtn.setBounds(500,140,80,25);

		displayChat = new JTextArea();
		displayChat.setEditable(false);
		sp = new JScrollPane(displayChat);
		sp.setBounds(0, 0, 650, 140);

		chatPanel.add(inputField);
		chatPanel.add(sendBtn);
		chatPanel.add(sp);

		add(chatPanel);
	}

	public JTextArea getDisplayChat() {
		return displayChat;
	}
}