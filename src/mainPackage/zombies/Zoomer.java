package mainPackage.zombies;

import javax.swing.ImageIcon;

/**
 * A class that represents a Zoomer zombie entity
 * Check GitHub for authors
 *
 */

public class Zoomer extends Zombie {
	
	/**
	 * creates a new Zoomer zombie
	 * @param name name of the zombie
	 */
	public Zoomer(String name) {
		super(name, 60, 20, 2);
	}
	
	public ImageIcon getIcon() {
		return Images.zoomerIcon;
	}
 	
}
