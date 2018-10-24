
public class Entity {
	protected int hp;
	protected int damage;
	protected String name;
	
	public Entity(String name, int hp, int damage) {
		this.name = name;
		this.hp = hp;
		this.damage = damage;
	}
	
	public String getName() {
		return name;
	}
	
	public int getDamage() {
		return damage;
	}
	
	public int getHp() {
		return hp;
	}
}
