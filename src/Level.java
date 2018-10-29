/**
 * a class that represents a level
 * Check GitHub for authors
 */

import java.util.Iterator;
import java.util.LinkedList;

public class Level {
	private class Wave {
		public int turn;
		public int numZombies;		
		/**
		 * creates a new wave
		 * @param turn turn number for wave
		 * @param numZombies number of zombies in this wave
		 */
		public Wave(int turn, int numZombies) {
			this.turn = turn;
			this.numZombies = numZombies;
		}
	}
	//the "turn" variable represents the turn relative to the start of the level
	//levelStartTurnOffset is the actual Controller turn number this level started on
	//This was need to make getWave work with multiple levels
	//public int levelStartTurnOffset = 0;
	
	private LinkedList<Wave> waves;
	private Wave current;
	private Iterator<Wave> iterator;
	private int totalZombies = 0;
	
	/**
	 * creates a new Level with 0 waves
	 */
	public Level() {
		waves = new LinkedList<Wave>();
	}
	
	/**
	 * adds a wave to the level
	 * @param turn the turn number
	 * @param numZombies the number of zombies on this turn
	 */
	public void addWave(int turn, int numZombies) {
		waves.add(new Wave(turn, numZombies));
		totalZombies += numZombies;
	}
	
	/**
	 * gets the number of zombies that are spawned on a certain turn
	 * @param turn specified turn
	 * @return an integer number of zombies
	 */
	public int getWave(int turn) {
		if (iterator == null) {//initialize iterator if starting level
			iterator = waves.iterator();
			//levelStartTurnOffset = turn;
		}
		
		if (current == null && iterator.hasNext()) {//start wave 1 if starting game
			current = iterator.next(); 
		}
		
		if (current != null) {
			if (turn == current.turn ) { //+ levelStartTurnOffsets
				int numZombies = current.numZombies;
				current = null;
				return numZombies;
			} else {
				// It's not time to spawn more zombies
				return 0;
			}
		} else {
			return -1;
		}
	}
	
	/**
	 * get the total zombies that need to be spawned this round;
	 */
	public int getTotalZombies() {
		return totalZombies;
	}
}
