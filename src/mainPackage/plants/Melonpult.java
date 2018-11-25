package mainPackage.plants;

import javax.swing.ImageIcon;

import mainPackage.Images;

/**
 * A heavy hitting plant thats costly but powerful
 * Check GitHub for authors
 *
 */

public class Melonpult extends Plant{
	
	/**
	 * creates a new MelonPult plant
	 */
	public Melonpult(String name) {
		super(name, 100, 30, 1, 300);
	}

	public ImageIcon getIcon() {
		return Images.melonIcon;
	}

}
