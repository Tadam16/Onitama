package onitama;

/**
 * A modellben a vármezőket reprezentáló osztály.
 *
 */
public class CastleField extends Field {
	/**
	 * A várat birtokló játékos.
	 */
	private Player owner;
	/**
	 * A játék, akinek szólni kell a vár elfoglalása esetén.
	 */
	private static Game game;
	
	/**
	 * Konstruktor, amely eltárolja, hogy mely játékos váráról van szó.
	 * @param p A játékos akié a vár.
	 */
	public CastleField(Player p) {
		owner = p;
	}
	
	/**
	 * Figura elhelyezésekor hívandó függvény, ha ellenséges király kerül rá, véget vet a játéknak.
	 * @param f A figura, melyet a mezőre akarunk helyezni.
	 */
	@Override
	public void putFigure(Figure f) {
		super.putFigure(f);
		if(f.getName().equals("king") && owner != f.getOwner()) { //Ha ellenséges király lép a várra, játék értesítése a győztesről.
			game.gameOver(f.getOwner());
		}
	}
	
	/**
	 * Beállítja a várakhoz tartozó játékot, melyet szükség esetén tájékoztat a játék végéről, és a győztes játékosról.
	 * @param g A várakhoz tartozó játék.
	 */
	public static void setGame(Game g) {
		game = g;
	}
}