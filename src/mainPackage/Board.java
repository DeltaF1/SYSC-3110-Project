package mainPackage;
/**
 * A class that represents a board that the plants and zombies move on
 * Check GitHub for authors
 */

public class Board {
	
	private Tile[][] tiles;
	public static final int WIDTH = 20;
	public static final int HEIGHT = 10;
	private int sunPoints;
	
	/**
	 * Creates a new board
	 */
	public Board() {
		this.tiles = new Tile[HEIGHT][WIDTH];
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				this.tiles[y][x] = new Tile();
			}
		}
		sunPoints = 0;
	}
	
	/**
	 * creates a new board from a 2d array of Tiles
	 * @param tiles Tiles to make the board out of
	 */
	public Board(Tile[][] tiles) {
		this();
		setTiles(tiles);
	}
	
	/**
	 * Sets tiles on the board to a 2d array of tiles
	 * if the array is smaller then the board, only some of the board will be filled
	 * @param tiles the tiles to fill the board with
	 */
	public void setTiles(Tile[][] tiles) {
		checkCoords(tiles[0].length - 1, tiles.length - 1);
		for (int y = 0; y < tiles.length; y++) {
			for (int x = 0; x < tiles[0].length; x++) {
				this.tiles[y][x] = tiles[y][x];
			}
		}
	}
	
	/**
	 * @return all the tiles
	 */
	public Tile[][] getTiles() {
		return tiles;
	}
	
	/**
	 * @param x x location of the tile
	 * @param y y location of the tile
	 * @return the tile located at (x, y) on the board
	 */
	public Tile getTile(int x, int y) {
		checkCoords(x, y);
		return tiles[y][x];
	}
	
	/**
	 * @return true if entity was placed successfully, false otherwise
	 */
	public boolean placeEntity(int x, int y, Entity e) {
		checkCoords(x, y);
		if (tiles[y][x].getOccupant() == null) {
			tiles[y][x].setOccupant(e);
			return true;
		}
		return false;
	}
	
	/**
	 * gets the tile with the specified entity on it
	 * @param e the entity to search for
	 * @return null if entity is not found, otherwise returns the Tile the entity is on
	 */
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
	
	/**
	 * moves the specified entity to the specified x, y coordinates
	 * @param x x coordinate to move e to
	 * @param y y coordinate to move e to
	 * @param e the entity to move
	 * @return true if entity was moved succesfully, false otherwise
	 */
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
	
	/**
	 * removes the entity at the specified location
	 * @param x x coordinate of the entity to remove
	 * @param y y coordinate of the entity to remove
	 */
	public void removeEntity(int x, int y) {
		checkCoords(x, y);
		tiles[y][x].setOccupant(null);
	}
	
	/**
	 * gets the entity at specified location
	 * @param x the x coordinate of the entity to remove
	 * @param y the y coordinate of the entity to remove
	 * @return the entity at (x, y)
	 */
	public Entity getEntity(int x, int y) {
		return getTile(x,y).getOccupant();
	}
	
	/**
	 * ensures that x and y are valid coords, throws an error if they're not
	 * @param x x coord to check
	 * @param y y coord to check
	 */
	public void checkCoords(int x, int y) {
		if (x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT) {
			String err = String.format("invalid board coordinates: (x = %d, y = %d)", x, y);
			throw new IllegalArgumentException(err);
		}
	}
	
	/**
	 * @return the current sunpoints on the board
	 */
	public int getSun() {
		return sunPoints;
	}
	
	/**
	 * tries to spend some sun points
	 * @param cost the amount of sun points to spend
	 * @return true if sunpoints were succesfully spent, false otherwise (insufficient funds)
	 */
	public boolean spendSun(int cost) {
		if (cost <= sunPoints) {
			sunPoints -= cost;
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * adds sunpoints to board
	 * @param sun amount of sunpoints to add
	 */
	public void addSun(int sun) {
		sunPoints += sun;
	}
	
	public void wipe() {
		this.tiles = new Tile[HEIGHT][WIDTH];
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				this.tiles[y][x] = new Tile();
			}
		}
		sunPoints = 0;
	}
}
