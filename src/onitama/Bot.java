package onitama;

import java.util.ArrayList;
import java.util.Random;

/**
 * Egyszerű Bot osztály, amely önálló játékra képes.
 *
 */
public class Bot extends Player{
	/**
	 * Az adatmodellben található mezők.
	 */
	private Field[][] fields;
	/**
	 * Az adatmodellben találhatü kártyahelyek.
	 */
	private ArrayList<CardSlot> cardSlots;
	/**
	 * Intefész, amelynek a lépéseket jelezni kell.
	 */
	private ModelInterface board;
	
	/**
	 * Új bot létrehozása
	 * @param name A bot neve
	 * @param direction A bot iránya (Ha a tábla alján kezd 1, különben -1).
	 * @param first Megadja, hogy a bot kezd-e.
	 */
	public Bot(String name, int direction, boolean first) {
		super(name, direction, first);
	}
	
	/**
	 * Egyéb tagváltozók inicializációja.
	 * @param board Az interfész, aminek a lépéseket kommunikálni kell.
	 * @param fields Az adatmodell mezőinek tömbje.
	 * @param cardSlots Az adatmodell slotjainak listája, sorrendjük, és a hossz fix (p1A, p1B, p2A, p2B, reserve).
	 */
	public void init(ModelInterface board, Field[][] fields, ArrayList<CardSlot> cardSlots) {
		this.fields = fields;
		this.cardSlots = cardSlots;
		this.board = board;
		setActive(isActive()); //Lépésre kényszerítjük a botot, ha már aktív.
	}
	
	/**
	 * Beállítja a bot aktivitását.
	 * @param a Aktív legyen-e a bot.
	 */
	@Override
	public void setActive(boolean a) {
		super.setActive(a);
		if(board == null) //Ha még nincs inicializálva visszatérés.
			return;
		while(isActive()) { //Ellenkező esetben lépés.
			move();
		}
	}
	
	/**
	 * Lépés függvény: Addig ad véletlenszerű inputokat, amíg akív, tehát ő van soron.
	 */
	private void move() {
		Random rng = new Random(); //Random szám generátor.
		while(isActive()) { //Amíg aktív.
			int card = rng.nextInt(ModelInterface.CardType.values().length);
			board.selectCard(ModelInterface.CardType.values()[card]); //Kártyaslot választása.
			int x = rng.nextInt() % 5;
			int y = rng.nextInt() % 5;	
			board.selectField(new Position(x < 0 ? x*-1 : x, y < 0 ? y *-1 : y)); //Mező választása.
		}
	}
}
