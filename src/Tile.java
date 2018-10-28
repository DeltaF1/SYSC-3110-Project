/**
 * A spot on a Board that can hold an Entity
 * Check GitHub for authors
 *
 */

public class Tile {
	private Entity occupant;
	
	/**
	 * @return the occupant of the this tile
	 */
	public Entity getOccupant() {
		return occupant;
	}
	
	/**
	 * sets the occupant
	 * @param occupant an entity to put on this tile
	 */
	public void setOccupant(Entity occupant) {
		this.occupant = occupant;
	}
}
