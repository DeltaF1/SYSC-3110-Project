package mainPackage;

import javax.swing.ImageIcon;

public class Images {
	public static final ImageIcon zombieIcon = new ImageIcon("images/zombie.png");
	public static final ImageIcon sunflowerIcon = new ImageIcon("images/sunflower.png");
	public static final ImageIcon peashooterIcon = new ImageIcon("images/peashooter.png");
	public static final ImageIcon blankIcon = new ImageIcon("images/blank.png");
	public static final ImageIcon boomerIcon = new ImageIcon("images/boomer.png"); 
	
	public static ImageIcon getIcon(String entityType) {
		switch(entityType) {
		case "sunflower":
			return sunflowerIcon;
		case "proj":
			return peashooterIcon;
		default:
			return null;
		}
	}
}
