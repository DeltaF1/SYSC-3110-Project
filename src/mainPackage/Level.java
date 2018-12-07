package mainPackage;

import java.util.LinkedList;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
		if (zombieTypes != null) {
			zombieTypes.remove(zombieType);
		}
	}
	
	public int getTotalZombies() {
		int amnt = 0;
		for (int wave: spawns.keySet()) {
			amnt += spawns.get(wave).size();
		}
		return amnt;
	}
	
	/**
	 * get the list of types of zombies that spawn during a turn
	 * @param wave
	 * @return the list of type names
	 */
	public LinkedList<String> getSpawn(int wave) {
		return spawns.get(wave);
	}
	
	private int current = 0;
	public void setCurrent(int num) {
		current = num;
	}
	
	public int getCurrent() {
		return current;
	}

	/**
	 * converts this Level to XML
	 * @return an XML string representing this Level
	 */
	public String toXML() {
		try {
			Document xml  = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element rootElm = xml.createElement("level");
			Element allWavesElm = xml.createElement("allWaves");
			Element currentWaveElm = xml.createElement("currentWave");
			Element totalZombieElm = xml.createElement("totalZombies");
			for (int wave: spawns.keySet()) {
				Element waveElm = xml.createElement("wave");
				Element zombTypesElm = xml.createElement("zombieTypes");
				for ( String zombType:spawns.get(wave) ) {
					Element zombTypeElm = xml.createElement("zombieType");
					zombTypeElm.appendChild(xml.createTextNode( zombType ));
					zombTypesElm.appendChild(zombTypeElm);
				}
				Element turnElm = xml.createElement("turn");
				turnElm.appendChild(xml.createTextNode(String.format("%d", wave)));
				waveElm.appendChild(turnElm);
				waveElm.appendChild(zombTypesElm);
				allWavesElm.appendChild(waveElm);
			}
			currentWaveElm.appendChild(xml.createTextNode(String.format("%d", current)));
			totalZombieElm.appendChild(xml.createTextNode(String.format("%d", getTotalZombies())));
			rootElm.appendChild(allWavesElm);
			rootElm.appendChild(currentWaveElm);
			rootElm.appendChild(totalZombieElm);
			xml.appendChild(rootElm);
			return StringUtils.XMLToString(xml);
		} catch (Exception e) {
			e.printStackTrace();
	    	return null;
	    }
	}
	
}
