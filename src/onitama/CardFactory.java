package onitama;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A játékban előfurduló kártyák nyilvántartása, lekérhető belőlük az egy játék megkezdéséhez szükséges 5 véletlenszerű kártya.
 *
 */
public class CardFactory {
	
	/**
	 * Az összes kártya adatait tároló lista.
	 */
	ArrayList<Card> cards = new ArrayList<Card>();
	
	/**
	 * Konstruktor, amely feltölti a tárolt kártyák listáját a kártyák adataival.
	 */
	public CardFactory() {
		cards.add(new Card("Tiger", new Position[] {new Position(0, 2), new Position(0,-1)}));
		cards.add(new Card("Dragon", new Position[] {new Position(-2,1), new Position(-1,-1), new Position(1,-1), new Position(2,1)}));
		cards.add(new Card("Frog", new Position[] {new Position(-2,0), new Position(-1,1), new Position(1,-1)}));
		cards.add(new Card("Rabbit", new Position[] {new Position(-1,-1), new Position(1,1), new Position(2,0)}));
		cards.add(new Card("Crab", new Position[] {new Position(-2,0), new Position(0,1), new Position(2,0)}));
		cards.add(new Card("Elephant", new Position[] {new Position(-1,1), new Position(-1,0), new Position(1,1), new Position(1,0)}));
		cards.add(new Card("Goose", new Position[] {new Position(-1,1), new Position(-1,0), new Position(1,0), new Position(1,-1)}));
		cards.add(new Card("Rooster", new Position[] {new Position(-1,0), new Position(-1,-1), new Position(1,1), new Position(1,0)}));
		cards.add(new Card("Monkey", new Position[] {new Position(-1,1), new Position(-1,-1), new Position(1,1), new Position(1,-1)}));
		cards.add(new Card("Mantis", new Position[] {new Position(-1,1), new Position(0,-1), new Position(1,1)}));
		cards.add(new Card("Horse", new Position[] {new Position(-1,0), new Position(0,1), new Position(0,-1)}));
		cards.add(new Card("Ox", new Position[] {new Position(0,1), new Position(0,-1), new Position(1,0)}));
		cards.add(new Card("Crane", new Position[] {new Position(-1,-1), new Position(0,1), new Position(1,-1)}));
		cards.add(new Card("Boar", new Position[] {new Position(-1,0), new Position(0,1), new Position(1,0)}));
		cards.add(new Card("Eel", new Position[] {new Position(-1,1), new Position(-1,-1), new Position(1,0)}));
		cards.add(new Card("Cobra", new Position[] {new Position(-1,0), new Position(1,1), new Position(1,-1)}));
		cards.add(new Card("Fox", new Position[] {new Position(1,1), new Position(1,0), new Position(1,-1)}));
		cards.add(new Card("Dog", new Position[] {new Position(-1,1), new Position(-1,0), new Position(-1,-1)}));
		cards.add(new Card("Giraffe", new Position[] {new Position(-2,1), new Position(0,-1), new Position(2,1)}));
		cards.add(new Card("Panda", new Position[] {new Position(-1,-1), new Position(0,1), new Position(1,1)}));
		cards.add(new Card("Bear", new Position[] {new Position(-1,1), new Position(0,1), new Position(1,-1)}));
		cards.add(new Card("Kirin", new Position[] {new Position(-1,2), new Position(0,-2), new Position(1,2)}));
		cards.add(new Card("Sea Snake", new Position[] {new Position(-1,-1), new Position(0,1), new Position(2,0)}));
		cards.add(new Card("Viper", new Position[] {new Position(-2,0), new Position(0,1), new Position(1,-1)}));
		cards.add(new Card("Phoenix", new Position[] {new Position(-2,0), new Position(-1,1), new Position(1,1), new Position(2,0)}));
		cards.add(new Card("Mouse", new Position[] {new Position(-1,-1), new Position(0,1), new Position(1,0)}));
		cards.add(new Card("Rat", new Position[] {new Position(-1,0), new Position(0,1), new Position(1,-1)}));
		cards.add(new Card("Turtle", new Position[] {new Position(-2,0), new Position(-1,-1), new Position(1,-1), new Position(2,0)}));
		cards.add(new Card("Tanuki", new Position[] {new Position(-1,-1), new Position(0,1), new Position(2,1)}));
		cards.add(new Card("Iguana", new Position[] {new Position(-2,1), new Position(0,1), new Position(1,-1)}));
		cards.add(new Card("Sable", new Position[] {new Position(-2,0), new Position(-1,-1), new Position(1,1)}));
		cards.add(new Card("Otter", new Position[] {new Position(-1,1), new Position(1,-1), new Position(2,0)}));
	}
	
	/**
	 * Ad egy pontosan 5 hosszú tömböt a véletlenszerűen választva a tárolt lapok közül.
	 * @return A választott 5 lap.
	 */
	public Card[] getRandomCards(){
		Card[] ret = new Card[5]; //tömb elkészítése.
		Collections.shuffle(cards); //kártyák listájának összekeverése.
		for(int i = 0; i < 5; i++) { //tömb feltöltése.
			ret[i] = cards.get(i);
		}
		return ret;
	}
}
