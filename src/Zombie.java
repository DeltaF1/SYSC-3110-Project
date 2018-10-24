public class Zombie extends Entity {
	private int movSpd;
	
	public Zombie(String name, int hp, int damage, int movSpd) {
		super(name, hp, damage);
		this.movSpd = movSpd;
	}
	
	public int getMovSpd() {
		return movSpd;
	}

}
