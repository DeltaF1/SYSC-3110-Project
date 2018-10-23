
public class ASCIIView {
	public static String repeat(String str, int times) {
        return new String(new char[times]).replace("\0", str);
    }
	
	public static void drawBoard(Board board) {
		Tile[][] tiles = board.getTiles();
		
		String out = "";
		
		int width = tiles[0].length;
		int height = tiles.length;
		
		for (int i = 0; i < height; i++) {
			out += repeat("+--", width) + "+\n";
			for (int j = 0; j < width; j++) {
				out += "|";
				
				Tile tile = tiles[i][j];
				
				if (tile.getOccupant() == null) {
					out += "  ";
				} else {
					out += entityRepr(tile.getOccupant());
				}
			}
			out += "|\n";
		}
		
		System.out.println(out);
	}
	
	public static String entityRepr(Entity entity) {
		return ")(";
	}
	
	public static void main(String[] args) {
		Tile[][] tiles = new Tile[2][2];
		
		tiles[0][0] = new Tile();
		tiles[0][1] = new Tile();
		tiles[1][0] = new Tile();
		tiles[1][0].setOccupant(new Entity("test", 0, 0));
		
		
		tiles[1][1] = new Tile();
		
		Board board = new Board(tiles);
		
		board.placeEntity(1, 19, new Entity("test", 0, 0));
		
		drawBoard(board);
		
		board.removeEntity(1, 19);
		
		drawBoard(board);
	}
}
