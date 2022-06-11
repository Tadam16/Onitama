package onitama;

import java.awt.Color;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * A nézetben a mezőket reprezentáló objektum.
 *
 */
public class FieldButton extends JButton implements FieldListener{

	/**
	 * A figyelt mező változásakor hívódó függvény.
	 * @param f A mező, amit figyelünk, hogy le tudjuk kérni a szükséges adatait.
	 */
	@Override
	public void change(Field f) {
		if(f.isSelected()) { //Ha ki van választva, megfellő háttérszín beállítása.
			super.setBackground(Color.cyan);
		}
		else if(f.isPossible()) { //Ha lehet rá lépni, megfelelő háttérszín beállítása.
			super.setBackground(Color.orange);
		}
		else { //Ha egyik sem, alapértelmezett háttérszín.
			super.setBackground(Color.white);
		}
		
		Figure fig = f.getFigure(); //Tartalmazott figura lekérése.
		if(fig != null) { //Ha tartalmaz figurát annak kirajzolása. Ehhez ismerni kell a figurát tulajdonló játékos színét (isFirsrt, ha 1 fehér, ha 0 fekete), illetve a figura nevét, majd az ikon megfelelő méretezése következik.
			super.setIcon(new ImageIcon(new ImageIcon("resources" + File.separator + (fig.getOwner().isFirst() ? "white" : "black") 
					+ fig.getName() + ".png").getImage().getScaledInstance(super.getPreferredSize().width, super.getPreferredSize().height, java.awt.Image.SCALE_SMOOTH)));
		}
		else {
			super.setIcon(null); //Ha nincs figura, nullra állítjuk az ikont.
		}
		super.repaint(); //Megjelenítés frissítése.
	}
	
	

}
