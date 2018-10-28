import java.util.concurrent.ThreadLocalRandom;

public class Controller {
	private static Board board;
	private static EntityFactory entityFactory;
	private static ASCIIView view;
	static Level level;
	public static final int PLACE_AREA_WIDTH = 5; //width of area that a player can place plants
	public static final float SELL_PERCENT = 0.8f;//percent of cost reclaimed when selling a plant
	enum GameState {
		MAINMENU,
		PRELEVEL,
		INLEVEL;
	}
	
	static GameState state = GameState.MAINMENU;
	static int turn = 0;
	
	/**
	 * register all command types here
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
			view.drawBoard();
		}
	}
	
	/*
	 * TODO: call loadLevel first to go to PRELEVEL state and show the player which zombies will be in the level
	 */
	public static void startGame() {
		state = GameState.INLEVEL;
		turn = 0;
		view.announce("Level start!");
		view.drawBoard();
	}
	
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
		if (board.getEntity(x, y) == null) {
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
	
	private static void endTurn(String[] args) {
		advancePlants();
		advanceZombies();
		spawnZombies();
		turn++;
	}
	
	
	
	/**
	 * Determine the X position of the closest zombie on the Y row (this must be the zombie hit by plant projectile)
	 */
	private static Integer getHitZombieX(int y) {
		for (int x = 0; x < Board.WIDTH; x++) {
			if(board.getEntity(x,y) instanceof Zombie) {
				return new Integer(x);
			}
		}
		return null;
	}
	
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
							view.announce("TEMP MSG: plant hit zombie at " + Integer.toString(bulletHit) + "," + Integer.toString(y) + " reducing it's health to " + Integer.toString(shotZombie.getHp()));
							if (newHP <= 0) {
								board.removeEntity(bulletHit, y);
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
	 */
	private static void advanceZombie(int x, int y) {
		int newX = x;
		Zombie zombie = (Zombie)board.getEntity(x,y);
		for (int i = 1; i < zombie.getMovSpd() + 1; i ++) {
			Entity encounteredEntity = board.getEntity(newX-1, y);
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
		int numZombies = level.getWave(turn);
		
		for (int i = 0; i < numZombies; i++) {
			boolean placed = false;
			do {
				int newPosY = ThreadLocalRandom.current().nextInt(0, board.HEIGHT);
				placed = board.placeEntity(board.WIDTH-1,newPosY, new BasicZombie());
			} while (placed == false);
		}
	}

	
	public static void main(String[] args) {
		board = new Board();
		board.addSun(150);
		
		//TODO: Load levels from text files
		level = new Level();
		
		level.addWave(0, 1);
		level.addWave(2, 2);
		level.addWave(5, 6);
		level.addWave(7, 6);
		
		view = new ASCIIView(board);
		view.drawMenu();
		entityFactory = new EntityFactory();
	}


}
