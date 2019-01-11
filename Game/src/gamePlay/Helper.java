package gamePlay;

import java.util.ArrayList;

/**
 * Created by hyunuk71@gmail.com on 21/07/2018
 * Github : http://github.com/hyunuk71
 */
public class Helper {
	public static ArrayList<Integer> findAvailableCells(int baseCell, ArrayList<Integer> totalCells, ArrayList<Player> players) {
		ArrayList<Integer> targets = findNeighborCells(baseCell, totalCells, 2);
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < players.get(i).cellIndex.size(); j++) {
				if (targets.contains(players.get(i).getCellIndex().get(j))) {
					targets.remove(players.get(i).getCellIndex().get(j));
				}
			}
		}
		return targets;
	}

	public static ArrayList<Integer> findNeighborCells(int baseCell, ArrayList<Integer> targets, int gap){
		Vector2 pos = convertToPosition(baseCell); // convert index value to x, y coordinate
		ArrayList<Integer> cellsInTheGap = new ArrayList<>();
		for (int i = 0; i < targets.size(); i++) {
			if (getDistance(pos, convertToPosition(targets.get(i))) <= gap){
				cellsInTheGap.add(targets.get(i));
			}
		}
		return cellsInTheGap;
	}

	public static int calc_row(int cell) {
		return (int)(cell / GameFrame.LINE_COUNT);
	}

	public static int calc_col(int cell) {
		return (int)(cell % GameFrame.LINE_COUNT);
	}

	public static Vector2 convertToPosition(int cell) {
		return new Vector2(calc_row(cell), calc_col(cell));
	}

	public static int getDistance(int from, int to) {
		return getDistance(convertToPosition(from), convertToPosition(to));
	}

	public static int getDistance(Vector2 pos1, Vector2 pos2) {
		int v0 = pos2.x - pos1.x;
		int v1 = pos2.y - pos1.y;
		return (int) Math.sqrt(v0*v0 + v1*v1);
	}

	public static int distanceFromClickedCell(int baseCell, int cell) {
		int row = (int)(baseCell / GameFrame.LINE_COUNT);
		int col = (int)(baseCell % GameFrame.LINE_COUNT);
		Vector2 basePos = new Vector2(col, row);

		row = (int)(cell / GameFrame.LINE_COUNT);
		col = (int)(cell % GameFrame.LINE_COUNT);
		Vector2 cellPos = new Vector2(col, row);

		return getDistance(basePos, cellPos);
	}

	public static boolean isContinue(ArrayList<Integer> board, ArrayList<Player> players, int currentPlayIndex) {
		Player current = players.get(currentPlayIndex);
		for (int i = 0; i < current.cellIndex.size();i++) {
			if (findAvailableCells(i, board, players).size() > 0) {
				return true;
			}
		}
		return false;
	}
}
