import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

import javax.swing.*;

public class ASCIIView {
	
	private Board board;
	private JTextArea textOutput;
	private JTextField textInput;
	
	public ASCIIView (Board board) {
		this.board = board;
		
		JFrame frame = new JFrame("PVZ - ASCII");
		
		Font monoFont = new Font("Courier New", Font.PLAIN, 16);
		
		textOutput = new JTextArea();
		textOutput.setEditable(false);
		textOutput.setFont(monoFont);
		textOutput.setText("\r\n" + 
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
				"");
		
		textInput = new JTextField();
		textInput.setFont(monoFont);
		textInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				handleTextInput();
			}
		});
		
		frame.add(textOutput, BorderLayout.NORTH);
		frame.add(textInput, BorderLayout.SOUTH);
		
		//frame.setSize(500, 500);
		frame.pack();
		
		frame.setVisible(true);
	}
	
	protected void handleTextInput() {
		String text = textInput.getText();
		textInput.setText(null);
		
		//output = Controller.parseText(text);
		//textOutput.append(output);
	}

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
		
		textOutput.setText(s);
		
		return s.length();
	}
	
	public static String entityRepr(Entity entity) {
		if (entity instanceof Zombie) {
			return "[Z]";
		}
		return "[X]";

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
