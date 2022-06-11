package onitama;

/**
 * Az adatmodell a controller számára nyújtott kritikus függvényeinek interfésze.
 * (Mezők, és kártyahelyek kiválasztásának kezelése, belső adatok frissítése az interakciók alapján.)
 */
public interface ModelInterface {
	/**
	 * Kártyahelyek típusait tároló enumeráció.
	 */
	public enum CardType{reserve, player1A, player1B, player2A, player2B};
	/**
	 * Mező kiválasztásakor hívandó függvény.
	 * @param p A kiválasztott mező pozíciója.
	 */
	public void selectField(Position p);
	/**
	 * Kártyahely kiválasztásakor hívandó függvény.
	 * @param type A kiválasztott kártyahely típusa.
	 */
	public void selectCard(ModelInterface.CardType type);
}
