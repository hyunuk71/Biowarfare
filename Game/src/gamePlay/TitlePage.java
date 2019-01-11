package gamePlay;

import clientThread.CReceiveThread;
import clientThread.CSendThread;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by hyunuk71@gmail.com on 22/07/2018
 * Github : http://github.com/hyunuk71
 */
public class TitlePage extends JPanel {
	ImageIcon title;
	JLabel titleLabel;

	JPanel clientPanel = new JPanel();
	JLabel ipInputMsg1;
	JLabel ipInputMsg2;
	JTextField ipInput;
	JTextField portInput;
	JButton createClientSocketBtn;
	Socket clientSocket;
	JTextArea clientInfoArea;
	JScrollPane sp2;

	JPanel bottomPanel = new JPanel();
	JButton gameStartBtn = new JButton();
	JButton cancelBtn;
	JTextField nameField;
	JLabel nameLabel;
	GameFrame gameFrame;
	GamePage gamePage;
	PrintWriter out;

	public TitlePage(GameFrame gameFrame, GamePage gamePage) {
    	this.gameFrame = gameFrame;
    	this.gamePage = gamePage;
		setLayout(null);
		setSize(700, 700);

		displayTitle();
		displayClient();
		displayBottomPanel();
		bottomPanel.setVisible(true);
		add(bottomPanel);
	}

	public void displayClient() {
		clientPanel = new JPanel();
		clientPanel.setBounds(25, 200, 640, 300);
		clientPanel.setBorder(new TitledBorder(new EtchedBorder(), "client"));
		clientPanel.setLayout(null);
		add(clientPanel);
		ipInputMsg1 = new JLabel("Please enter the IP address of the server.");
		ipInputMsg1.setBounds(30, 30, 400, 20);

		ipInputMsg2 = new JLabel("Please enter the PORT number of the server.");
		ipInputMsg2.setBounds(30, 80, 400, 20);

		ipInput = new JTextField();
		ipInput.setEditable(true);
		ipInput.setBounds(30, 50, 200, 25);

		portInput = new JTextField();
		portInput.setEditable(true);
		portInput.setText("8888");
		portInput.setBounds(30, 100, 50, 25);

		clientInfoArea = new JTextArea();
		clientInfoArea.setEditable(false);
		sp2 = new JScrollPane(clientInfoArea);
		sp2.setBounds(30, 180, 500, 100);

		createClientSocketBtn = new JButton("Create a Socket");
		createClientSocketBtn.setBounds(30, 130, 150, 40);
		createClientSocketBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createClientSocketFunc();
			}
		});

		clientPanel.add(ipInputMsg1);
		clientPanel.add(ipInputMsg2);
		clientPanel.add(ipInput);
		clientPanel.add(portInput);
		clientPanel.add(createClientSocketBtn);
		clientPanel.add(sp2);
	}

	public void createClientSocketFunc() {
		String hostname = ipInput.getText();
		int port = Integer.parseInt(portInput.getText());
		try {
			if (clientSocket == null) {
				clientSocket = new Socket(hostname, port);
				CReceiveThread clientReceiveThread = new CReceiveThread(clientSocket, this, gameFrame, gamePage);
				CSendThread clientSendThread = new CSendThread(clientSocket, this, gameFrame);
				clientInfoArea.append("Found server, connected.\r\n");
				nameField.setEditable(false);

				clientReceiveThread.start();
				clientSendThread.start();
			} else {
				clientInfoArea.append("You are already connected.\r\n");
			}
		} catch (Exception e) {
			clientInfoArea.append(e.getMessage() + "\r\n");
		}
	}

	public void displayBottomPanel() {
		bottomPanel.setBounds(0, 500, 700, 200);
		bottomPanel.setLayout(null);

		gameStartBtn.setText("Ready");
		gameStartBtn.setBounds(100, 80, 200, 40);
		bottomPanel.add(gameStartBtn);
		gameStartBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (clientSocket != null) {
					gameStartBtn.setText("Waiting for other player..");
					gameStartBtn.setEnabled(false);
					clientInfoArea.append("Ready for the game. Waiting another player.\r\n");
					out.println("ready");

					cancelBtn.setVisible(true);
				} else {
					clientInfoArea.append("You are not connected yet.\r\n");
				}
			}
		});

		cancelBtn = new JButton("Cancel");
		cancelBtn.setBounds(360, 80, 150, 40);
		bottomPanel.add(cancelBtn);
		cancelBtn.setVisible(false);
		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameStartBtn.setText("Game Start");
				gameStartBtn.setEnabled(true);
				cancelBtn.setVisible(false);
				clientInfoArea.append("Waiting cancelled.\r\n");
			}
		});

		nameField = new JTextField("Player" + (int)(Math.random() * 1000));
		nameField.setBounds(300, 30, 200, 25);
		nameLabel = new JLabel("Enter your name: ");
		nameLabel.setBounds(130, 30, 120, 25);
		bottomPanel.add(nameField);
		bottomPanel.add(nameLabel);
	}


	public void displayTitle() {
		title = new ImageIcon(this.getClass().getResource("/resource/Title.png"));
		titleLabel = new JLabel(title);
		titleLabel.setBounds(0, 0, 700, 180);
		add(titleLabel);
	}

	public JTextField getNameField() {
		return nameField;
	}

	public JTextArea getClientInfoArea() {
		return clientInfoArea;
	}

	public void setCommunication(PrintWriter out) {
		this.out=out;
	}

}
