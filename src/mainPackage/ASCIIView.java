package mainPackage;
/**
 * A view for the game made with ascii graphics
 * Check GitHub for authors
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class ASCIIView {
	
	private Board board;
	private JFrame frame;
	private JTextArea textOutput;
	private JTextField textInput;
	private JTextArea eventLog;
	
	private static String splash = "\r\n" + 
			"          ___    __                          _                           \r\n" + 
			"         F _ \",  LJ     ___ _     _ ___     FJ_       ____               \r\n" + 
			"        J `-' |  FJ    F __` L   J '__ J   J  _|     F ___J              \r\n" + 
			"        |  __/F J  L  | |--| |   | |__| |  | |-'    | '----_             \r\n" + 
			"        F |__/  J  L  F L__J J   F L  J J  F |__-.  )-____  L            \r\n" + 
			"       J__|     J__L J\\____,__L J__L  J__L \\_____/ J\\______/F            \r\n" + 
			"       |__L     |__|  J____,__F |__L  J__| J_____F  J______F             \r\n" + 
			"                                                                         \r\n" + 
			"                                                                         \r\n" + 
			"                          _    _     ____                                \r\n" + 
			"                         J |  | L   F ___J                               \r\n" + 
			"                         J J  F L  | '----_                              \r\n" + 
			"                         J\\ \\/ /F  )-____  L  __                         \r\n" + 
			"                          \\\\__//  J\\______/F J__L                        \r\n" + 
			"                           \\__/    J______F  |__|                        \r\n" + 
			"                                                                         \r\n" + 
			"    ____                               _        __                       \r\n" + 
			"   [__  '.     ____      _ _____      FJ___     LJ     ____       ____   \r\n" + 
			"   `--7 .'    F __ J    J '_  _ `,   J  __ J          F __ J     F ___J  \r\n" + 
			"    .'.'.'   | |--| |   | |_||_| |   | |--| |   FJ   | _____J   | '----_ \r\n" + 
			"  .' (_(__   F L__J J   F L LJ J J   F L__J J  J  L  F L___--.  )-____  L\r\n" + 
			" J________L J\\______/F J__L LJ J__L J__,____/L J__L J\\______/F J\\______/F\r\n" + 
			" |________|  J______F  |__L LJ J__| J__,____F  |__|  J______F   J______F \r\n" + 
			"                                                                         \r\n" + 
			"";
	
	/**
	 * creates a new ASCIIView
	 * @param board the board to use in this view
	 */
	public ASCIIView (Board board) {
		this.board = board;
		
		frame = new JFrame("PVZ - ASCII");
		
		Font monoFont = new Font("Courier New", Font.PLAIN, 16);
		
		textOutput = new JTextArea();
		textOutput.setEditable(false);
		textOutput.setFont(monoFont);
		
		eventLog = new JTextArea();
		eventLog.setEditable(false);
		eventLog.setFont(monoFont);
		JScrollPane eventLogPane = new JScrollPane(eventLog);
		
		// Trick to keep the scrollpane at a max height from Alexander @ https://stackoverflow.com/questions/41629778/fix-height-of-jscrollpane
		eventLogPane.setMaximumSize(new Dimension(1000, 100));
		eventLogPane.setPreferredSize(new Dimension(0, 100));
		
		textInput = new JTextField();
		textInput.setFont(monoFont);
		textInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				handleTextInput();
			}
		});
		
		frame.add(textOutput, BorderLayout.NORTH);
		frame.add(textInput, BorderLayout.SOUTH);
		frame.add(eventLogPane, BorderLayout.CENTER);
		
		//frame.setSize(500, 500);
		frame.pack();
		
		frame.setVisible(true);
	}
	
	/**
	 * Receives text from a user and sends it to the controller to be parsed
	 */
	protected void handleTextInput() {
		String text = textInput.getText();
		textInput.setText(null);
		
		Controller.parseText(text);
	}
	
	/**
	 * repeats a string a certain number of times
	 * @param str String to repeat
	 * @param times times to repeat the string
	 * @return
	 */
	public static String repeat(String str, int times) {
        return new String(new char[times]).replace("\0", str);
    }
	
	/**
	 * creates a string that divides a row on the view with a specified width
	 * @param width width of the row divider
	 * @return the row divider string
	 */
	private static String rowDivider(int width) {
		return repeat("+---", width) + "+\n";
	}
	
	/**
	 * draws the whole board out of ASCII, complete with indicated sun points
	 */
	public void drawBoard() {
		String out = boardRepr(this.board);
		out += "Sun: " + board.getSun() +"\n";
		textOutput.setText(out);
		frame.pack();
	}
	
	/**
	 * Converts a board to it's ASCII representation
	 * @param board board to convert to ASCII
	 * @return an ASCII representation of the board
	 */
	private static String boardRepr(Board board) {
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
	
	/**
	 * creates an ASCII representation of an entity
	 * @param entity entity to make an ASCII representation of
	 * @return the ASCII representation of the entity
	 */
	public static String entityRepr(Entity entity) {
		if (entity instanceof Zombie) {
			return "[Z]";
		}
		return "[X]";

	}
	
	/**
	 * Tells the player something
	 * @param message information to tell the player
	 */
	public void announce(String message) {
		if (message == null) {
			return;
		}
		eventLog.append(message + "\r\n");
		eventLog.setCaretPosition(eventLog.getText().length());
	}

	/**
	 * Draws the start menu
	 */
	public void drawMenu() {
		// TODO Auto-generated method stub
		textOutput.setText(splash);
		frame.pack();
		announce("Type 'start' to begin the game. To place a plant type 'place <name> x y'. When you're satisfied with your turn type 'done'");
	}
	
	/**
	 * Draws the game over screen
	 */
	public void drawGameOver() {
		textOutput.setText("          ___    __                          _                           \r\n Game over ascii art or something \n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n ");
		frame.pack();
		announce("Type 'start' to start a new game. To place a plant type 'place <name> x y'. When you're satisfied with your turn type 'done'");

	}
	
	/**
	 * Draws the win screen
	 */
	public void drawWinScreen() {
		textOutput.setText("          ___    __                          _                           \r\nWin ascii art or something \n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n ");
		frame.pack();
		announce("Type 'start' to start a new game. To place a plant type 'place <name> x y'. When you're satisfied with your turn type 'done'");

	}
	
	
/*	old main replaced with Controller's Main
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
		
		System.out.println(view.drawBoard(board));
		//view.draw();
		
		board.removeEntity(1, 19);
		//view.draw();
		System.out.println(view.drawBoard(board));
	}*/
}
