package mainPackage;
/**
 * Entry point of the program: the "Controller" in the so called "Model View Controller" pattern
 * Check GitHub for authors
 */

import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public class Controller {
	private static Board board;
	private static EntityFactory entityFactory;
	private static View view;
	static LinkedList<Level> levels;
	static int level;
	static int levelZombiesLeft;
	public static final int PLACE_AREA_WIDTH = 5; //width of area that a player can place plants
	public static final float SELL_PERCENT = 0.8f;//percent of cost reclaimed when selling a plant
	enum GameState {
		MAINMENU,
		PRELEVEL,
		INLEVEL,
		GAMEOVER,
		WINSCREEN;
	}
	
	static GameState state = GameState.MAINMENU;
	static int turn = 0;
	
	/**
	 * register all command types here
	 * @param commandStr a command to evaluate
	 */
	public static void parseText(String commandStr) {
		if (commandStr == null || commandStr.length() == 0) {
			view.announce("Try actually typing something next time");
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
				view.drawBoard();
			}else if (state == GameState.GAMEOVER) {
				view.drawGameOver();
			}else if (state == GameState.WINSCREEN) {
				view.drawWinScreen();
			}
		}
	}
	
	/*
	 * TODO: call loadLevel first to go to PRELEVEL state and show the player which zombies will be in the level
	 * stats the game
	 */
	public static void startGame() {
		state = GameState.INLEVEL;
		turn = 0;
		view.announce("Level start!");
		view.drawBoard();
	}
	
	/**
	 * places a plant
	 * @param args user specified arguments (plant name and coordinates)
	 */
	private static void placePlant(String[] args) {
		if (args.length < 3) {
			view.announce("To place a plant you need to specify plant name, x coordinate, and y coordinate");
			return;
		}
		String plantName = args[0];
		Plant selectedPlant = entityFactory.makePlant(plantName);
		if (selectedPlant == null) {
			view.announce("\"" + plantName + "\"" + " is not a valid plant name");
			return;
		}
		int[] coords = strCoordsToInt(args[1], args[2]);
		if (coords == null)
			return;
		int x = coords[0]; 
		int y = coords[1];
		if (x >= PLACE_AREA_WIDTH) {
			view.announce(String.format("You can't place a plant beyond x = %d", PLACE_AREA_WIDTH));
		}
		else if (board.getEntity(x, y) == null) {
			int plantCost = selectedPlant.getCost();
			if (board.spendSun(selectedPlant.getCost())) {
				board.placeEntity(x, y, selectedPlant);
				view.announce(String.format("%s placed successfully at (%d, %d)", plantName, x, y));
			} else {
				view.announce(String.format("You can't afford a %s. It costs %d sun points and you have %d!", 
					plantName, plantCost, board.getSun()));
			}
		} else {
			view.announce("There's already something there, put your plant somewhere else.");
		}
	}
	
	/**
	 * Tells a user what's on a specific tile
	 * @param args user specified arguments (tile coordinates)
	 */
	private static void tileInfo(String[] args) {
		if (args.length < 2) {
			view.announce("To see tile info you need to specify an x coordinate and a y coordinate");
			return;
		}
		int[] coords = strCoordsToInt(args[0], args[1]);
		if (coords == null)
			return;
		int x = coords[0]; 
		int y = coords[1];
		Entity tileEntity = board.getEntity(x, y);
		if (tileEntity == null) {
			view.announce("There's nothing on that tile");
		} else if (tileEntity instanceof Plant) {
			Plant p = (Plant) tileEntity;
			String desc = "That tile contains a %s Plant with an hp of %d, damage of %d, and attack speed of %d";
			view.announce(String.format(desc, p.getName(), p.getHp(), p.getDamage(), p.getAtkSpd()));
		} else if (tileEntity instanceof Zombie) {
			Zombie z = (Zombie) tileEntity;
			String desc = "That tile contains a %s Zombie with an hp of %d, damage of %d, and movement speed of %d";
			view.announce(String.format(desc, z.getName(), z.getHp(), z.getDamage(), z.getMovSpd()));
		} else {
			view.announce("The entity on that tile isn't a plant or a zombie, who knows what it is.");
		}
	}
	
	/**
	 * converts two strings to x y coordinates on the board
	 * @param strx x coordinate as a String
	 * @param stry y coordinate as a String
	 * @return null if (strx, stry) is not a valid position on the board, otherwise two integer locations are returned in an int array
	 */
	private static int[] strCoordsToInt(String strx, String stry) {
		int x, y;
		try {
			x = Integer.parseInt(strx);
			y = Integer.parseInt(stry);
			board.checkCoords(x, y);
		} catch (IllegalArgumentException err) { //parseInt and checkCoords throw the same type of error
			view.announce(String.format("invalid board coordinates: (x = %s, y = %s)", strx, stry));
			return null;
		}
		return new int[] {x, y};
	}
	
	/**
	 * ends the current turn
	 * @param args extra arguments specified by player (currently unused)
	 */
	private static void endTurn(String[] args) {
		advancePlants();
		advanceZombies();
		if (state == GameState.INLEVEL) {
			spawnZombies();
		}
		turn++;
	}
	
	
	
	/**
	 * Determine the X position of the closest zombie on the Y row (this must be the zombie hit by plant projectile)
	 * @param y the y position of row we are checking for a zombie
	 */
	private static Integer getHitZombieX(int y) {
		for (int x = 0; x < Board.WIDTH; x++) {
			if(board.getEntity(x,y) instanceof Zombie) {
				return new Integer(x);
			}
		}
		return null;
	}
	
	/**
	 * makes the plants shoot/increase sun
	 */
	private static void advancePlants() {

		for (int y = 0; y < Board.HEIGHT; y++) {
			for (int x = 0; x < PLACE_AREA_WIDTH; x++) {
				Entity occupant = board.getEntity(x,y);
				if( occupant instanceof Plant) {
					Plant plant = (Plant)occupant;
					if (plant instanceof Sunflower) { //pretty trash logic, should have this in the Plant.update() method somehow
						board.addSun(50);
					}
					if (plant.getCoolDown() >= 1) {
						plant.setCoolDown(plant.getCoolDown()-1);
					}else if (plant.getCoolDown() >= 0){
						plant.setCoolDown(plant.getAtkSpd());
						
						view.announce("TEMP MSG: plant at " + Integer.toString(x) + "," + Integer.toString(y) + " shot");
						Integer bulletHit = getHitZombieX(y);
						if (bulletHit != null) {
							Zombie shotZombie = (Zombie) board.getEntity(bulletHit, y);
							int newHP = shotZombie.getHp() - ((Plant)board.getEntity(x,y)).getDamage();
							view.announce("TEMP MSG: plant hit zombie at " + Integer.toString(bulletHit) + "," + Integer.toString(y) + " reducing it's health to " + Integer.toString(newHP));
							if (newHP <= 0) {
								board.removeEntity(bulletHit, y);
								decreaseZombieCount(1);
							}else {
								shotZombie.setHp(newHP);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Complete a turn for an individual zombie
	 * @param x x position of the zombie
	 * @param y y position of the zombie
	 */
	private static void advanceZombie(int x, int y) {
		int newX = x;
		Zombie zombie = (Zombie)board.getEntity(x,y);
		for (int i = 1; i < zombie.getMovSpd() + 1; i ++) {
			
			Entity encounteredEntity;
			if (newX > 1) {
				encounteredEntity = board.getEntity(newX-1, y);
			}else {
				encounteredEntity = null;
			}
			
			//wait in line behind zombie ahead
			if (newX > 0 && encounteredEntity instanceof Zombie ) {
				break;
			//attack plant
			} else if  (newX > 0 && encounteredEntity instanceof Plant ) {
				Plant attacking = (Plant) encounteredEntity;
				int newHP = attacking.getHp() - zombie.getDamage();
				view.announce("TMP MSG: The plant at " + Integer.toString(newX-1) + "," + Integer.toString(y) +" had its health reduced to " + Integer.toString(newHP));
				if (newHP <= 0) {
					board.removeEntity(newX-1, y);
				}else {
					attacking.setHp(newHP);
				}
				break;
			//walk forward
			} else {
				newX = x - 1;
			}
		}
		if (newX != x) {
			if (newX >= 0) {
				board.moveEntity(newX,y,zombie);
			
			}else {
				view.announce("TEMP MSG: a zombie broke through on row " + Integer.toString(y));
				board.removeEntity(x, y);
				//dont drawGameOver() here because it will be erased
				state = GameState.GAMEOVER;
				
				//decreaseZombieCount(1); // probably doesnt matter since this should end game eventually
			}
			
		}
	}
	
	/**
	 * Complete the turn for all currently placed zombies
	 */
	private static void advanceZombies() {		
		
		for (int y = 0; y < board.getTiles().length; y++) {
			for (int x = 0; x <  board.getTiles()[y].length; x++) {
				if( board.getEntity(x, y) instanceof Zombie) {
					advanceZombie(x,y);				}
			}
		}
	}
	
	/**
	 * Handle new zombies spawned this turn
	 */
	private static void spawnZombies() {
		int numZombies = levels.get(level).getWave(turn);
		
		for (int i = 0; i < numZombies; i++) {
			boolean placed = false;
			do {
				int newPosY = ThreadLocalRandom.current().nextInt(0, board.HEIGHT);
				placed = board.placeEntity(board.WIDTH-1,newPosY, new BasicZombie());
			} while (placed == false);
		}
	}

	/**
	 * decreases the count of zombies left alive and takes appropriate action when no zombies are left
	 * @param amnt int the amount to decrease the zombies alive count
	 */
	public static void decreaseZombieCount(int amnt) {
		levelZombiesLeft -= amnt;
		view.announce("TEMP: zombies left this level - " + Integer.toString(levelZombiesLeft));
		if (levelZombiesLeft <= 0){
			level += 1;
			turn = 0;
			if (level >= levels.size() ) {
				view.announce("TEMP: the player beat all levels");
				state = GameState.WINSCREEN;
			}else {
				levelZombiesLeft = levels.get(level).getTotalZombies();
				view.announce("TEMP: the player beat level " + Integer.toString(level));
			}
			
		}
	}
	
	/**
	 * sets the board to be empty, sets money to the starting amount and sets up level data
	 * @param board Board the board the game takes place on
	 */
	public static void setUpGame(Board board) {
		board.wipe();
		board.addSun(150);
		
		//TODO: Load levels from text files
		levels = new LinkedList<Level>();
		
		Level level1 = new Level();
		level1.addWave(0, 1);
		
		Level level2 = new Level();
		level2.addWave(0, 2);
		
		levels.add(level1);
		levels.add(level2);
		
		level = 0;
		turn = 0;
		levelZombiesLeft = level1.getTotalZombies();
	}
	
	public static void main(String[] args) {
		board = new Board();
		
		setUpGame(board);
		
		view = new ASCIIView(board);
		view.drawMenu();
		entityFactory = new EntityFactory();
	}


}
