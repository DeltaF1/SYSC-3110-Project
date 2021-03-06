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
import java.util.LinkedList;
import java.util.TreeMap;

import javax.swing.*;

import mainPackage.zombies.Zombie;

public class ASCIIView implements View {
	
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
		
		//parseText(text);
	}
	
	/**
	 * register all command types here
	 * @param commandStr a command to evaluate
	 */
	/*
	public void parseText(String commandStr) {
		if (commandStr == null || commandStr.length() == 0) {
			announce("Try actually typing something next time");
			return;
		}
		String[] cmdNameAndArgs = commandStr.split(" ", 2);
		String cmdName = cmdNameAndArgs[0];
		String[] cmdArgs = new String[0];
		if (cmdNameAndArgs.length > 1) {
			cmdArgs = cmdNameAndArgs[1].split(" ");
		}
		
		// Move this into more sub-methods?
		switch (state) {
		case MAINMENU:
			if (cmdName.equals("start")) {
				startGame();
			}
			break;
		case GAMEOVER:
		case WINSCREEN:
			if (cmdName.equals("start")) {
				setUpGame(board);
				startGame();
			}
			break;
		case INLEVEL:	
			switch (cmdName) {
			case "place":
				placePlant(cmdArgs);
				break;
			case "done":
				endTurn(cmdArgs);
				break;
			case "info":
				tileInfo(cmdArgs);
				break;
			default:
				view.announce("\"" + cmdName + "\"" + " isn't even a real command");
			}
			if (state == GameState.INLEVEL) {
				view.drawBoard(board);
			}else if (state == GameState.GAMEOVER) {
				view.drawGameOver();
			}else if (state == GameState.WINSCREEN) {
				view.drawWinScreen();
			}
		}
	}*/
	
	/**
	 * creates a string that divides a row on the view with a specified width
	 * @param width width of the row divider
	 * @return the row divider string
	 */
	private static String rowDivider(int width) {
		return StringUtils.repeat("+---", width) + "+\n";
	}
	
	/**
	 * draws the whole board out of ASCII, complete with indicated sun points
	 */
	public void drawGame() {
		String out = boardRepr(board);
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
		Entity[][] entities = board.getTiles();
		
		String out = "";
		
		int width = entities[0].length;
		int height = entities.length;
		
		for (int i = 0; i < height; i++) {
			out += rowDivider(width);

			for (int j = 0; j < width; j++) {
				out += "|";
				
				Entity tile = entities[i][j];
				
				if (tile == null) {
					out += "   ";
				} else {
					out += entityRepr(tile);
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

	/*
	 * NOTE: The following methods are not implemented fully either because they do not apply to a textview
	 * or because we were not required to maintain our old view type as we added new features to GraphicsView 
	 */
	
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

	@Override
	public void updateEntity(Entity entity, int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateSun(int sun) {
		// TODO Auto-generated method stub
		
	}


	public void updateZombSettings(LinkedList<ZombieSpawnSettings> zombSettings)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateZombSettings(TreeMap<Integer, LinkedList<String>> spawns)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendFailedLevelWriteAlert(String errMsg)
	{
		// TODO Auto-generated method stub
		
	}
}
