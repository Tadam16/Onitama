package onitama;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A modellben a kártyahelyeket reprezentáló objektum.
 *
 */
public class CardSlot implements Serializable {
	/**
	 * A kártyahely által tárolt kártya.
	 */
	private Card card;
	/**
	 * A kártyahelyet figyelő listenerek listája. Miután tranziens változóról van szó mindig meg kell vizsgálnunk, hogy null-e.
	 */
	private transient ArrayList<CardSlotListener> listeners = null;
	/**
	 * Megadja, hogy ki van-e választva a slot.
	 */
	private boolean selected = false;
	
	/**
	 * Megadja, hogy az adott kártyahely ki van-e választva.
	 * @return Igaz, ha ki van választva, egyébként hamis.
	 */
	public boolean isSelected() {
		return selected;
	}
	
	/**
	 * Kiválasztja a kártyahelyet, vagy törli a kiválasztást.
	 * @param s Igaz érték eseteén kiválaszt, hamis érték esetén töröl.
	 */
	public void select(boolean s) {
		selected = s;
		change();
	}
	
	/**
	 * Visszaadja a tárolt kártyát.
	 * @return A tárolt kártya.
	 */
	public Card getCard() {
		return card;
	}
	
	/**
	 * Beállítja a tárolt kártyát.
	 * @param c A kártya, amit el szeretnénk tárolni.
	 */
	public void setCard(Card c) {
		card = c;
		change();
	}
	
	/**
	 * Egy paraméterként kapott listenert hozzáad a tárolt listenerek listájához.
	 * @param l A listener, ami fel szeretne iratkozni az objektumra.
	 */
	public void addCardSlotListener(CardSlotListener l) {
		if(listeners == null) //Ha még nincs listener lista, csinálunk.
			listeners = new ArrayList<CardSlotListener>();
		listeners.add(l); //listener hozzáadása a tárolóhoz.
		change(); //Jelzés a listenereknek a belső állapotról.
	}
	
	/**
	 * Belső változáskor hívandó függvény, amely értesíti a listenereket. Mindig a változtatások végrehajtása után hívandó!
	 */
	private void change() {
		if(listeners == null) //Ha nincs listener nem csinálunk semmit.
			return;
		for(CardSlotListener l : listeners) //Ha van értesítjük őket a lehetséges változásról.
			l.change(this);
	}
}
