package onitama;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A modellben a kártyákkal kapcsolatos adatokat, és velük végezhető műveleteket implementáló osztáy.
 *
 */
public class Card implements Serializable {
	
	/**
	 * A kártya neve.
	 */
	private String name;
	/**
	 * Relatív poziciók, amelyekre a kártya segítségével lépni lehet.
	 */
	private Position[] relativePositions;
	
	/**
	 * Az osztály konstruktora.
	 * @param name A kártya neve.
	 * @param pos Relatív pozíciók, amely irányokba lépések lehetségesek.
	 */
	public Card(String name, Position[] pos) {
		this.name = name; //Tagváltozók inicializálása.
		this.relativePositions = pos;
	}
	
	/**
	 * Megadja a kártya nevét.
	 * @return A kártya neve.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Megadja, hogy az adott lépés a két pozíció között ezen kártya segítségével lehetséges-e.
	 * @param src A kiindulási pozíció.
	 * @param dst A cél pozíció.
	 * @param direction A mozgatás iránya, azaz, hogy a tábla tetején, vagy alján induló játékos lépne-e, értéke csak pontosan 1, vagy -1 lehet.
	 * @return Megadja, hogy a kapott paraméterek alapján a mozgatás lehetséges-e.
	 */
	public boolean canMove(Position src, Position dst, int direction) {
		Position v = new Position(dst.x - src.x, src.y - dst.y); //kezdőponthoz képesti relatív pozíció.
		v.x *= direction; //Középpontos tükrözés a mozgás iránya szerint.
		v.y *= direction;
		for(int i = 0; i < relativePositions.length; i++) {
			Position p = relativePositions[i];
			if(v.x - p.x == 0 && v.y - p.y == 0) //Ha megyezik az így kapott pozíció valamelyik kártya szerint tárolttal, a lépés lehetséges.
				return true;
		}
		return false; //Ellenkező esetben nem.
	}
	
	/**
	 * Megadja, hogy adott pozícióból milyen mezőkre lehetséges a mozgatás.
	 * @param src Kiindulási pozíció.
	 * @param direction A mozgatás iránya, azaz, hogy a tábla tetején, vagy alján induló játékos lépne-e, értéke csak pontosan 1, vagy -1 lehet.
	 * @return Lista, amely a kapott lehetséges pozíciókat tartalmazza.
	 */
	public ArrayList<Position> getPossiblePositions(Position src, int direction) {
		ArrayList<Position> ret = new ArrayList<Position>(); //Visszatérési lista inicializálása.
		for(int i = 0; i < relativePositions.length; i++) {
			Position p = relativePositions[i];
			Position dst = new Position(src.x + p.x * direction, src.y - p.y * direction); //A forrásból, és a relatív poziciókból (irány szerint középpontosan tükrözve) abszolót pozíció számítása.
			if(dst.x < 5 && dst.x >= 0 && dst.y < 5 && dst.y >= 0) //Ellenőrzés, hogy a kapott pozíció a táblán van-e.
				ret.add(dst); //Ha igen, hozzáadjuk a listához.
		}
		return ret; //Lista visszaadása.
	}
}
