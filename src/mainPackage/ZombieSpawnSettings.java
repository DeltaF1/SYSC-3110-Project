package mainPackage;

public class ZombieSpawnSettings {
	

	
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String aName) {
		name = aName;
	}
	
	private int spawnTurn;
	public int getSpawnTurn() {
		return spawnTurn;
	}
	public void setSpawnTurn(int aTurn) {
		spawnTurn = aTurn;
	}
	
	ZombieSpawnSettings(String name, int spawnTurn){
		this.name = name;
		this.spawnTurn = spawnTurn;
	}
	
}
