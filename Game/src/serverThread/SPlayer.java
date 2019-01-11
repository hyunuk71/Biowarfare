package serverThread;

/**
 * Created by hyunuk71@gmail.com on 30/07/2018
 * Github : http://github.com/hyunuk71
 */
public class SPlayer {
	public int userID;
	public String name;
	public boolean isReady;

	public SPlayer(int userID, String name){
		this.userID = userID;
		this.name = name;
		isReady = false;
	}
}
