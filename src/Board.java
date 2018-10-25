
public class Board {
	
	private Tile[][] tiles;
	public static final int WIDTH = 20;
	public static final int HEIGHT = 10;
	
	public Board() {
		this.tiles = new Tile[HEIGHT][WIDTH];
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				this.tiles[y][x] = new Tile();
			}
		}
	}
	
	public Board(Tile[][] tiles) {
		this();
		setTiles(tiles);
	}
	
	public void setTiles(Tile[][] tiles) {
		checkCoords(tiles[0].length - 1, tiles.length - 1);
		for (int y = 0; y < tiles.length; y++) {
			for (int x = 0; x < tiles[0].length; x++) {
				this.tiles[y][x] = tiles[y][x];
			}
		}
	}
	
	public Tile[][] getTiles() {
		return tiles;
	}
	
	public Tile getTile(int x, int y) {
		checkCoords(x, y);
		return tiles[y][x];
	}
	
	/**
	 * Returns true if entity was placed successfully, false otherwise
	 */
	public boolean placeEntity(int x, int y, Entity e) {
		checkCoords(x, y);
		if (tiles[y][x].getOccupant() == null) {
			tiles[y][x].setOccupant(e);
			return true;
		}
		return false;
	}
	
	public void removeEntity(int x, int y) {
		checkCoords(x, y);
		tiles[y][x].setOccupant(null);
	}
	
	public Entity getEntity(int x, int y) {
		checkCoords(x, y);
		return tiles[y][x].getOccupant();
	}
	
	/**
	 * ensures that x and y are valid coords, throws an error if they're not
	 */
	public void checkCoords(int x, int y) {
		if (x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT) {
			String err = String.format("invalid board coordinates: (x = %d, y = %d)", x, y);
			throw new IllegalArgumentException(err);
		}
	}
}
