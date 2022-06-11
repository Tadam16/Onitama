package onitama;

import java.io.Serializable;

/**
 * Az adatmodellben a gyalogokat reprezentáló osztály
 *
 */
public class Figure implements Serializable {
	/**
	 * Az adott figurát birtokló játékos.
	 */
	private Player owner;
	/**
	 * Az adott figura neve.
	 */
	protected String name = "pawn";
	
	/**
	 * Konstruktor, amelyben megadható a gyalogot tulajdonló játékos.
	 * @param p A gyalogot tulajdonló játékos.
	 */
	public Figure(Player p) {
		owner = p;
	}
	
	/**
	 * Megadja a gyalogot tulajdonló játékost.
	 * @return a gyalogot tulajdonló játékos.
	 */
	public Player getOwner() {
		return owner;
	}
	
	/**
	 * A bábu leütésekor hívódó függvény.
	 * @param f A figura, amely leütötte a bábut.
	 */
	public void hitBy(Figure f) {}
	
	/**
	 * Megadja a figura nevét, ebben az esetben ez gyalog (pawn).
	 * @return A figura neve.
	 */
	public String getName() {
		return name;
	}
}
