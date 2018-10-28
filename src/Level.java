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
	
	private LinkedList<Wave> waves;
	private Wave current;
	private Iterator<Wave> iterator;
	
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
	}
	
	/**
	 * gets the number of zombies that are spawned on a certain turn
	 * @param turn specified turn
	 * @return an integer number of zombies
	 */
	public int getWave(int turn) {
		if (iterator == null) {
			iterator = waves.iterator();
		}
		
		if (current == null && iterator.hasNext()) {
			current = iterator.next(); 
		}
		
		if (current != null) {
			if (turn == current.turn) {
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
}
