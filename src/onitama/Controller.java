package onitama;

import java.awt.event.ActionListener;

/**
 * Az MVC architectúrában a kontroller szerepét betöltő főosztály. 
 * Feldolgoza és delegálja a nézetben történő eseményeket az adatmodellnek.
 *
 */
public class Controller {
	/**
	 * A megjelenítésbeli mezőkhöz tartozó kattintásokat kezelő listenerek tömbje.
	 */
	private ActionListener[][] fieldActionListeners = new FieldActionListener[5][5];
	/**
	 * Az adatmodell fő interfésze, akinek delegálni kell, a mező, és kártyakiválasztásokat.
	 */
	private ModelInterface model;
	/**
	 * A megjelenítésben a kártyakiválasztásokat kezelő listenerek.
	 */
	private ActionListener 	player1ACard = new CardSlotActionListener(ModelInterface.CardType.player1A), 
		player1BCard = new CardSlotActionListener(ModelInterface.CardType.player1B),
		player2ACard = new CardSlotActionListener(ModelInterface.CardType.player2A),
		player2BCard = new CardSlotActionListener(ModelInterface.CardType.player2B);
		//reserveCard = new CardSlotActionListener(ModelInterface.CardType.reserve), //Nem szükséges, mert nem foglakozunk az erre a gombra történő kattintásokkal, hiszen soha nem lehet kiválasztani.
	/**
	 * A megjelenítés állapotát jellemző enumeráció.
	 */
	public enum State{menu, game, endgame};
	/**
	 * Az általánosabb gombok lehetséges akcióit tároló enumeráció.
	 */
	public enum ButtonAction {save, load, toMenu};
	/**
	 * A játékbeli megjelenítés jelenlegi állapotát tükröző belső állapotváltozó.
	 */
	private State state = State.menu;
	/**
	 * A megjelenítésért felelős fő objektum.
	 */
	private ViewController view;
	/**
	 * Az adatmodellt elmentő, betöltő, inicializáló objektum, továbbá ő szól akkor is, ha a játéknak vége szakad.
	 */
	Game g;
	
	/**
	 * Az osztály konstruktora, amely inicializálja a belső változókat, és a nézetet, majd ezután a játékot is.
	 */
	public Controller() {
		CardSlotActionListener.setController(this); //Listenerekhez statikus Controller beállítása.
		FieldActionListener.setController(this);
		
		for(int y = 0; y < 5; y++) {
			for(int x = 0; x < 5; x++) {
				fieldActionListeners[x][y] = new FieldActionListener(new Position(x,y)); //Mezőkre kattint eseménykezelők elkészítése.
			}
		}
		view = new ViewController(this, fieldActionListeners, player1ACard, player1BCard, player2ACard, player2BCard); //Nézet inicializálása.
		g = new Game(this); //Játék objektum inicializálása.
	}
	
	/**
	 * A nézeten található általánosabb gombok esetén kattintás feldolgozása.
	 * @param a A kattintás attribútumait leíró objektum.
	 */
	public void ButtonAction(ButtonAction a) {
		switch (a) {
		case save: //Játék elmentése.
			if(state == State.game) {
				g.saveGame("save.bak");
			}
			break;
		case load: //Játék betöltése.
			model = g.loadGame("save.bak", view.getFieldListeners(), view.getCardSlotListeners(), view.getPlayerListeners()); //Adatmodell betöltése.
			view.ChangeView(State.game); //Nézet, és hozzá tartozó állapotváltozó változtatás.
			state = State.game;
			break;
		case toMenu: //Visszalépés a menübe.
			if(state == State.game || state == State.endgame) {
				view.ChangeView(State.menu); //Nézet, és hozzá tartozó állapotváltozó változtatás.
				state = State.menu;
			}
			break;
		}
	}
	
	/**
	 * Új játék indításakor hívandó metódus.
	 * Pontosan egy játékosnak kell kezdenie. Legfejjebb egy játékos lehet bot.
	 * @param p1name Az első játékos neve.
	 * @param p1cpu Megadja, hogy az első játékos bot-e.
	 * @param p1first Megadja, hogy az első játékos kezd-e.
	 * @param p2name A második játékos neve.
	 * @param p2cpu Megadja, hogy második játékos bot-e.
	 * @param p2first Megadja, hogy a második játékos kezd-e.
	 */
	public void startGame(String p1name, boolean p1cpu, boolean p1first, String p2name, boolean p2cpu, boolean p2first) { //Új játék indítésakor hívódó függvény.
		model = g.newGame(view.getFieldListeners(), view.getCardSlotListeners(), view.getPlayerListeners(), p1name, p1cpu, p1first, p2name, p2cpu, p2first); //Új játék indítása.
		view.ChangeView(State.game); //Nézet, és hozzá állapotváltozó átállítás.
		state = State.game;
	}
	
	/**
	 * A játék végekor hívandó függvény. Bár valamennyire megtöri az MVC-t,
	 * azonban ennek jelzése csak a View felé meglehetősen nehézkes lenne, hiszen a Controller is tárolja a játék állapotát.
	 * @param name A győztes játékos neve.
	 */
	public void endGame(String name) {
		if(state == State.game) { //Csak, ha éppen zajlik a játék.
			state = State.endgame;
			view.ChangeView(State.endgame); //Nézet, és hozzá állapotváltozó átállítás.
			view.setWinnerName(name); //Győztes nevének beállítása.
		}
	}
	
	/**
	 * Kártyahelyre történő kattintáskor az akció delegálása a modellnek.
	 * @param card A kártyahely, amelyre kattintottunk.
	 */
	public void CardAction(ModelInterface.CardType card) {
		if(state == State.game) { //Ha éppen megy a játék, hívás delegálása az adatmodell felé.
			model.selectCard(card);
		}
	}
	
	/**
	 * Mezőre történő kattintáskor az akció delegálása a modellnek.
	 * @param p A kiválasztott mező koordinátái.
	 */
	public void FieldAction(Position p) {
		if(state == State.game) //Ha éppen megy a játék, hívás delegálása az adatmodell felé.
			model.selectField(p);
	}
}
