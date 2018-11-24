package mainPackage;

import javax.swing.ImageIcon;

/**
 * A plant that shoots twice as often as peashooters
 * Check GitHub for authors
 *
 */

public class Repeater extends Plant{
	
	/**
	 * creates a new repeater plant
	 */
	public Repeater(String name) {
		super(name, 100, 10, 2, 200);
	}

	public ImageIcon getIcon() {
		return Images.repeaterIcon;
	}
}
