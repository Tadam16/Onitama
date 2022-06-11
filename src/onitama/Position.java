package onitama;

import java.io.Serializable;

/**
 * A játékban a pozíciókat tároló osztály.
 *
 */
public class Position implements Serializable {
	/**
	 * Pozíció x koordinátája.
	 */
	public int x;
	/**
	 * Pozíció y koordinátája.
	 */
	public int y;
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
