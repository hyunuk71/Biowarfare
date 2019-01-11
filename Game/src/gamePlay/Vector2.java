package gamePlay;

/**
 * Created by hyunuk71@gmail.com on 23/07/2018
 * Github : http://github.com/hyunuk71
 */
public class Vector2 {
	public int x;
	public int y;

	public Vector2() {
		this.x = 0;
		this.y = 0;
	}

	public Vector2(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public boolean equals(Vector2 other) {
		return (this.x == other.x && this.y == other.y);
	}

}
