package clientThread;

import gamePlay.GameFrame;
import gamePlay.GamePage;
import gamePlay.TitlePage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class CReceiveThread extends Thread {
	Socket clientSocket;
	TitlePage titlePage;
	GameFrame gameFrame;
	CPlayer myPlayer;
	CPlayer anotherPlayer;
	GamePage gamePage;

	public CReceiveThread(Socket clientSocket, TitlePage titlePage, GameFrame gameFrame, GamePage gamePage) {
		this.clientSocket = clientSocket;
		this.titlePage = titlePage;
		this.gameFrame = gameFrame;
		this.gamePage = gamePage;
	}

	public void run() {
		super.run();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String receiveString;
			while (true) {
				receiveString = in.readLine();
				System.out.println(receiveString);
				titlePage.getClientInfoArea().append(receiveString + "\n");

				if (receiveString.equals("startGame")) {
					gameFrame.gameStart();
				}

				// user is able to know its id(myID) and name.
				if (receiveString.contains(titlePage.getNameField().getText()) && receiveString.contains(",")) {
					String temp[] = receiveString.split(",");
					String name = temp[0];
					int myID = Integer.parseInt(temp[1]);
					System.out.println(myID);
					myPlayer = new CPlayer(myID, name);
				}

				if (receiveString.contains("0name")) {
					String temp[] = receiveString.split(":");
					String name = temp[1];
					if (!name.equals(myPlayer.name)) {
						anotherPlayer = new CPlayer(0, name);
					} else {
						anotherPlayer = new CPlayer(1, temp[3]);
					}
				}
				this.gameFrame.setMyPlayer(myPlayer);
				this.gameFrame.setAnotherPlayer(anotherPlayer);

				if (receiveString.startsWith("[ACTION]")) {
					int selectedIndex = Integer.parseInt(receiveString.substring(8));
					this.gameFrame.onClick(selectedIndex);
				}

				if (receiveString.startsWith("[CHAT]")) {
					String temp[] = receiveString.split(":");
					String name = temp[1];
					String msg = temp[0].substring(6);
					gamePage.getDisplayChat().append("[" + name + "] " + msg + "\r\n");

				}
			}
		} catch (IOException e){
		}
	}
}
