package mainPackage.plants;

import javax.swing.ImageIcon;

import mainPackage.Images;

/**
* a plant that creates sun points every turn
 * Check GitHub for authors
 */

public class Sunflower extends Plant{
	
	/**
	 * creates a new sunflower
	 */
	public Sunflower(String name) {
		super(name, 100, 0, 0, 150);
	}
	
	public ImageIcon getIcon() {
		return Images.sunflowerIcon;
	}
}
