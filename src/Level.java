import java.util.Iterator;
import java.util.LinkedList;

public class Level {
	private class Wave {
		public int turn;
		public int numZombies;
		
		public Wave(int turn, int numZombies) {
			this.turn = turn;
			this.numZombies = numZombies;
		}
	}
	
	private LinkedList<Wave> waves;
	private Wave current;
	private Iterator<Wave> iterator;
	public Level() {
		waves = new LinkedList<Wave>();
	}
	
	public void addWave(int turn, int numZombies) {
		waves.add(new Wave(turn, numZombies));
	}
	
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
