package onitama;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A megjelenítésben, a kártyahelyekre való kattintásokat kezelő osztály.
 */
public class CardSlotActionListener implements ActionListener {
	/**
	 * A kontroller, akinek a hívást delegálni kell.
	 */
	private static Controller c;
	/**
	 * A slottípus, amit a listener képvisel.
	 */
	private ModelInterface.CardType card;
	
	/**
	 * Metódus, amivel beállítható a játékmenetet figyelő Controller osztály.
	 * @param c Az objektum, amit be szeretnénk állítani.
	 */
	public static void setController(Controller c) {
		CardSlotActionListener.c = c;
	}
	
	/**
	 * Konstruktor, amely inicializálja, hogy melyik kártyahelyről van szó.
	 * @param card Az adott kártyahely.
	 */
	public CardSlotActionListener(ModelInterface.CardType card) {
		this.card = card;
	}
	
	/**
	 * Kattintáskor történő interakció a Controller osztállyal.
	 * @param arg0 Az akcióval kapcsolatos információkat tartalmazó osztály.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		c.CardAction(card);
	}
	
}
