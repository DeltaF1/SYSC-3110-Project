
public class Board {
	
	private Tile[][] tiles;
	public static final int WIDTH = 20;
	public static final int HEIGHT = 20;
	
	public Board() {
		tiles = new Tile[WIDTH][HEIGHT];
	}
	
	public Board(Tile[][] tiles) {
		this.tiles = tiles;
	}
	
	public Tile[][] getTiles() {
		return tiles;
	}
	
	/**
	 * Returns true if entity was placed successfully, false otherwise
	 */
	public boolean placeEntity(int x, int y, Entity e) {
		checkCoords(x, y);
		if (tiles[x][y].getOccupant() == null) {
			tiles[x][y].setOccupant(e);
			return true;
		}
		return false;
	}
	
	public void removeEntity(int x, int y) {
		checkCoords(x, y);
		tiles[x][y].setOccupant(null);
	}
	
	/**
	 * ensures that x and y are valid coords, throws an error if they're not
	 */
	private void checkCoords(int x, int y) {
		if (x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT) {
			String err = String.format("invalid board coordinates (x = %d, y = %d)", x, y);
			throw new IllegalArgumentException(err);
		}
	}
}
