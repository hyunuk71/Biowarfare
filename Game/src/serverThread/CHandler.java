package serverThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by hyunuk71@gmail.com on 30/07/2018
 * Github : http://github.com/hyunuk71
 */
public class CHandler implements Runnable {
	private Socket socket;
	private int userID;
	private CallBack cBack;

	public CHandler(Socket socket, int userID, CallBack cBack) {
		this.socket = socket;
		this.userID = userID;
		this.cBack = cBack;
	}

	@Override
	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(socket.getInputStream());
			BufferedReader in = new BufferedReader(isr);

			String msg;
			while(true) {
				msg = in.readLine();
				cBack.executeCallBack(userID + "", msg);
			}
		} catch (IOException e) {
			cBack.executeCallBack(userID + "", e.getMessage());
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				cBack.executeCallBack(userID + "", e.getMessage());
			}
		}
	}
}