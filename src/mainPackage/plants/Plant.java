package mainPackage.plants;

import mainPackage.Entity;

/**
 * A class that represents a plant
 * Check GitHub for authors
 */

public class Plant extends Entity {
	private int atkSpd;
	private int cost;
	private int coolDown;
	
	/**
	 * creates a new plant
	 * @param name name of the plant
	 * @param hp hp of the plant
	 * @param damage damage of the plant
	 * @param atkSpd attack speed of the plant
	 * @param cost cost of the plant
	 */
	public Plant(String name, int hp, int damage, int atkSpd, int cost) {
		super(name, hp, damage);
		this.atkSpd = atkSpd;
		this.cost = cost;
		this.coolDown = atkSpd;
	}

	/**
	 * @return attack speed of the plant
	 */
	public int getAtkSpd() {
		return atkSpd;
	}

	/**
	 * @return cost of the plant
	 */
	public int getCost() {
		return cost;
	}
	
	/**
	 * @return cooldown of the plant
	 */
	public int getCoolDown() {
		return coolDown;
	}
	
	/**
	 * sets cooldown of the plant
	 * @param newCoolDown new cooldown
	 */
	public void setCoolDown(int newCoolDown) {
		coolDown = newCoolDown;
	}

}
