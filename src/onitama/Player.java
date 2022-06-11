package onitama;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Az adatmodellben a játékosokat reprezentáló osztály.
 *
 */
public class Player implements Serializable {
	/**
	 * A játékos neve.
	 */
	private String name;
	/**
	 * Megadja, hogy a játékos alul, vagy fölül kezd értéke ez alapján csak pontosan 1, vagy -1 lehet.
	 */
	private int direction;
	/**
	 * Megadja, hogy a játékos kezdőjátékos-e (Ettől függ a színe).
	 */
	private boolean first;
	/**
	 * Megadja, hogy a játékos fog-e épp lépni.
	 */
	private boolean active = false;
	/**
	 * A játékos változásait követő listenerek. Tranziens mivoltából fakadóan mindig ellenőrizni kell, hogy inicializált-e.
	 */
	private transient ArrayList<PlayerListener> listeners = null;
	
	/**
	 * Konstruktor, amely a megadott paraméterek alapján inicializálja a játékost.
	 * @param name A játékos neve.
	 * @param direction Megadja, hogy a játékos alul, vagy fölül kezd értéke ez alapján csak pontosan 1, vagy -1 lehet.
	 * (Nem túl elegáns, azonban a kártyákat, és bábuk lépéseit kezelő utasítások során meglehetősen kényelmes megoldás.)
	 * @param first Megadja, hogy a játékos kezdőjátékos-e (Ettől függ a színe).
	 */
	public Player(String name, int direction, boolean first) {
		this.name = name; //tagváltozók beállítása.
		this.direction = direction;
		this.first = first;
	}
	
	/**
	 * Megadja, hogy a játékos kezdőjátékos-e
	 * @return Igaz érték esetén igen, ellenkező esetben nem.
	 */
	public boolean isFirst() {
		return first;
	}
	
	/**
	 * Megadja, hogy a játékos a tábla tetejéről, vagy aljáról indul.
	 * @return 1, ha a tábla aljáról, -1, ha a tetejéről.
	 */
	public int getDirection() {
		return direction;
	}
	
	/**
	 * Megadja a játékos nevét.
	 * @return A játékos neve.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Megadja, hogy éppen ez a játékos van-e soron.
	 * @return Igaz érték esetén igen, ellenkező esetben nem.
	 */
	public boolean isActive() {
		return active;
	}
	
	/**
	 * Beállítja, hogy épp a játékos következik-e.
	 * @param a Igaz érték esetén igen, ellenkező esetben nem.
	 */
	public void setActive(boolean a) {
		active = a;
		change();
	}
	
	/**
	 * Egy a játékos változásait figyelő listenert regisztrál.
	 * @param l A regisztrálandó listener.
	 */
	public void addPlayerListener(PlayerListener l) {
		if(listeners == null) //Ha még nincsenek listener tároló, készítünk.
			listeners = new ArrayList<PlayerListener>();
		listeners.add(l); //Listener hozzáadása a tárolóhoz.
		change(); //Hogy a hozzáadott listenerhez tartozó nézet, beállítódjon.
	}
	
	/**
	 * A játékos adatainak változtatása után hívandó függvény.
	 */
	private void change() {
		if(listeners == null) //Ha nincs listener nem csinálunk semmit.
			return;
		for(PlayerListener l : listeners) //Ha van, mindenkinek szólunk.
			l.change(this);
	}
}
