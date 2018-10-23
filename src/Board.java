
public class Board {
	private Tile[][] tiles;
	
	public Board() {
		tiles = new Tile[20][20];
	}
	
	public Board(Tile[][] tiles) {
		this.tiles = tiles;
	}
	
	public Tile[][] getTiles() {
		return tiles;
	}
}
