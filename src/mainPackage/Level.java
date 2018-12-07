package mainPackage;

import java.util.LinkedList;
import java.util.TreeMap;

/**
 * a class that represents a level
 * Check GitHub for authors
 */

public class Level {

	TreeMap<Integer, LinkedList<String>> spawns = new TreeMap<Integer, LinkedList<String>>();
	
	public void addSpawn(int wave, String zombieType) {
		LinkedList<String> zombieTypes = spawns.get(wave);
		if (zombieTypes == null) {
			zombieTypes = new LinkedList<String> ();
		}
		zombieTypes.add(zombieType);
		setSpawn(wave,zombieTypes);
	}
	
	public void setSpawn(int wave, LinkedList<String> zombieTypes) {
		spawns.put( wave, zombieTypes );
	}
	
	public void removeSpawn(int wave, String zombieType) {
		LinkedList<String> zombieTypes = spawns.get(wave);
		for (int i = 0 ; i < zombieTypes.size(); i++) {
			if( zombieTypes.get(i) == zombieType  ) {
				zombieTypes.remove(i);
			}
		}
		setSpawn(wave,zombieTypes);
	}
	
	public LinkedList<String> getSpawn(int wave) {
		return spawns.get(wave);
	}
	
}
