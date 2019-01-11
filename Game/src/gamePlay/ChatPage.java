package gamePlay;

import javax.swing.*;
import java.awt.*;

/**
 * Created by hyunuk71@gmail.com on 31/07/2018
 * Github : http://github.com/hyunuk71
 */
public class ChatPage extends JPanel {
	private JScrollPane sp;
	private JTextArea displayChat;
	private JTextField inputField;
	private JButton sendBtn;
	JPanel chatPanel;

	public ChatPage() {
		chatPanel = new JPanel();
		chatPanel.setLayout(null);
		chatPanel.setSize(700, 200);
		chatPanel.setLocation(0, 500);

		inputField = new JTextField();
		attach(inputField,0,175,300,25);

		sendBtn = new JButton("Send");
		attach(sendBtn,300,175,80,25);

		displayChat = new JTextArea();
		sp = new JScrollPane(displayChat);
		attach(sp,0,0,700,200);

		add(chatPanel);
	}

	private void attach(Component c, int x, int y, int w, int h) {
		chatPanel.add(c);
		c.setBounds(x,y,w,h);
	}
}