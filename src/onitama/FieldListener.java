package onitama;

/**
 * Az adatmodell mezőinek változáskor ilyen interfészeken keresztül értesítik a hozzájuk regisztrált Listenereket.
 *
 */
public interface FieldListener {
	/**
	 * Változáskor hívódó függvény
	 * @param f A mező, ami változott.
	 */
	public void change(Field f);
}
