package mainPackage;

import javax.swing.ImageIcon;

/**
 * A class that represents a zombie entity
 * Check GitHub for authors
 *
 */

public class Zombie extends Entity {
	private int movSpd;
	
	/**
	 * creates a new zombie
	 * @param name name of the zombie
	 * @param hp health of the zombie
	 * @param damage damage zombie does
	 * @param movSpd movement speed of zombie
	 */
	public Zombie(String name, int hp, int damage, int movSpd) {
		super(name, hp, damage);
		this.movSpd = movSpd;
	}
	
	/**
	 * @return movement speed
	 */
	public int getMovSpd() {
		return movSpd;
	}
	
	public ImageIcon getIcon() {
		return Images.zombieIcon;
	}
}
