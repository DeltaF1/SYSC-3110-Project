package mainPackage.plants;

import javax.swing.ImageIcon;

import mainPackage.Images;

/**
 * A plant that shoots
 * Check GitHub for authors
 *
 */

public class ProjectilePlant extends Plant{
	
	/**
	 * creates a new projectile plant
	 */
	public ProjectilePlant(String name) {
		super(name, 100, 10, 1, 50);
	}
	
	public ImageIcon getIcon() {
		return Images.peashooterIcon;
	}
	
}
