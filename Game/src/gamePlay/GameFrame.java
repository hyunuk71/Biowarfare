package gamePlay;

import clientThread.CPlayer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by hyunuk71@gmail.com on 23/07/2018
 * Github : http://github.com/hyunuk71
 */
public class GameFrame extends JFrame implements Runnable {
	public static final int LINE_COUNT = 7;
	static int selection;
	static int turnCount = 0;
	Thread thread;
	ArrayList<Player> players;
	ArrayList<Integer> board;
	ArrayList<Integer> table_board;
	ArrayList<Integer> availableAttackCells;
	ArrayList<ImageIcon> imgPlayers;
	ImageIcon oIcon = new ImageIcon(this.getClass().getResource("/resource/red.png"));
	ImageIcon xIcon = new ImageIcon(this.getClass().getResource("/resource/blue.png"));
	ImageIcon border = new ImageIcon(this.getClass().getResource("/resource/border.png"));

	PrintWriter out;

	CPlayer myPlayer;
	CPlayer anotherPlayer;
	Player player;

	int currentPlayerIndex;
	TitlePage titlePage;
	GamePage gamePage;
	int step;

	public GameFrame(){
		setTitle("Bio warfare Client");
		resourceLoad();
		if(thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	private void resourceLoad(){
		//playBGM();
		this.table_board = new ArrayList<>();
		this.availableAttackCells = new ArrayList<>();
		this.imgPlayers = new ArrayList<>();
		this.imgPlayers.add(xIcon);
		this.imgPlayers.add(oIcon);

		this.players = new ArrayList<>();
		for (int i = 0; i < 2; i++) {
			player = new Player();
			player.initialize(i);
			this.players.add(player);
		}

		this.board = new ArrayList<>();

		gamePage = new GamePage();
		titlePage = new TitlePage(this, gamePage);
		this.add(titlePage);

		this.setSize(700,700);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);

		reset();

		gamePage.sendBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String msg = gamePage.inputField.getText();
				sendMsg(msg);
				gamePage.inputField.setText("");
			}
		});
		gamePage.inputField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String msg = gamePage.inputField.getText();
				sendMsg(msg);
				gamePage.inputField.setText("");
			}
		});
	}

	public void gameStart() {
		getContentPane().removeAll();
		getContentPane().add(gamePage);
		revalidate();
		repaint();
	}

	private void reset() {
		this.players.get(0).add(0);
		this.players.get(0).add(1);
		this.players.get(0).add(47);
		this.players.get(0).add(48);

		this.players.get(0).changeToServer();
		this.players.get(1).add(5);
		this.players.get(1).add(6);
		this.players.get(1).add(42);
		this.players.get(1).add(43);
		this.players.get(1).changeToClient();

		this.board.clear();
		this.table_board.clear();
		for (int i = 0; i < LINE_COUNT * LINE_COUNT; i++) {
			this.board.add(Integer.MAX_VALUE);
			this.table_board.add(i);
		}
		for (int i = 0; i < 2; i++) { // let record player(i)'s cell indexes to the board
			for (int j = 0; j < this.players.get(i).cellIndex.size(); j++) {
				this.board.set(this.players.get(i).cellIndex.get(j), i);
			}
		}
		this.currentPlayerIndex = 0;
		this.step = 0;
	}

	private void drawBoard(){
		int index = 0;
		for(int y = 0; y < LINE_COUNT; y++) {
			for (int x = 0; x < LINE_COUNT; x++) {
				int selectedIndex = index;
				gamePage.btns[y][x].addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// if SPlayer's index == player index
						if (currentPlayerIndex == myPlayer.userID) {
							onClick(selectedIndex);
							out.println("[ACTION]" + selectedIndex);
						}
					}
				});

				gamePage.btns[y][x].setIcon(null);
				if (this.board.get(index) != Integer.MAX_VALUE) {
					int tempPlayerIndex = this.board.get(index);
					if (tempPlayerIndex == 0) {
						gamePage.btns[y][x].setIcon(oIcon);
					} else if (tempPlayerIndex == 1) {
						gamePage.btns[y][x].setIcon(xIcon);
					}
				}

				if (this.availableAttackCells.contains(index)) {
					gamePage.btns[y][x].setIcon(border);
				}
				index++;

				String whosTurnInfo = "";
				String myName = "";
				String anotherName = "";
				String myCells = "";
				String anotherCells = "";
				int myID = 0;
				int anotherID = 0;
				if (myPlayer != null && anotherPlayer != null) {
					if (currentPlayerIndex != myPlayer.userID) {
						myName = myPlayer.name;
						myID = myPlayer.userID;
						anotherName = anotherPlayer.name;
						anotherID = anotherPlayer.userID;
						whosTurnInfo = "It's " + anotherName + "'s turn";
					} else if (currentPlayerIndex == myPlayer.userID) {
						anotherName = anotherPlayer.name;
						anotherID = anotherPlayer.userID;

						myName = myPlayer.name;
						myID = myPlayer.userID;
						whosTurnInfo = "It's your turn";
					}
				}
				myCells = "You have " + players.get(myID).cellIndex.size() + " cells.";
				anotherCells = anotherName + " has " + players.get(anotherID).cellIndex.size() + " cells.";
				gamePage.currentTurnInfo.setText(whosTurnInfo);
				gamePage.p1CellInfo.setText(myCells);
				gamePage.p2CellInfo.setText(anotherCells);

				gamePage.selectedCellInfo.setText("You selected cell #" + selection + ".");
				gamePage.currentTurnCount.setText("Turn: " + turnCount);
			}
		}
	}

	public void sendMsg(String msg) {
		msg = "[CHAT]" + msg;
		out.println(msg);
	}

	int selectedCell = Integer.MAX_VALUE;
	public void onClick(int cell){
		selection = cell;
		switch (this.step) {
			case 0:
				if (isValidToBeginCell(cell)) { // initial selection & if this cell is mine
					this.selectedCell = cell;
					this.step = 1;
					refreshAvailableCells(this.selectedCell); // store available cell info to availableAttackCells;
				}
				break;

			case 1: // After user have clicked case 0
				if (this.players.get(this.currentPlayerIndex).cellIndex.contains(cell)) {
					this.selectedCell = cell;
					refreshAvailableCells(this.selectedCell);
					break;
				}

				// user cannot select the other player's cell
				if (this.currentPlayerIndex == 0) {
					if (this.players.get(1).cellIndex.contains(cell)) {
						return;
					}
				} else if (this.currentPlayerIndex == 1) {
					if (this.players.get(0).cellIndex.contains(cell)) {
						return;
					}
				}

				this.step = 2;
				onSelectedCellToAttack(cell);
				break;

			case 2:
				this.step = 0;
				break;
		}
	}

	public void onSelectedCellToAttack(int cell) {
		int distance = Helper.distanceFromClickedCell(this.selectedCell, cell);
		if (distance == 1) {
			reproduce(cell);
			endTurn();
		} else if (distance == 2) {
			this.board.set(this.selectedCell, Integer.MAX_VALUE);
			this.players.get(this.currentPlayerIndex).remove(this.selectedCell);
			reproduce(cell);
			endTurn();
		}
	}

	void gameOver(){
		System.out.println("Game over!");
	}

	public void endTurn(){
		if (this.currentPlayerIndex == 0) {
			this.currentPlayerIndex = 1;
		} else {
			this.currentPlayerIndex = 0;
		}

		if (!Helper.isContinue(this.table_board, this.players, this.currentPlayerIndex)){
			gameOver();
			return;
		}

		this.availableAttackCells.clear();
		turnCount++;
	}


	@Override
	public void run() {
		try {
			while(true) {
				drawBoard();
				Thread.sleep(100);
			}
		} catch (Exception e) {}
	}


/*	public void playBGM() {
		File bgm = new File("Projects/Biowarfare/src/resource/sMario.wav");
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(bgm));
			clip.loop(Clip.LOOP_CONTINUOUSLY);
			clip.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/

	public void refreshAvailableCells(int cell){
		this.availableAttackCells = Helper.findAvailableCells(cell, this.table_board, this.players);
	}

	public void clearAvailableAttackCells(){
		this.availableAttackCells.clear();
	}

	public void reproduce(int cell) {
		Player currentPlayer = this.players.get(this.currentPlayerIndex);
		Player otherPlayer = (currentPlayerIndex == 0) ? this.players.get(1) : this.players.get(0);

		clearAvailableAttackCells();

		this.board.set(cell, currentPlayer.playerIndex);
		currentPlayer.add(cell);

		// consume other's
		ArrayList<Integer> neighbors = Helper.findNeighborCells(cell, otherPlayer.cellIndex, 1);
		for (int i = 0; i < neighbors.size(); i++) {
			this.board.set(neighbors.get(i), currentPlayer.playerIndex);
			currentPlayer.add(neighbors.get(i));
			otherPlayer.remove(neighbors.get(i));
		}
	}

	public boolean isValidToBeginCell(int cell) {
		return this.players.get(this.currentPlayerIndex).cellIndex.contains(cell);
	}

	public void setMyPlayer(CPlayer myPlayer) {
		this.myPlayer = myPlayer;
	}

	public void setAnotherPlayer(CPlayer anotherPlayer) {
		this.anotherPlayer = anotherPlayer;
	}

	public void setCommunication(PrintWriter out) {
		this.out=out;
	}
}
