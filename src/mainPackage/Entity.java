package mainPackage;

import javax.swing.Icon;

/**
 * Represents an entity (either a zombie or a plant)
 * Check GitHub for authors
 */

public class Entity {
	protected int hp;
	protected int damage;
	protected String name;
	
	/**
	 * creates a new entity
	 * @param name name of the entity
	 * @param hp health of the entity
	 * @param damage amount of damage the entity does
	 */
	public Entity(String name, int hp, int damage) {
		this.name = name;
		this.hp = hp;
		this.damage = damage;
	}
	
	/**
	 * @return name of entity
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return current hp of entity
	 */
	public int getHp() {
		return hp;
	}
	
	/**
	 * sets hp
	 * @param amount of hp this entity should have
	 */
	public void setHp(int hp) {
		this.hp = hp;
	}
	
	/**
	 * @return the amount of damage this entity does
	 */
	public int getDamage() {
		return damage;
	}

	public Icon getIcon() {
		// TODO Auto-generated method stub
		return null;
	}

}
