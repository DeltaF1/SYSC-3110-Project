package mainPackage.zombies;

import javax.swing.ImageIcon;

import mainPackage.Images;

/**
 * A class that represents a Boomer zombie entity
 * Check GitHub for authors
 *
 */

public class Boomer extends Zombie {
	
	/**
	 * creates a new Boomer zombie
	 * @param name name of the zombie
	 */
	public Boomer(String name) {
		super(name, 120, 20, 1);
	}
	
	public ImageIcon getIcon() {
		return Images.boomerIcon;
	}
}
