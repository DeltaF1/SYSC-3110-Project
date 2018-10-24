
public class Plant extends Entity {
	private int atkSpd;
	private int cost;
	private int coolDown;
	
	public Plant(String name, int hp, int damage, int atkSpd, int cost) {
		super(name, hp, damage);
		this.atkSpd = atkSpd;
		this.cost = cost;
		this.coolDown = atkSpd;
	}

	public int getAtkSpd() {
		return atkSpd;
	}

	public int getCost() {
		return cost;
	}
	
	public int getCoolDown() {
		return coolDown;
	}
	
	public void setCoolDown(int newCoolDown) {
		coolDown = newCoolDown;
	}

}
