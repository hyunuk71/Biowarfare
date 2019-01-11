package serverThread;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

public class SFrame extends JFrame implements Runnable, CallBack {
	int portNum;

	ArrayList<PrintWriter> toSend;
	ServerSocket ss;
	PrintWriter out;

	JPanel serverPanel;
	JLabel currentIPLabel;
	JTextField currentIP;
	InetAddress ip;
	JButton createServerSocketBtn;
	JTextField portText;
	JLabel portLabel;
	JTextArea serverInfoArea;
	JScrollPane sp;

	ArrayList<SPlayer> sPlayers;

	public SFrame() {
		sPlayers = new ArrayList<>();

		setTitle("Bio warfare Server");
		toSend = new ArrayList<>();

		try {
			ip = InetAddress.getLocalHost();
		} catch (Exception e) {
			System.out.println(e);
		}
		displayServer();
		add(serverPanel);
		this.setSize(700,300);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				if (ss != null)
					try {
						ss.close();
						System.out.println("Closing server");
					} catch (IOException e) {
						serverInfoArea.append(e.getMessage() + "\r\n");
					}
			}

		});
		this.setVisible(true);
	}

	public void createServerSocketFunc() {
		try {
			portNum = Integer.parseInt(portText.getText());
			ss = new ServerSocket(portNum);
			Thread t = new Thread(SFrame.this);
			t.start();

		} catch (IOException ee) {
			serverInfoArea.append("PORT #" + portNum + " is already opened.\r\n");
		}
	}

	public void displayServer() {
		serverPanel = new JPanel();
		serverPanel.setBounds(25, 200, 640, 300);
		serverPanel.setBorder(new TitledBorder(new EtchedBorder(), "server"));
		serverPanel.setLayout(null);
		add(serverPanel);
		currentIPLabel = new JLabel("Current IP: ");
		currentIP = new JTextField(String.valueOf(ip.getHostAddress()));
		currentIP.setEditable(false);
		portLabel = new JLabel("Enter the PORT number (1024-49151): ");
		portText = new JTextField("8888");
		currentIPLabel.setBounds(30, 30, 80, 25);
		currentIP.setBounds(110, 30, 100, 25);
		portLabel.setBounds(30, 60, 250, 25);
		portText.setBounds(280, 60, 50, 25);

		serverInfoArea = new JTextArea();
		serverInfoArea.setEditable(false);
		serverInfoArea.append("Please let the client player know your IP address and Port number.\r\n");
		sp = new JScrollPane(serverInfoArea);
		sp.setBounds(30, 150, 500, 100);

		createServerSocketBtn = new JButton("Create a Socket");
		createServerSocketBtn.setBounds(30, 100, 150, 40);
		createServerSocketBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createServerSocketFunc();
			}
		});

		serverPanel.add(currentIPLabel);
		serverPanel.add(currentIP);
		serverPanel.add(portLabel);
		serverPanel.add(portText);
		serverPanel.add(createServerSocketBtn);
		serverPanel.add(sp);
	}

	@Override
	public void run() {
		if (ss.isBound()) {
			serverInfoArea.append("ServerSocket #" + portNum + " is bound. " + (new Date()).toString() + "\r\n");
		}

		Socket connectionSock;
		boolean proceed = true;
		int userID = 0;
		Thread t;
		CHandler ch;

		while(proceed) {
			try {
				connectionSock = ss.accept();
				ch = new CHandler(connectionSock, userID, this);

				out = new PrintWriter(connectionSock.getOutputStream(), true);
				out.println("Welcome client # " + userID);
				userID++;
				toSend.add(out);
				//out은 한개. userID로 구분하면 된다.

				t = new Thread(ch);
				t.start();
			} catch (IOException e) {
				proceed = false;
			}
		}
	}

	@Override
	public void executeCallBack(String user, String msg) {
		int intUser = Integer.parseInt(user);

		if (msg == null) { // when user is quit.
			serverInfoArea.append(user + " is quit. Thread ending.\r\n");
			out.println(user + " is quit. Thread end.");
		}

		if (msg.contains("IDhighkrs12345")){ // when user joined.
			String name = msg.replaceAll("IDhighkrs12345", "");
			SPlayer player = new SPlayer(intUser, name);
			sPlayers.add(player);
			toSend.get(intUser).println(name + "," + intUser);
		}

		if (msg.equals("ready")) {
			sPlayers.get(intUser).isReady = true;
			broadCast("Player " + sPlayers.get(intUser).name + " is ready.");
			if (sPlayers.get(0).isReady && sPlayers.get(1).isReady) {
				broadCast("startGame");
				broadCast("0name:" + sPlayers.get(0).name + ":1name:" + sPlayers.get(1).name);
				// Start the game
			}
		}

		if (msg.startsWith("[ACTION]")) {
			if (intUser == 0) {
				toSend.get(1).println(msg);
			} else if (intUser == 1){
				toSend.get(0).println(msg);
			}
		}

		if (msg.startsWith("[CHAT]")) {
			broadCast(msg + ":" + sPlayers.get(intUser).name);
		}
	}

	public void broadCast(String str) {
		for (int i = 0; i < toSend.size(); i++) {
			toSend.get(i).println(str);
		}
	}

}
