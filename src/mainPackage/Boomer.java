package mainPackage;

import javax.swing.ImageIcon;

/**
 * A class that represents a Boomer zombie entity
 * Check GitHub for authors
 *
 */

public class Boomer extends Zombie {
	
	/**
	 * creates a new Boomer zombie
	 * @param name name of the zombie
	 * @param hp health of the zombie
	 * @param damage damage zombie does
	 * @param movSpd movement speed of zombie
	 */
	public Boomer(String name) {
		super(name, 120, 20, 1);
	}
	
	public ImageIcon getIcon() {
		return Images.boomerIcon;
	}
}
