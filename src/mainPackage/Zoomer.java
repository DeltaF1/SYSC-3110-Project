package mainPackage;

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
	 * @param hp health of the zombie
	 * @param damage damage zombie does
	 * @param movSpd movement speed of zombie
	 */
	public Boomer(String name) {
		super(name, 60, 20, 2);
	}
	/*
	public ImageIcon getIcon() {
		return Images.zoomerIcon;
	}
 	*/
}
