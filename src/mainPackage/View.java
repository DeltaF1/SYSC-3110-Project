package mainPackage;

import java.util.LinkedList;
import java.util.TreeMap;

import javax.swing.ListModel;

public interface View {
	
	public void drawGame();
	
	public void announce(String message);
	
	public void drawMenu();
	
	public void drawGameOver();
	
	public void drawWinScreen();
	
	public void updateEntity(Entity entity, int x, int y);
	
	public void updateSun(int sun);

	public void updateZombSettings(TreeMap<Integer, LinkedList<String>> spawns);
	//public void updateZombSettings(LinkedList<ZombieSpawnSettings> zombSettings);
}
