package onitama;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A nézetben megjelenített mezőkre történő kattintásokat regisztrálja, majd delegálja a fő Controller osztálynak.
 *
 */
public class FieldActionListener implements ActionListener {

	/**
	 * A controller akinek változáskor szólni kell.
	 */
	private static Controller c;
	/**
	 * A pozíció, amit figyelünk.
	 */
	private Position p;
	
	/**
	 * Konstruktor, amelyben megadható a mezőhöz tartozó pozíció.
	 * @param p A mezőhöz tartozó pozíció.
	 */
	public FieldActionListener(Position p) {
		this.p = p;
	}
	
	/**
	 * Metódus, amellyel beállítható az objektumhoz tartozó kontroller.
	 * @param c A kontroller amit be szeretnénk állítani.
	 */
	public static void setController(Controller c) {
		FieldActionListener.c = c;
	}
	
	/**
	 * A mezőre való kattintáskor hívódó függvény.
	 * @param arg0 A kattintáshoz tartozó információkat tároló osztály.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		c.FieldAction(p);
	}
	
}
