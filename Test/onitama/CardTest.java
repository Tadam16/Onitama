package onitama;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.*;

//Kártya osztály tesztelése
public class CardTest {
	private Card c;
	
	@Before
	public void init() {
		c = new Card("triangle", new Position[] {new Position(-1,-1), new Position(0,1), new Position(1, -1)});//Saját tesztelési célra készült kártya inicializálása.
	}
	
	@Test
	public void tests() {
		Assert.assertEquals("triangle", c.getName()); //név tesztlése.
		
		List<Position> pos = c.getPossiblePositions(new Position(2,2), 1);
		Assert.assertEquals(3, pos.size()); //A középső mezőből három másikra is lehetségesnek kell lennie a lépsnek.
		List<Position> pos2 = c.getPossiblePositions(new Position (2, 0),  -1);
		Assert.assertEquals(1, pos2.size()); //Itt viszont már csak egy lehetséges lépés kell hogy legyen
		Assert.assertEquals(new Position(2,1).x, pos2.get(0).x); //Ez a pozíció pedig (2,1) kell, hogy legyen.
		Assert.assertEquals(new Position(2,1).y, pos2.get(0).y);
	}
}
