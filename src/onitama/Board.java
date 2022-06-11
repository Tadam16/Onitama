package onitama;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Modellelem, ami a külvilág számára nyújtott interfész kritikus elemeit implementálja. 
 * (Mezők, és kártyahelyek kiválasztásának kezelése, belső adatok frissítése az interakciók alapján.)
 */
public class Board implements ModelInterface, Serializable {
	/**
	 * Az adatmodell mezőinek 5*5-ös tömbje.
	 */
	private Field[][] fields;
	/**
	 * Az éppen aktív játékos által kiválasztott kezdőpozíció, ha valid.
	 */
	private Position srcPos;
	/**
	 * A játékosok. activePlayer értéke, megegyezik, az egyes, vagy a kettes játékoséval.
	 */
	private Player player1, player2, activePlayer;
	/**
	 * A figura állapotát jellemző enumeráció. select értékkor még nincs kiválasztva kiindulási mező (srcPos nem valid),
	 * move értékkor már a mozgatás helyére, vagy kártya kiválasztására vár.
	 */
	private enum FigureState{select, move};
	/**
	 * A kártyaválasztás állapotát jellemző enumeráció. Select értékkor még nincs kiválasztva cardslot (activeCardSlot nem valid), 
	 * move értékkor, már csak a mozgatás helyére, vagy saját figura kiválasztására vár.
	 */
	private enum CardState{select, move};
	/**
	 * Figuraválasztás állapotát leíró tagváltozó.
	 */
	private FigureState fstate = FigureState.select;
	/**
	 * Kártyaválasztás állapotát leíró tagváltozó.
	 */
	private CardState cstate = CardState.select; 
	/**
	 * A tárolt kártyahelyek, activeCardSlot értéke mindig az első 5 közül kerül ki, ha valid.
	 */
	private CardSlot reserve, player1A, player1B, player2A, player2B, activeCardSlot = null;
	
	/**
	 * Konstruktor, amely inicializálja a belső változókat.
	 * 
	 * @param fields Mezők, amelyekkel a modell dolgozni fog. Egy 5*5-ös két dimenziós tömbnek kell lennie.
	 * @param cardslots Kártyahelyek, amelyekkel a modell dolgozni fog. A játékban szereplő kártyákat már tartalmazniuk kell.
	 * Sorrendjuk kötött: p1A, p1B, p1A, p2B, reserve.
	 * @param p1 Az első játékosra mutató referencia, aki a tábla aljáról indul.
	 * @param p2 A második játékosra mutató referencia, aki a tábla tetejáről indul.
	 */
	public Board(Field[][] fields, ArrayList<CardSlot> cardslots, Player p1, Player p2) {
		this.fields = fields; // Tagváltozók inicializálása.
		player1A = cardslots.get(0);
		player1B = cardslots.get(1);
		player2A = cardslots.get(2);
		player2B = cardslots.get(3);
		reserve = cardslots.get(4);
		player1 = p1;
		player2 = p2;
		
		if(player1.isFirst()) { // Kezdőjátékos beállítása.
			activePlayer = player1;
			player1.setActive(true);
		}
		else {
			activePlayer = player2;
			player2.setActive(true);
		}
	}
	
	/**
	 * A interfész metódus, amely lehetőséget nyújt a külvilágnak egy mező kiválasztására. 
	 * @param p A kiválasztott mező pozíciója. Az argumentum x és y tagváltozóinak értéke 0 és 4 köze kell, hogy essen.
	 */
	@Override
	public void selectField(Position p) {
		Field selectedField = fields[p.x][p.y]; //A kiválasztott mező.
		Figure selectedFigure = selectedField.getFigure(); //A mezőn található bábu.
		
		if(fstate == FigureState.select) { //Ha bábu kiválasztás állapotban vagyunk.
			if(selectedFigure == null) //A kiválasztott figura null: nem csinálunk semmit.
				return;
			else if(selectedFigure.getOwner() == activePlayer) { //A kiválasztott figura tulajdonosa megegyezik az aktív játékossal:
				selectedField.select(true); //mező kiválasztása.
				srcPos = p; //Kiindulási pozíció frissítése.
				fstate = FigureState.move; //Állapotváltozók beállítása.
				setPossibilities(p, true); //Lépési lehetőségek kirajzolása.
			}
		}
		
		else { //Ha már van kiválasztva bábu:
			if(selectedFigure != null && selectedFigure.getOwner() == activePlayer) { //Ha újra saját figurát választunk ki:
				fields[srcPos.x][srcPos.y].select(false); //Előzőleg kiválasztott mező kiválasztásának törlése.
				setPossibilities(srcPos, false); //Lehetőségek kirajzolásának megszüntetése.
				selectedField.select(true); //Az új mező kiválasztása.
				srcPos = p; //Kezdőpozíció frissítése.
				setPossibilities(p, true); //Lépési lehetőségek kirajzolása.
				return;
			}
			else if(cstate == CardState.move) //Ha nem saját figurát választunk ki: megpróbálunk mozogni.
				move(p);
		}
	}
	
	/**
	 * Beállítja a mezőkön, hogy léphet-e rájuk a játékos.
	 * @param p A pozíció, ahonnan a lépés indulna.
	 * @param set Igaz értéknél bekapcsolja a jelzést, hamis értéknél eltünteti. 
	 */
	private void setPossibilities(Position p, boolean set) {
		if(cstate == CardState.move) { //Ha van kiválasztva kártya:
			for(Position poss : activeCardSlot.getCard().getPossiblePositions(p, activePlayer.getDirection())) { //Minden olyan pozíció ahová a kezdeti pozícióról lépni lehetne a kártya segítségével:
				Field cf = fields[poss.x][poss.y]; //Mező lekérése az adott pozíción.
				if(cf.getFigure() == null || cf.getFigure().getOwner() != activePlayer) //Ha a mezőn nem áll saját játékos, lépési lehetőség jelzése.
					cf.possible(set);
			}
		}
	}

	/**
	 * Kártyaslot kiválasztását teszi lehetővé a külvilág számára.
	 * @param type A kiválasztott kártyaslot neve.
	 */
	@Override
	public void selectCard(ModelInterface.CardType type) {
		if(activePlayer == player1) { //Ha az egyes játékos aktív, és, ha neki választották valamely slotját, azt aktiváljuk.
			if(type == ModelInterface.CardType.player1A) {
				setActiveCardSlot(player1A);
			}
			else if(type == ModelInterface.CardType.player1B) {
				setActiveCardSlot(player1B);
			}
		}
		else if(activePlayer == player2) { //Ha az kettes játékos aktív, és, ha neki választották valamely slotját, azt aktiváljuk.
			if(type == ModelInterface.CardType.player2A) {
				setActiveCardSlot(player2A);
			}
			else if(type == ModelInterface.CardType.player2B) {
				setActiveCardSlot(player2B);
			}
		}
	}
	
	/**
	 * Be, illetve átállítja a modellben az aktív kártyaslotot, és elvégzi az ezzel járó egyéb szükséges adminisztrációt.
	 * @param c A kártyahely, amit aktívvá szeretnénk tenni.
	 */
	private void setActiveCardSlot(CardSlot c) {
		
		if(cstate == CardState.move) { //Ha már volt kiválasztva valamely slot:
			activeCardSlot.select(false); //slot kiválasztásának törlése.
			if(fstate == FigureState.move) { //Ha van kiválasztva figura, lépési lehetőségek mutatásának törlése.
				setPossibilities(srcPos, false);
			}
		}
		
		activeCardSlot = c; //Új slot aktiválása.
		activeCardSlot.select(true);
		
		cstate = CardState.move; //slot kiválasztási állapotváltozó beállítása.
		if(fstate == FigureState.move) //Ha van kiválasztva mező, lépési lehetőségek kirajzolása.
			setPossibilities(srcPos, true); 
	}
	
	/**
	 * Végrehajt egy kör beli lépést, és elvégzi az ezzel járó adminisztrációt.
	 * @param dst A pozíció, ahová a bábut mozgatni szeretnénk. (A forráshely osztályszintű változóként tárolódik.)
	 */
	private void move(Position dst) {
		Field srcField = fields[srcPos.x][srcPos.y]; //Indulási mező.
		Field dstField = fields[dst.x][dst.y]; //Célmező.
		setPossibilities(srcPos, false); //Lépési lehetőségek törlése.
		if(activeCardSlot.getCard().canMove(srcPos, dst, activePlayer.getDirection())) { //Ha lehet a célpozícióra mozogni:
			
			Figure f = srcField.getFigure(); //Figura mozgatása. (Azért itt, mert csak ennyi miatt felesleges lenne ismernie a mező osztályt.)
			srcField.removeFigure();
			dstField.putFigure(f);
			
			activeCardSlot.select(false); //Slotkiválasztás deaktiválása.
			
			Card tmp = reserve.getCard(); //reserve, és az activeCardSlot közötti kártyacsere.
			reserve.setCard(activeCardSlot.getCard());
			activeCardSlot.setCard(tmp);
			
			cstate = CardState.select; //Állapotváltozók átállítása, kiindulási mező kiválasztásának törlése.
			srcField.select(false);
			fstate = FigureState.select;
			
			activePlayer.setActive(false); //Aktív játékos átállítása.
			activePlayer = activePlayer == player1 ? player2 : player1;
			activePlayer.setActive(true);
		}
		else { //Ha nem lehet a célpozícióra mozogni:
			srcField.select(false); //Kiindulási mező kiválasztásának törlése
			fstate = FigureState.select; //Bábuválasztási állapotváltozó alaphelyzetbe állítása.
		}
	}
}
