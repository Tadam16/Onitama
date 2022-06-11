package onitama;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JButton;

/**
 * A programban a kártyaslotok, és kártyák kirajzolásáért felelős osztály.
 *
 */
public class CardButton extends JButton implements CardSlotListener {
	
	/**
	 * A slotban megjelenített kártya adatai.
	 */
	private Card card = null;
	/**
	 * A slot fejjel fel-, vagy lefelé van. 1 esetén felfelé, -1 esetén lefelé.
	 */
	private int dir;
	
	/**
	 * Az objektum konstruktora.
	 * @param direction Megadja, hogy a kártya fejjel felfelé, vagy lefelé legyen, értéke ennek alapján csak pontosan 1, vagy -1 lehet.
	 */
	public CardButton(int direction) {
		dir = direction;
	}
	
	/**
	 * Megadja a megjelenített kártya nevét.
	 */
	@Override
	public String getName() {
		if(card != null) //Ha van tárolt kártya, a nevének megadása.
			return card.getName();
		return null;
	}

	/**
	 * A felügyelt kártyaslot adatainak módosulása esetén frissíti, újrarajzolja az adatait.
	 */
	@Override
	public void change(CardSlot c) {
		if(c.isSelected()) { //Háttérszín módosítása kiválasztottság alapján.
			super.setBackground(Color.cyan);
		}
		else
			super.setBackground(Color.white);
		card = c.getCard(); //Tárolt kártya átállítása.
		super.repaint(); //Megjelenítés frissítése.
		super.fireStateChanged(); //ChangeListenerek értesítése a változásról.
	}
	
	/**
	 * A felügyelt kártyaslot tartalmazott kártyájának kirajzolásálért felelős függvény.
	 * @param g Az objektum, amire ki akarjuk rajzolni a kártyát.
	 */
	private void drawCard(Graphics g) {
		int w = super.getPreferredSize().width, h = super.getPreferredSize().height; //Slot méreteinek lekérése.
		super.getGraphics().clearRect(0, 0, w, h); //Slotról a korábban rajzolt elemek letörlése.
		if(card != null) { //Ha a slot nem üres:
			g.setColor(Color.gray); //Középső mező szürkére rajzolása.
			g.fillRect(2*w/5, 2*h/5, w/5, h/5);
			g.setColor(Color.lightGray); //Többi mező világosszürkére rajzolása, ha lehet rájuk lépni a tárolt kártya alapján.
			for(Position p : card.getPossiblePositions(new Position(2,2), dir)) {
				g.fillRect(p.x*w/5, p.y*h/5, w/5, h/5);
			}
			g.setColor(Color.black);
			for(int x = 1; x <= 4; x++) { //Függőleges elválasztó vonalak behúzása.
				g.drawLine(w/5*x, 0, w/5*x, h);
			}
			for(int y = 1; y <= 4; y++) { //Vízszintes elválasztó vonalak behúzása.
				g.drawLine( 0, h/5*y, w, h/5*y);
			}
		}
	}
	
	/**
	 * A JButton osztály kirajzolófüggvényének felüldefiniálása.
	 * @param g Az objektum, amire rajzolni akarunk.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g); //Eredeti műveletek.
		drawCard(g); //Kártya kirajzolása.
	}

}
