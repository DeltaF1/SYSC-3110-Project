import java.util.Scanner;

public class ASCIIView {
	
	private Board board;
	
	public static String repeat(String str, int times) {
        return new String(new char[times]).replace("\0", str);
    }
	
	private static String rowDivider(int width) {
		return repeat("+---", width) + "+\n";
	}
	
	private String drawBoard(Board board) {
		Tile[][] tiles = board.getTiles();
		
		String out = "";
		
		int width = tiles[0].length;
		int height = tiles.length;
		
		for (int i = 0; i < height; i++) {
			out += rowDivider(width);

			for (int j = 0; j < width; j++) {
				out += "|";
				
				Tile tile = tiles[i][j];
				
				if (tile.getOccupant() == null) {
					out += "   ";
				} else {
					out += entityRepr(tile.getOccupant());
				}
			}
			out += "|\n";
		}
		out += rowDivider(width);
		return out;
	}
	
	public int draw()
	{
		String s = drawBoard(board);
		
		System.out.println(s);
		System.out.print("> ");
		
		return s.length() + 2;
	}
	
	public static String entityRepr(Entity entity) {
		if (entity instanceof Zombie) {
			return "[Z]";
		}
		return "[X]";

	}
	
	
	
	public static void main(String[] args) {
		
		ASCIIView view = new ASCIIView();
		
		Tile[][] tiles = new Tile[2][2];
		
		tiles[0][0] = new Tile();
		tiles[0][1] = new Tile();
		tiles[1][0] = new Tile();
		tiles[1][0].setOccupant(new Entity("test", 0, 0));

		
		tiles[1][1] = new Tile();
		
		Board board = new Board(tiles);
		
		view.board = board;
//		
//		// Move this to a different method? Or should this be in the controller loop?
//		
//		Scanner scanner = new Scanner(System.in);
//		String input;
//		while (true) {
//			view.draw();
//			
//			input = scanner.next();
//			
//			if (input.equals("quit")) { break; }
//		}
//		
//		scanner.close();

		board.placeEntity(1, 19, new Entity("test", 0, 0));
		
		view.drawBoard(board);
		
		board.removeEntity(1, 19);
		
		view.drawBoard(board);
	}
}
