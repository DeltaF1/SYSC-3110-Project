package mainPackage.zombies;

import javax.swing.ImageIcon;

import mainPackage.Images;

/**
 * A class that represents a Doomer zombie entity
 * Check GitHub for authors
 *
 */

public class Doomer extends Zombie {
	
	/**
	 * creates a new Doomer zombie
	 * @param name name of the zombie
	 */
	public Doomer(String name) {
		super(name, 60, 40, 1);
	}
	
	public ImageIcon getIcon() {
		return Images.doomerIcon;
	}
  
}
