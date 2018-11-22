package mainPackage;

import javax.swing.ImageIcon;

public class Images {
	public static ImageIcon zombieIcon = new ImageIcon("images/zombie.png");
	public static ImageIcon sunflowerIcon = new ImageIcon("images/sunflower.png");
	public static ImageIcon peashooterIcon = new ImageIcon("images/peashooter.png");
	public static ImageIcon blankIcon = new ImageIcon("images/blank.png");
	
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
