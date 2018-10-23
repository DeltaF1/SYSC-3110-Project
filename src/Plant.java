
public class Plant extends Entity {
	private int atkSpd;
	private int cost;
	
	public Plant(String name, int hp, int damage, int atkSpd, int cost) {
		super(name, hp, damage);
		this.atkSpd = atkSpd;
		this.cost = cost;
	}

	public int getAtkSpd() {
		return atkSpd;
	}

	public int getCost() {
		return cost;
	}

}
