package onitama;

/**
 * A kártyahelyeken történő módosításokat ilyen interfészeken keresztül jelzi az adatmodell.
 *
 */
public interface CardSlotListener {
	/**
	 * Belső adatok változásakor hívandó függvény.
	 * @param c A kártyahely, ami változott.
	 */
	public void change(CardSlot c);
}
