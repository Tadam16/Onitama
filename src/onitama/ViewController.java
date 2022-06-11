package onitama;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * A megjelenítés egészét összefogó osztály. Felelős a nézetek inicializálásáért, cseréléséért.
 *
 */
public class ViewController extends JFrame{
	/**
	 * A menühöz tartozó nézetet tároló panel
	 */
	private JPanel MenuView;
	/**
	 * A játékhoz tartozó nézetet tároló panel.
	 */
	private JPanel GameView;
	/**
	 * A játék végekor megjelenő nézetet tartalmazó panel.
	 */
	private JPanel GameEndView;
	/**
	 * Az adatmodellben található mezők változásait figyelő listenerek.
	 */
	private FieldListener[][] fieldListeners = new FieldListener[5][5];
	/**
	 * Az adatmodellben található kártyahelyek változásait figyelő listenerek.
	 */
	private CardSlotListener p1Acsl, p1Bcsl, p2Acsl, p2Bcsl, rcsl;
	/**
	 * Az aktuális nézetet reprezentáló állapotváltozó.
	 */
	private Controller.State viewState;
	/**
	 * A megjelenítéshez tartozó Controller objektum.
	 */
	Controller c;
	/**
	 * A mentéshez tartozó menüpont, osztályszintű változó, mert bizonyos esetekben ki kell szürkíteni.
	 */
	private JMenuItem save;
	/**
	 * A győztes játékos nevét kiírő label, a játék vége nézetben. 
	 * Controller állítja setter függvénnyel - ez enyhén ellentmond az MVC architectúrának, azonban ezt is a modell felől implementálni, túl sok felesleges adminisztrációval járna.
	 */
	JLabel winner = new JLabel();
	/**
	 * Az adatmodellben a játékosok változásait figyelő listenerek.
	 */
	private PlayerListener p1l, p2l, actpl;
	
	/**
	 * Az osztály konstruktora, inicializálja a nézeteket, és a nézetek elemeihez tartozó listenereket.
	 * @param c A Kontoller aki számára delegálni kell a változásokat.
	 * @param fal A mezőkhoz tartozó kattintásokat kezelő listenerek 5*5-ös mátrixa. (field action listeners)
	 * @param p1Aal A p1A kártyaslotra való kattintást kezelő listener.
	 * @param p1Bal A p1B kártyaslotra való kattintást kezelő listener.
	 * @param p2Aal A p2A kártyaslotra való kattintást kezelő listener.
	 * @param p2Bal A p2B kártyaslotra való kattintást kezelő listener.
	 */
	public ViewController(Controller c, ActionListener[][] fal, ActionListener p1Aal, ActionListener p1Bal, ActionListener p2Aal, ActionListener p2Bal) {
		//Az ablak, illetve a tagváltozók inicializálása.
		this.c = c;
		super.setTitle("Onitama");
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setResizable(false); //Átméretezhetőség kikapcsolása.
		try {
			super.setIconImage(ImageIO.read(new File("resources" + File.separator + "onitama_icon.png"))); //Ikon beállítása.
		} catch (IOException e) {
			System.out.println("IO hiba"); //Hiba esetén hibaüzenet STDOUT-ra, és terminálás.
			System.exit(-1);
		}
		
		//Nézetek inicializálása.
		MenuViewInit();
		GameViewInit(fal, p1Aal, p1Bal, p2Aal, p2Bal);
		GameEndViewInit();
		
		//Megjelenítési állapot, és kezdő nézet beállítása.
		viewState = Controller.State.menu;
		super.add(MenuView);
		
		//Szabványos menüsáv beállítása.
		menuBarInit();
		
		//Az ablak rendezése, és megjelenítése.
		super.pack();
		super.setVisible(true);
	}
	
	/**
	 * A nézethez tartozó az adatmodellben a kártyahelyeket tartalmazó listenerek listájának lekérése.
	 * @return A listenerek listája, sorrendjük fix: p1A, p1B, p2A, p2B, reserved.
	 */
	public ArrayList<CardSlotListener> getCardSlotListeners() {
		ArrayList<CardSlotListener> l = new ArrayList<CardSlotListener>(); //A visszatérési lista elkészítése, majd feltöltése a megfelelő sorrendben.
		l.add(p1Acsl);
		l.add(p1Bcsl);
		l.add(p2Acsl);
		l.add(p2Bcsl);
		l.add(rcsl);
		return l;
	}
	
	/**
	 * A nézethez tartozó adatmodellben a mezők változásait kezelő listener.
	 * @return A mezőkhöz tartozó listenerek 5*5-ös tömbje. 
	 */
	public FieldListener[][] getFieldListeners(){
		return fieldListeners;
	}
	
	/**
	 * A nézethez tartozó adatmodellben a játékosok változásait figyelő listenerek listájának lekérése.
	 * @return A listenerek listája, sorrendjük fix: 1.játékost figyelő, 2. játékost figyelő, mindkét játékost figyelő.
	 */
	public ArrayList<PlayerListener> getPlayerListeners(){
		ArrayList<PlayerListener> l = new ArrayList<PlayerListener>(); //A visszatérési lista elkészítése, majd feltöltése a megfelelő sorrendben.
		l.add(p1l);
		l.add(p2l);
		l.add(actpl);
		return l;
	}
	
	/**
	 * A nézetben a menü sáv inicializálása.
	 */
	private void menuBarInit() {
		
		JMenuBar menubar = new JMenuBar(); //Menüsáv, és rajta a File opció előkészítése.
		JMenu menu = new JMenu("File");
		super.setJMenuBar(menubar);
		menubar.add(menu);
		
		save = new JMenuItem("save"); //Mentés opció hozzáadása, rajta gombnyomás kezelése.
		save.addActionListener((s) -> c.ButtonAction(Controller.ButtonAction.save));
		save.setActionCommand("SaveGame");
		menu.add(save);
		save.setEnabled(false); //A kezdeti állapotban menü nézetben vagyunk, ezért kikapcsoljuk ezt az opciót.
		
		JMenuItem load = new JMenuItem("load"); //Betöltés opció hozzádaása, rajta gombnyomás kezelése.
		load.addActionListener((s) -> c.ButtonAction(Controller.ButtonAction.load));
		load.setActionCommand("LoadGame");
		menu.add(load);
	}
	
	/**
	 * A megjelenítésben, a menü nézet inicializálása.
	 */
	private void MenuViewInit() {
		
		MenuView = new JPanel(); //A nézetet tartalmazó panel.
		MenuView.setLayout(new BoxLayout(MenuView, BoxLayout.Y_AXIS)); //Layout beállítása. Nem kell speciálisabb layout, mert a sorok pont ugyanolyan hosszúak.
		JLabel p1nl = new JLabel("Player1:"); //A menüben található itemek létrehozása.
		JTextField p1t = new JTextField(20);
		JCheckBox p1cpu = new JCheckBox("CPU");
		JRadioButton p1s = new JRadioButton("Player1 starts");
		p1s.setSelected(true); //Alapból az első játékos kezd.
		JLabel p2nl = new JLabel("Player2:");
		JTextField p2t = new JTextField(20);
		JCheckBox p2cpu = new JCheckBox("CPU");
		JRadioButton p2s = new JRadioButton("Player2 starts");
		
		JLabel errl = new JLabel(" "); //Ha invalid paraméterekkel akarna valaki játékot indítani, itt jelezzük a hibát.
		errl.setForeground(Color.red);
		
		
		JButton newGameButton = new JButton("New Game"); //Új játék gomb hozzáadása, és a nyomásakor történő teendők.
		newGameButton.addActionListener((s)->{
			if(p1t.getText().equals("") || p2t.getText().equals("")) //Mindkét játékosnak kell legyen neve.
				errl.setText("Set name for both players!");
			else if(p1t.getText().equals(p2t.getText())) //A két játékos neve nem egyezhet meg.
				errl.setText("Players name cannot match!");
			else {
				errl.setText(" "); //Ha minden paraméter helyes, az esetleges hibaüzenetek törlése, ha valaki a játék vége után a menübe jutna, már ne lássa, és a játék elindítása.
				c.startGame(p1t.getText(), p1cpu.isSelected(), p1s.isSelected(), p2t.getText(), p2cpu.isSelected(), p2s.isSelected());
			}
			super.pack(); //A szövegek változása miatt kell.
		});
		
		ButtonGroup rb = new ButtonGroup(); //Beállítani a kényszert, hogy csak egy játékos kezdhessen.
		rb.add(p1s);
		rb.add(p2s);
		
		p1cpu.addActionListener((s) -> {if(p2cpu.isSelected()) p2cpu.setSelected(false);}); //Beállítani a kényszert, hogy a játékban legfejjebb egy bot lehessen.
		p2cpu.addActionListener((s) -> {if(p2cpu.isSelected()) p1cpu.setSelected(false);});
		
		JPanel p1row = new JPanel(); //A megfelelő menüsorok létrehozása.
		JPanel p2row = new JPanel();
		JPanel errrow = new JPanel();
		JPanel startrow = new JPanel();
		
		p1row.add(p1nl); //Az menüsorok feltöltése a sorokban találhatü itemekkel.
		p1row.add(p1t);
		p1row.add(p1cpu);
		p1row.add(p1s);
		
		p2row.add(p2nl);
		p2row.add(p2t);
		p2row.add(p2cpu);
		p2row.add(p2s);
		
		errrow.add(errl);
		
		MenuView.add(p1row); //Menüsorok hozzáadása a nézethez.
		MenuView.add(p2row);
		MenuView.add(errrow);
		
		newGameButton.setAlignmentX(CENTER_ALIGNMENT); //Új játékot indító gomb formázása, és hozzáadaása a nézethez.
		newGameButton.setPreferredSize(new Dimension(200,50));
		startrow.add(newGameButton);
		MenuView.add(startrow);
	}
	
	/**
	 * A megjelenítésben a játék nézet inicializálása.
	 * @param fal A mezőkhoz tartozó kattintásokat kezelő listenerek 5*5-ös mátrixa. (field action listeners)
	 * @param p1Aal A p1A kártyaslotra való kattintást kezelő listener.
	 * @param p1Bal A p1B kártyaslotra való kattintást kezelő listener.
	 * @param p2Aal A p2A kártyaslotra való kattintást kezelő listener.
	 * @param p2Bal A p2B kártyaslotra való kattintást kezelő listener.
	 */
	private void GameViewInit(ActionListener[][] fal, ActionListener p1Aal, ActionListener p1Bal, ActionListener p2Aal, ActionListener p2Bal) {
		GameView = new JPanel(); //A játék nézetet tároló panel.
		GameView.setLayout(new BoxLayout(GameView, BoxLayout.X_AXIS)); //A nézet 3 oszlopból fog állni.
		JPanel left = new JPanel(); //Az első oszlop a játékosok neveit, és az aktív játékost tartalmazza.
		JPanel mid = new JPanel(); //A második 2-2 kártyát, valamint a tábla mezőit.
		JPanel right = new JPanel(); //A harmadik pedig az 5. kártyát (reserve).
		GameView.add(left); GameView.add(mid); GameView.add(right);
		left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS)); //Az oszlopokban az elemek függőlegesen helyezkednek el.
		mid.setLayout(new BoxLayout(mid, BoxLayout.Y_AXIS));
		right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
		
		//Első oszlop elemeinek inicializálása, illetve az adatmodellben történő változáskor az akciók kezelése.
		JLabel p1 = new JLabel(), p2 = new JLabel(), actp = new JLabel();
		p1l = (p) -> {
			p1.setText(p.getName()); //Első játékos nevének beállítása.
		};
		
		p2l = (p) -> {
			p2.setText(p.getName()); //Második játékos nevének beállítása.
		};
		
		actpl = (p) -> {
			if(p.isActive()) //Ha bármelyik játékos változásakor aktív lesz, beállítjuk a nevét aktív játékosnak is.
				actp.setText(p.getName() + " moves!");
		};
		//Labelek formázása.
		p1.setPreferredSize(new Dimension(120,50));
		p1.setSize(120, 50);
		p1.setMaximumSize(p1.getSize());
		p2.setPreferredSize(new Dimension(120,50));
		p2.setSize(120, 50);
		p2.setMaximumSize(p2.getSize());
		actp.setPreferredSize(new Dimension(120,50));
		actp.setSize(120, 50);
		actp.setMaximumSize(actp.getSize());
		//Labelek hozzáadása a nézethez.
		left.add(p2); //Felül
		left.add(Box.createVerticalGlue());
		left.add(actp); //Középen
		left.add(Box.createVerticalGlue());
		left.add(p1); //Alul
		left.setBorder(new EmptyBorder(100,50,100,50)); //Padding beállítása az esztétikum érdekében.
		
		//Középső oszlop inicializálása.
		JPanel midtop = new JPanel(); //Az oszlop tetején levő két kártyahely tárolója.
		mid.add(midtop);
		
		p2Acsl = CardButtonFactory(midtop, p2Aal, -1); //Kártyahelyek elkészítése.
		p2Bcsl = CardButtonFactory(midtop, p2Bal, -1);
		
		fieldFactory(mid, fal); //Mezők hozzáadása a nézethez.
		
		JPanel midbottom = new JPanel(); //Az oszlop alján levő két kártyahely tárolója.
		mid.add(midbottom);
		
		p1Acsl = CardButtonFactory(midbottom, p1Aal, 1); //Kártyahelyek elkészítése.
		p1Bcsl = CardButtonFactory(midbottom, p1Bal, 1);
		
		//Jobb oldali oszlop inicializálása.
		right.setBorder(new EmptyBorder(5,5,5,5)); //Padding beállítása.
		rcsl = CardButtonFactory(right, null, 1); //Kártyaslot hozzáadása. (null a tartozó action listener, mert a rajta való kattintásokkal nem foglakozunk.)
	}
	
	/**
	 * A megjelenítésben a játék vége nézet inicializálása.
	 */
	private void GameEndViewInit() {
		GameEndView = new JPanel(); //A játék vége nézetet tároló panel.
		GameEndView.setLayout(new BoxLayout(GameEndView, BoxLayout.Y_AXIS));
		GameEndView.add(winner); //Győztest kiírő label hozzádaása.
		GameEndView.add(Box.createVerticalStrut(30)); //Térköz a label és gomb között.
		winner.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18)); //Új betűméret.
		winner.setAlignmentX(CENTER_ALIGNMENT); //Középre állítás.
		JButton backtomenu = new JButton("Back to menu"); //Játék vége gomb elkészítése, formázása, és akcióinak kezelése.
		backtomenu.setActionCommand("BackToMenu");
		backtomenu.addActionListener((s) -> c.ButtonAction(Controller.ButtonAction.toMenu));
		backtomenu.setPreferredSize(new Dimension(200, 50));
		backtomenu.setSize(200, 50);
		backtomenu.setMaximumSize(backtomenu.getSize());
		backtomenu.setAlignmentX(CENTER_ALIGNMENT);
		GameEndView.add(backtomenu); //Gomb hozzáadása a nézethez.
		GameEndView.setBorder(new EmptyBorder(30,30,30,30)); //Padding.
	}
	
	/**
	 * A játék vége nézetben a győztes nevének beállítása.
	 * @param s A győztes játékos neve.
	 */
	public void setWinnerName(String s) {
		winner.setText(s + " wins the game!"); //Beállítja a győztés nevét.
		super.pack(); //Megjelenítés újrarendezése.
	}
	
	/**
	 * A megjelenítésen belüli nézet változtatása.
	 * @param s A nézet, amit aktiválni szeretnénk.
	 */
	public void ChangeView(Controller.State s) {
		switch(viewState) { //A jelenlegi nézet kivétele a megjelenítésből,
			case menu:
				super.remove(MenuView);
				break;
			case game:
				super.remove(GameView);
				break;
			case endgame:
				super.remove(GameEndView);
				break;
		}
		viewState = s;
		switch(s) { //Az új nézet beállítása, mentés menüpont állapotának frissítése.
			case menu:
				super.add(MenuView);
				save.setEnabled(false);
				break;
			case game:
				super.add(GameView);
				save.setEnabled(true);
				break;
			case endgame:
				super.add(GameEndView);
				save.setEnabled(false);
				break;
		}
		super.pack(); //Megjelenítés rendezése.
	}
	
	/**
	 * A kártyahelyek kirajzolása a paraméterként kapott panelbe.
	 * @param j A panel, amibe a kártyaslotot tenni szeretnénk.
	 * @param al A kártyára való kattintást figyelő listener.
	 * @param direction A kártya iránya. Ha a kártya a tábla alján van 1, egyébként -1. (Fejjel felfelé, vagy lefelé jelenítsük-e meg a kártyát.)
	 * (Nem túl elegáns, azonban a kártyákat, és bábuk lépéseit kezelő utasítások során meglehetősen kényelmes megoldás.)
	 * @return A kreált gombra mutató referencia.
	 */
	private CardButton CardButtonFactory(JPanel j, ActionListener al, int direction) {
		CardButton ret = new CardButton(direction); //Kártyahely inicializálása.
		JPanel cont = new JPanel(); //A kártyahely tárolójának inicializálása.
		cont.setLayout(new BoxLayout(cont, BoxLayout.Y_AXIS));
		j.add(cont);
		ret.setPreferredSize(new Dimension(200,200)); //Kártyahely formázása.
		ret.setSize(200, 200);
		ret.setMaximumSize(ret.getSize());
		ret.addActionListener(al); 
		JLabel l = new JLabel(); //Kártyahelyben tárolt kártya nevét kiírő label.
		ret.addChangeListener((s)->{ //Ha a kártyahely változik frissítjük a tartalmát.
			String txt = ret.getName();
			if(txt == null)
				l.setText(" ");
			else
				l.setText(txt);
			l.revalidate();
			l.repaint();
		});
		cont.add(ret); //Kártyaslot gomb, és a név hozzáadása a tárolóhoz.
		cont.add(l);
		return ret; //Az elkészített kártyaslot visszaadása. (CardSlotListenerként még szükség lesz rá.)
	}
	
	/**
	 * A mezők kirajzolása a kapott panelbe.
	 * @param l A panel, amelybe a mezők gridjét tenni szeretnénk.
	 * @param fal A  mezőkre való kattintásokat kezelő listenerek tömbje.
	 */
	private void fieldFactory(JPanel l, ActionListener[][] fal) {
		JPanel p = new JPanel(); //Mezőket tároló.
		p.setLayout(new GridLayout(5,5)); //layout beállítása.
		l.add(p);
		for(int y = 0; y < 5; y++) {
			for(int x = 0; x < 5; x++) {
				FieldButton f = new FieldButton(); //Mező létrehozása.
				f.setPreferredSize(new Dimension(100, 100)); //Méretezés.
				f.addActionListener(fal[x][y]); //Eseménykezelő beállítása.
				p.add(f); //Mező hozzáadása a tárolóhoz.
				fieldListeners[x][y] = f; //FieldListenerek tömbjének frissítése.
			}
		}
	}
}
