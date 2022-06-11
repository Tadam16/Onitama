package onitama;

import org.junit.*;

//Field osztály tesztelése.
public class FieldTest {
	private Field f;
	private boolean has_changed;
	
	//Tesztelés előtti inicializáció.
	@Before
	public void init() {
		f = new Field();
		has_changed = false;
	}
	
	//Alapértékek tesztelése
	@Test
	public void defValues(){
		Assert.assertEquals(false, f.isSelected());
		Assert.assertEquals(false, f.isPossible());
		Assert.assertEquals(null, f.getFigure());
	}
	
	//Mező kiválaztásának tesztelése.
	@Test
	public void selectTest() {
		f.select(true);
		Assert.assertEquals(true, f.isSelected());
		f.select(true);
		Assert.assertEquals(true, f.isSelected());
		f.select(false);
		Assert.assertEquals(false, f.isSelected());
		f.select(false);
		Assert.assertEquals(false, f.isSelected());
	}
	
	//Mező lehetségesként kiválasztásának tesztelése.
	@Test
	public void possibleTest() {
		f.possible(true);
		Assert.assertEquals(true, f.isPossible());
		f.possible(true);
		Assert.assertEquals(true, f.isPossible());
		f.possible(false);
		Assert.assertEquals(false, f.isPossible());
		f.possible(false);
		Assert.assertEquals(false, f.isPossible());
	}
	
	//Figura hozzáadás, és levétel tesztelése.
	@Test
	public void figureHandlingTest() {
		f.addFieldListener((s) -> has_changed = true);
		Figure fig = new Figure(new Player("Testplayer", 1, false));
		f.putFigure(fig);
		Assert.assertEquals(fig, f.getFigure());
		Assert.assertEquals(true, has_changed); //A mezőnek üzennie kellett a listenerjeinek.
		f.removeFigure();
		Assert.assertEquals(null, f.getFigure());
	}
	
}
