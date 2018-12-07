package mainPackage;

import java.util.LinkedList;
import java.util.TreeMap;

/**
 * a class that represents a level
 * Check GitHub for authors
 */

public class Level {

	TreeMap<Integer, LinkedList<String>> spawns = new TreeMap<Integer, LinkedList<String>>();
	
	/**
	 * add a zombie to the zombies to spawn
	 * @param wave the turn to spawn at
	 * @param zombieType the type of zombie
	 */
	public void addSpawn(int wave, String zombieType) {
		LinkedList<String> zombieTypes = spawns.get(wave);
		if (zombieTypes == null) {
			zombieTypes = new LinkedList<String> ();
		}
		zombieTypes.add(zombieType);
		setSpawn(wave,zombieTypes);
	}
	
	/**
	 * set the entire list of 
	 * @param wave
	 * @param zombieTypes
	 */
	public void setSpawn(int wave, LinkedList<String> zombieTypes) {
		spawns.put( wave, zombieTypes );
	}
	
	/**
	 * remove a zombie that has the given settings
	 * @param wave the wave the zombie should have
	 * @param zombieType the type name the zombie should have
	 */
	public void removeSpawn(int wave, String zombieType) {
		LinkedList<String> zombieTypes = spawns.get(wave);
		for (int i = 0 ; i < zombieTypes.size(); i++) {
			if( zombieTypes.get(i) == zombieType  ) {
				zombieTypes.remove(i);
			}
		}
		setSpawn(wave,zombieTypes);
	}
	
	/**
	 * get the list of types of zombies that spawn during a turn
	 * @param wave
	 * @return the list of type names
	 */
	public LinkedList<String> getSpawn(int wave) {
		return spawns.get(wave);
	}
	
}
