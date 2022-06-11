package onitama;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Az adatmodellhez tartozó, a külvilághoz kapcsolodó speciális feladatok ellátása (mentés, betöltés, inicializálás, játék végének jelzése).
 *
 */
public class Game {
	/**
	 * A játékhoz tartozó Controller objektum.
	 */
	private Controller c;
	/**
	 * Az adatmodellben található mezők tömbje.
	 */
	private Field[][] fields = null;
	/**
	 * Az adatmodellben található kártyahelyek listája.
	 */
	private ArrayList<CardSlot> cardSlots = null;
	/**
	 * Az adatmodellben található egyes játékos.
	 */
	private Player p1 = null;
	/**
	 * Az adatmodellben található kettes játékos.
	 */
	private Player p2 = null;
	/**
	 * Az adatmodell fő interakcióit irányítő tábla objektum.
	 */
	private Board board;
	
	/**
	 * Konstruktor, amely beállítja az modellhez tartozó Controllert.
	 * @param controller A modellhez tartozó Controller.
	 */
	public Game(Controller controller) {
		c = controller;
	}
	
	/**
	 * Játék végekor hívandó függvény, amely jelzi ezt a Controllernek, a győztes nevének átadásával.
	 * @param p A győztes játékos.
	 */
	public void gameOver(Player p){
		c.endGame(p.getName());
	}
	
	/**
	 * Új játék indításakor hívandó függvény.A játékhoz tartozó adatmodell inicializálása.
	 * Legfejjebb egy bot játékos lehet, és pontosan egy játékos kezd.
	 * @param fl 5*5-ös mátrix, mely a mezőkhöz tartalmazza a listenereket, a megjelenítésbeli elhelyezkedésüknek megfelelően.
	 * @param csl A kártyahelyekhez tartozó listenerek listája, sorrendjük kötött: p1A, p1B, p2A, p2B, reserve.
	 * @param pl A játékosok változásait figyelő listenerek. Sorrendjük kötött: 1. játékos listenere, 2. játékos listenere, mindkét játékost figyelő listener.
	 * @param p1name Az első játékos neve.
	 * @param p1cpu Az első játékos bot-e.
	 * @param p1first Az első játékos kezd-e.
	 * @param p2name A második játékos neve.
	 * @param p2cpu A második játékos bot-e.
	 * @param p2first A második játékos kezde-e.
	 * @return Az intefész mely számára delegálhatók a mezőkre, és kártyahelyekre történő kattintások.
	 */
	public ModelInterface newGame(FieldListener[][] fl, ArrayList<CardSlotListener> csl, ArrayList<PlayerListener> pl, String p1name, boolean p1cpu, boolean p1first, String p2name, boolean p2cpu, boolean p2first) {
		Bot p1b = null, p2b = null;
		if(p1cpu) { //Játékosok inicializálása aszerint, hogy botok-e.
			p1 = p1b = new Bot(p1name, 1, p1first);
		}
		else {
			p1 = new Player(p1name, 1, p1first);
		}
		if(p2cpu) {
			p2 = p2b = new Bot(p2name, -1, p2first);
		}
		else {
			p2 = new Player(p2name, -1, p2first);
		}
		
		p1.addPlayerListener(pl.get(0)); //Játékosok megjelenítéséhez tartozó listenerek hozzáadása.
		p1.addPlayerListener(pl.get(2));
		
		p2.addPlayerListener(pl.get(1));
		p2.addPlayerListener(pl.get(2));
			
		CastleField.setGame(this); //Speciális mezőknél és bábuknál beállítjuk, hogy kinek szóljanak, ha a játéknak vége szakad.
		KingFigure.setGame(this);
		CardFactory cf = new CardFactory(); //CardFactory, ahonnan majd lekérjük a játékban rész vevő kártyákat.
			
		fields = new Field[5][5]; //Mezők tömbjének létrehozása, majd feltöltése, (bábukkal, és listenerekkel is.)
		for(int y = 0; y < 5; y++) {
			for(int x = 0; x < 5; x++) {
				if((y == 0 || y == 4) && x == 2) {
					if(y == 0) {
						fields[x][y] = new CastleField(p2); //Felső középs mező, vár, és vezér bábu.
						fields[x][y].putFigure(new KingFigure(p2));
					}
					else {
						fields[x][y] = new CastleField(p1); //Alsó középső mező vár, és vezér bábu.
						fields[x][y].putFigure(new KingFigure(p1));
					}
				}
				else if((y == 0 || y == 4) && x != 2){
					fields[x][y] = new Field(); //Egyéb alsó, vagy felső sorbeli mező esetén gyalog.
					fields[x][y].putFigure(new Figure(y == 0 ? p2 : p1));
				}
				else {
					fields[x][y] = new Field(); //Különben csak üres mező.
				}
				fields[x][y].addFieldListener(fl[x][y]); //Listener regisztrálása.
			}
		}
		
		cardSlots = new ArrayList<CardSlot>(); //Kártyahelyek listája.
		
		Card[] cards = cf.getRandomCards(); //A játékban rész vevő kártyák 5 hosszú tömbje. Sorrendjuk, és típusuk véletlenszerű.
		int idx = 0; //Hol tartunk a tömbben.
		for(CardSlotListener l : csl) {
			CardSlot cs = new CardSlot(); //Kártyahely létrehozása.
			cs.addCardSlotListener(l); //Listener regisztrálása.
			cs.setCard(cards[idx++]); //Kártya hozzáadaása a kártyahelyhez.
			cardSlots.add(cs); //Kártyaslot eltárolása.
		}
		board = new Board(fields, cardSlots, p1, p2); //Tábla létrehozása
		if(p1cpu) //Ha valamelyik játékos bot, akkor inicializáció.
			p1b.init(board, fields, cardSlots);
		if(p2cpu)
			p2b.init(board, fields, cardSlots);
		return board; //Visszatérés a modellhez tartozó interfésszel.
	}
	
	/**
	 * Játék elmentésért felelős függvény.
	 * @param savefile Megadja az útvobalat ahová a játékot menteni szeretnénk.
	 */
	public void saveGame(String savefile) {
		try {
			FileOutputStream f = new FileOutputStream(savefile); //File, ahová menteni akarunk-
			ObjectOutputStream os = new ObjectOutputStream(f);
			os.writeObject(fields); //mezők kiírása
			os.writeObject(cardSlots); //slotok kiírésa.
			os.writeObject(p1); //játékosok kírása.
			os.writeObject(p2);
			os.writeObject(board); //tábla kiírása.
			f.close(); //erőforrások felszabadítása.
			//Bár a kiíratás tartalmaz redundanciát, hiszen a board-ban minden benne van, azonban így visszetöltéskor könnyebb lesz beállítani a listenereket.
		}
		catch(Exception e) { //Hiba esetén hibaüzenet STDOUT-ra és kilép.
			System.out.println("Sikertelen mentés!");
			System.exit(-1);
		}
	}
	
	/**
	 * Játék betöltéséért felelős függvény.
	 * @param savefile Fájlnév, ahonnan betölteni szeretnénk.
	 * @param fl 5*5-ös mátrix, mely a mezőkhöz tartalmazza a listenereket, a megjelenítésbeli elhelyezkedésüknek megfelelően.
	 * @param csl A kártyahelyekhez tartozó listenerek listája, sorrendjük kötött: p1A, p1B, p2A, p2B, reserve.
	 * @param pl A játékosok változásait figyelő listenerek. Sorrendjük kötött: 1. játékos listenere, 2. játékos listenere, mindkét játékost figyelő listener.
	 * @return Az intefész mely számára delegálhatók a mezőkre, és kártyahelyekre történő kattintások.
	 */
	public ModelInterface loadGame(String savefile, FieldListener[][] fl, ArrayList<CardSlotListener> csl, ArrayList<PlayerListener> pl) {
		try {
			FileInputStream f = new FileInputStream(savefile); //File, ahonnan olvasni szeretnénk.
			ObjectInputStream is = new ObjectInputStream(f);
			fields = (Field[][]) is.readObject(); //mezők visszaolvasása.
			cardSlots = (ArrayList<CardSlot>) is.readObject(); //slotok vissszaolvasása.
			p1 = (Player) is.readObject(); //játékosok visszaolvasása.
			p2 = (Player) is.readObject();
			board = (Board) is.readObject(); //tábla visszaolvasása.
			f.close(); //erőforrások felszabadítása.
		}
		catch (FileNotFoundException exc) { //Ha nem találunk mentés fájlt, új játék indításam Player1, és Player2 nevekkel, valamint default paraméterekkel.
			ModelInterface ret = newGame(fl, csl, pl, "Player1", false, true, "Player2", false, false);
			return ret;
		}
		catch(Exception e) {
			System.out.println("Bad file error!"); //Egyéb hiba esetén hibaüzenet STDOUT-ra, és terminálás.
			System.exit(-1);
		}
		
		CastleField.setGame(this); //Speciális mezőknél és bábuknál beállítjuk, hogy kinek szóljanak, ha a játéknak vége szakad.
		KingFigure.setGame(this);
		
		p1.addPlayerListener(pl.get(0)); //Játékosok megjelenítéséhez tartozó listenerek hozzáadása.
		p1.addPlayerListener(pl.get(2));
		
		p2.addPlayerListener(pl.get(1));
		p2.addPlayerListener(pl.get(2));
		
		for(int y = 0; y < 5; y++) {
			for(int x = 0; x < 5; x++) {
				fields[y][x].addFieldListener(fl[y][x]); //Mezőkhoz tartozó listenerek regisztrálása.
			}
		}
		
		for(int i = 0; i < 5; i++) {
			cardSlots.get(i).addCardSlotListener(csl.get(i)); //Slotokhoz tartozó listenerek regisztrálása.
		}
		
		return board; //Visszatérés a modellhez tartozó interfésszel..
	}
	
}
