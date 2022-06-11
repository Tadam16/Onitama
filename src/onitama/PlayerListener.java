package onitama;

/**
 * A játékos ezen listenerek felé jelzi, ha megváltozhatott valamely attribútuma.
 *
 */
public interface PlayerListener {
	/**
	 * A játékos változásakor hívott függvéy.
	 * @param p A játékos, ami megváltozhatott.
	 */
	public void change(Player p);
}
