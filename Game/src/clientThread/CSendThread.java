package clientThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import gamePlay.*;

public class CSendThread extends Thread {
	Socket clientSocket;
	String userName;
	TitlePage titlePage;
	BufferedReader in;
	PrintWriter out;
	GameFrame gameFrame;

	public CSendThread(Socket clientSocket, TitlePage titlePage, GameFrame gameFrame) {
		this.clientSocket = clientSocket;
		this.titlePage = titlePage;
		this.gameFrame = gameFrame;
	}

	public void run() {
		super.run();
		try {
			in = new BufferedReader(new InputStreamReader(System.in));
			out = new PrintWriter(clientSocket.getOutputStream(), true);

			this.titlePage.setCommunication(out);
			this.gameFrame.setCommunication(out);
			String sendString;
			while (true) {
				if (clientSocket.isConnected()) {
					userName = titlePage.getNameField().getText();
					out.println("IDhighkrs12345" + userName);
				}
				sendString = in.readLine();
				out.println(sendString);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}