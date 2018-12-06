package mainPackage;

import java.util.LinkedList;

public interface View {
	
	public void drawGame();
	
	public void announce(String message);
	
	public void drawMenu();
	
	public void drawGameOver();
	
	public void drawWinScreen();
	
	public void updateEntity(Entity entity, int x, int y);
	
	public void updateSun(int sun);

	public void updateZombSettings(LinkedList<ZombieSpawnSettings> zombSettings);
	
}
