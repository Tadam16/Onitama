package onitama;

import java.util.ArrayList;

import org.junit.*;

//Board osztály alapabb funkcióinak tesztelése.
public class BoardTest {
	
	private Board b;
	private Field fields[][];
	private ArrayList<CardSlot> slots;
	private Player p1, p2;
	
	@Before //Tesztelés előtti inicializáció.
	public void init() {
		p1 = new Player("P1", 1, true);
		p2 = new Player("P2", -1, false);
				
		fields = new Field[5][5];
		for(int y = 0; y < 5; y++) {
			for(int x = 0; x < 5; x++) {
				if((y == 0 || y == 4) && x == 2) {
					if(y == 0) {
						fields[x][y] = new CastleField(p2);
						fields[x][y].putFigure(new KingFigure(p2));
					}
					else {
						fields[x][y] = new CastleField(p1);
						fields[x][y].putFigure(new KingFigure(p1));
					}
				}
				else if((y == 0 || y == 4) && x != 2){
					fields[x][y] = new Field();
					fields[x][y].putFigure(new Figure(y == 0 ? p2 : p1));
				}
				else {
					fields[x][y] = new Field();
				}
			}
		}
		
		slots = new ArrayList<CardSlot>();
		Card cards[] = new CardFactory().getRandomCards();
		for(Card c : cards) {
			CardSlot s = new CardSlot();
			s.setCard(c);
			slots.add(s);
		}
		
		b = new Board(fields, slots, p1, p2);
	}
	
	@Test //Kezdőjátékos megfelelő-e.
	public void initValueTest() {
		Assert.assertEquals(true, p1.isActive());
		Assert.assertEquals(false, p2.isActive());
	}
	
	@Test //Kártyakiválasztás
	public void cardSelectionTest() {
		b.selectCard(ModelInterface.CardType.player2A); //1-es játékos kezd, tehát ha 2-es játékos kártyáját választjuk semmi sem választódhat ki.
		for(CardSlot s : slots) {
			Assert.assertEquals(false, s.isSelected());
		}
		
		b.selectCard(ModelInterface.CardType.player1A); //Ennek a kártyának ki kéne választódnia.
		Assert.assertEquals(true, slots.get(0).isSelected()); //Az inicializációs sorrend miatt, ennek a kártyának p1A slothoz kell tartoznia.
		
		b.selectCard(ModelInterface.CardType.player1B); //Nézzük meg mi történik ha a játékos másik kártyáját választjuk ki.
		Assert.assertEquals(false, slots.get(0).isSelected());
		Assert.assertEquals(true, slots.get(1).isSelected());
	}
	
	@Test //Mezőkiválasztás
	public void fieldSelectionTest() {
		b.selectField(new Position(2,2)); //Nem lehet rögtön a tábla közepén figura, úgyhogy ennek semmit sem kéne kiválasztania.
		for(Field arr[] : fields) {
			for(Field f : arr) {
				Assert.assertEquals(false, f.isSelected());
			}
		}
		
		b.selectField(new Position(4,4)); //Itt kéne lennie egy fehér parasztnak.
		Assert.assertEquals(true, fields[4][4].isSelected());
		
		b.selectField(new Position(2,4)); //Nézzük meg, hogy mi történik ha a királyt választjuk ki.
		Assert.assertEquals(true, fields[2][4].isSelected());
		Assert.assertEquals(false, fields[4][4].isSelected());
	}
	
	@Test //Mozgatás
	public void moveTest() {
		slots.get(0).setCard(new Card("move1forward",new Position[] {new Position(0, 1)})); //Különleges tesztkártya behelyezése p1A slotba.
		b.selectField(new Position(2,4));
		b.selectCard(ModelInterface.CardType.player1B); //Cserélgessük a kártyaszelekciót is.
		b.selectCard(ModelInterface.CardType.player1A);
		//Miután figura, és kártya is ki lett választva, valamint tudjuk a kártya relatív pozícióit, meg tudjuk mondani, hogy a (2,3) mezőnek lehetséges lépésként kell szerepelnie.
		Assert.assertEquals(true, fields[2][3].isPossible());
		
		Figure fig = fields[2][4].getFigure();
		b.selectField(new Position(2,2)); //Próbáljunk invalid mezőre lépni.
		Assert.assertEquals(false, fields[2][4].isSelected()); //A kiválasztásnak törlődnia kellett.
		
		b.selectField(new Position(2,4));
		b.selectField(new Position(2,3)); //Próbáljunk odalépni.
		
		Assert.assertEquals(fig, fields[2][3].getFigure()); // A mozgatásnak sikeresnek kell lennie.
		Assert.assertEquals(null, fields[2][4].getFigure());
		
		for(Field arr[] : fields) { // Egy mező sem kéne, hogy kiválasztva legyen mozgatás után.
			for(Field f : arr) {
				Assert.assertEquals(false, f.isSelected());
				Assert.assertEquals(false, f.isPossible());
			}
		}
		
		for(CardSlot s : slots) { // Ez igaz a kártyahelyekre is.
			Assert.assertEquals(false, s.isSelected());
		}
		
		Assert.assertEquals(true, p2.isActive()); //A 2-es játékosnak kell aktívnak lennie a mozgatás után.
		Assert.assertEquals(false, p1.isActive());
		
	}
}
