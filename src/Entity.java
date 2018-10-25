
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
	
	public int getHp() {
		return hp;
	}
	
	public void setHp(int hp) {
		this.hp = hp;
	}
	
	public int getDamage() {
		return damage;
	}

}
