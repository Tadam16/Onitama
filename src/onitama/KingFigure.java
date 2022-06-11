package onitama;

/**
 * Az adatmodellben a vezér bábut reprezentáló osztály.
 *
 */
public class KingFigure extends Figure {
	/**
	 * A játék objektum, akinek leütéskor szólni kell.
	 */
	private static Game game;
	
	/**
	 * Konstruktor, melyben megadható a figurát birtokló játékos.
	 * @param p A figurát birtokló játékos.
	 */
	public KingFigure(Player p) {
		super(p);
		name = "king";
	}
	
	/**
	 * A figura leütésekor hívódó függvény, szól a tárolt Game-nek, hogy a játéknak vége.
	 * @param f A vezért leütő figura.
	 */
	@Override
	public void hitBy(Figure f) {
		game.gameOver(f.getOwner());
	}
	
	/**
	 * Beállítja a játékot, akinek leütés esetén szólni kell.
	 * @param g Az értesítendő játék.
	 */
	public static void setGame(Game g) {
		game = g;
	}
}