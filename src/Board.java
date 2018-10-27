import java.awt.Point;

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
	
	public Tile getEntityTile(Entity e) {
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				if (tiles[y][x].getOccupant() == e) {
					return tiles[y][x];
				}
			}
		}
		return null;
	}
	
	public boolean moveEntity(int x, int y, Entity e) {
		checkCoords(x, y);
		Tile startTile = getEntityTile(e);
		if (startTile != null && tiles[y][x].getOccupant() == null) {
			startTile.setOccupant(null);
			tiles[y][x].setOccupant(e);
			System.out.println("moved zomb to "+ Integer.toString(x));
			return true;
		}
		System.out.println("failed move");
		return false;
	}
	
	public void removeEntity(int x, int y) {
		checkCoords(x, y);
		tiles[y][x].setOccupant(null);
	}
	
	public Entity getEntity(int x, int y) {
		return getTile(x,y).getOccupant();
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
