package onitama;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Az adatmodellben a mezőket reprezentáló osztály.
 *
 */
public class Field implements Serializable {
	/**
	 * Megadja, hogy az adott mező ki van-e választva.
	 */
	private boolean selected = false;
	/**
	 * Megadja, hogy az adott mezőre lehetséges-e lépés.
	 */
	private boolean possible = false;
	/**
	 * A mezőn álló figura.
	 */
	private Figure figure = null;
	/**
	 * A mező változásait figyelő listenerek. Miután tranziens változóról van szó, a vele való operáció előtt mindig ellenőrizni kell null-ra.
	 */
	private transient ArrayList<FieldListener> listeners = null;
	
	/**
	 * Megadja, hogy az adott mező ki van-e választva.
	 * @return Igaz, ha ki van választva, hamis, ha nem.
	 */
	public boolean isSelected() {
		return selected;
	}
	
	/**
	 * Beállítja a mező kiválasztottságát.
	 * @param select Igaz érték esetén a mező kiválasztott lesz, ellenkező esetben a kiválasztás törlésre kerül.
	 */
	public void select (boolean select) {
		selected = select;
		change();
	}
	
	/**
	 * Megadja, hogy az adott mezőre lehetséges-e lépni.
	 * @return Igaz érték esetén lehet, ellenkező esetben nem.
	 */
	public boolean isPossible() {
		return possible;
	}
	
	/**
	 * Beállítja, illetve törli, hogy az adotz mezőre lehessen-e lépni.
	 * @param possible Igaz érték lehet, hamis esetén nem.
	 */
	public void possible(boolean possible) {
		this.possible = possible;
		change();
	}
	
	/**
	 * Figurát tesz az adott mezőre, ha már van rajta figura, azt értesíti a leütés tényéről.
	 * @param f A figura, amit a mezőre akarunk tenni.
	 */
	public void putFigure(Figure f) {
		if(figure != null) //Ha már van figura értesítés a leütésről.
			figure.hitBy(f);
		figure = f;
		change();
	}
	
	/**
	 * Eltávolítja a mezőről a rajta álló figurát. (Azonban azt nem ütik le.)
	 */
	public void removeFigure() {
		figure = null;
		change();
	}
	
	/**
	 * Megadja, hogy milyen figura áll a mezőn.
	 * @return A bábu, ami a mezőn áll.
	 */
	public Figure getFigure() {
		return figure;
	}
	
	/**
	 * A mező belsejében történő változáskor hívandó függvény, értesíti a változásról Listenereket.
	 */
	private void change() {
		if(listeners == null) //Ha nincsenek listenerek, nem csinálunk semmit.
			return;
		for(FieldListener l : listeners) //Ha vannak, értesítjük őket a lehetséges változásról.
			l.change(this);
	}
	
	/**
	 * Listenert regisztrál az adott mezőhöz.
	 * @param l A listener amivel figyelni szeretnénk a mező változásait.
	 */
	public void addFieldListener(FieldListener l) {
		if(listeners == null) //Ha még nincsenek inicalizállva a listenerek, új listát készítünk.
			listeners = new ArrayList<FieldListener>();
		listeners.add(l); //Listener regisztrálása.
		change(); //Listener értesítése a kezdeti állapotról.
	}
}
