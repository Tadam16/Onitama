package onitama;

import org.junit.Test;

import org.junit.Assert;

//Kártyagenerátor osztály ellenőrzése,
public class CardFactoryTest {
	
	@Test //Random kártyagenerátor ellenőrzése,
	public void getRandomCardsTest() {
		CardFactory cf = new CardFactory();
		Card[] cards = cf.getRandomCards();
		Assert.assertEquals(5, cards.length); //Pontosan 5 kártyának kell generálódnia.
		for(int x = 0; x < 5; x++) {
			for(int y = x + 1; y < 5; y++) {
				Assert.assertNotEquals(cards[x], cards[y]); //csupa különböző kártyát kapunk.
			}
		}
	}
}
