package gamePlay;

import java.util.ArrayList;

/**
 * Created by hyunuk71@gmail.com on 21/07/2018
 * Github : http://github.com/hyunuk71
 */
public class Player {
	public enum PLAYER_STATE {
		CLIENT, SERVER
	}

	public ArrayList<Integer> cellIndex;
	public int playerIndex;
	public PLAYER_STATE state;

	public Player() {
		this.cellIndex = new ArrayList<>();
	}

	public ArrayList<Integer> getCellIndex() {
		return cellIndex;
	}

	public PLAYER_STATE getState() {
		return state;
	}

	public int getPlayerIndex() {
		return playerIndex;
	}

	public void setCellIndex(ArrayList<Integer> cellIndex) {
		this.cellIndex = cellIndex;
	}

	public void setPlayerIndex(int playerIndex) {
		this.playerIndex = playerIndex;
	}

	public void setState(PLAYER_STATE state) {
		this.state = state;
	}

	public void awake() {
		this.cellIndex = new ArrayList<>();
	}

	public void clear() {
		this.cellIndex.removeAll(cellIndex);
	}

	public void initialize(int playerIndex){
		this.playerIndex = playerIndex;
	}

	public void add(int cell) {
		if (this.cellIndex.contains(cell)) {
			System.out.println("Occupied in " + cell);
			return;
		}
		this.cellIndex.add(cell);
	}

	public void remove(int cell) {
		this.cellIndex.remove(cellIndex.indexOf(cell));
	}

	public void changeToClient() {
		this.state = PLAYER_STATE.CLIENT;
	}

	public void changeToServer() {
		this.state = PLAYER_STATE.SERVER;
	}

}
