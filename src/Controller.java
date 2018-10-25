import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.HashMap;

public class Controller {
	private static Board board;
	private static HashMap<String, Plant> plantTypes;
	private static HashMap<String, Zombie> zombieTypes;
	private static ASCIIView view;
	public static final int PLACE_AREA_WIDTH = 5;
	public static final float SELL_PERCENT = 0.8f;//percent of cost reclaimed when sold
	
	
	/**
	 * register all plant types here
	 */
	private static HashMap<String, Plant> createPlantTypes() {
		HashMap<String, Plant> plants = new HashMap<String, Plant>();
		plants.put("basic", new Plant("basic", 1, 1, 1, 1));
		return plants;
	}
	
	/**
	 * register all zombie types here
	 */
	private static HashMap<String, Zombie> createZombieTypes() {
		return new HashMap<String, Zombie>();
	}
	
	/**
	 * register all command types here
	 */
	public static String parseText(String commandStr) {
		if (commandStr == null || commandStr.length() == 0) {
			return "Try actually typing something next time";
		}
		String[] cmdNameAndArgs = commandStr.split(" ", 2);
		String cmdName = cmdNameAndArgs[0];
		String[] cmdArgs = cmdNameAndArgs[1].split(" ");
		
		switch (cmdName) {
		case "place":
			return placePlant(cmdArgs);
		case "done":
			return endTurn(cmdArgs);
		case "info":
			return tileInfo(cmdArgs);
		default:
			return "\"" + cmdName + "\"" + " isn't even a real command";
		}
	}
	
	private static String placePlant(String[] args) {
		if (args.length < 3) {
			return "To place a plant you need to specify plant name, x coordinate, and y coordinate";
		}
		String plantName = args[0];
		Plant selectedPlant = plantTypes.get(plantName);
		if (selectedPlant == null) {
			return "\"" + plantName + "\"" + " is not a valid plant name";
		}
		int[] coords = strCoordsToInt(args[1], args[2]);
		if (coords == null) {
			return String.format("invalid board coordinates: (x = %s, y = %s)", args[1], args[2]);
		}
		int x = coords[0]; 
		int y = coords[1];
		if (board.placeEntity(x, y, selectedPlant)) {
			return String.format("%s placed successfully at (%d, %d)", plantName, x, y);
		} else {
			return "There's already something there, put your plant somewhere else.";
		}
	}
	
	private static String tileInfo(String[] args) {
		if (args.length < 2) {
			return "To see tile info you need to specify an x coordinate and a y coordinate";
		}
		int[] coords = strCoordsToInt(args[1], args[2]);
		if (coords == null) {
			return String.format("invalid board coordinates: (x = %s, y = %s)", args[1], args[2]);
		}
		int x = coords[0]; 
		int y = coords[1];
		Entity tileEntity = board.getTile(x, y).getOccupant();
		if (tileEntity == null) {
			return "There's nothing on that tile";
		}
		if (tileEntity instanceof Plant) {
			Plant p = (Plant) tileEntity;
			String desc = "That tile contains a %s Plant with an hp of %d, damage of %d, and attack speed of %d";
			return String.format(desc, p.getName(), p.getHp(), p.getDamage(), p.getAtkSpd());
		}
		if (tileEntity instanceof Zombie) {
			Zombie z = (Zombie) tileEntity;
			String desc = "That tile contains a %s Zombie with an hp of %d, damage of %d, and movement speed of %d";
			return String.format(desc, z.getName(), z.getHp(), z.getDamage(), z.getMovSpd());
		}
		return "The entity on that tile isn't a plant or a zombie, who knows what it is.";
	}
	
	private static int[] strCoordsToInt(String strx, String stry) {
		int x, y;
		try {
			x = Integer.parseInt(strx);
			y = Integer.parseInt(stry);
			board.checkCoords(x, y);
		} catch (IllegalArgumentException err) { //parseInt and checkCoords throw the same type of error
			return null;
		}
		return new int[] {x, y};
	}
	
	private static String endTurn(String[] args) {
		return "put code here to end the turn somehow";
	}
	
	
	
	/**
	 * Determine the X position of the closest zombie on the Y row (this must be the zombie hit by plant projectile)
	 */
	private static Integer getHitZombieX(int y) {
		for (int x = 0; x < board.getTiles()[y].length; x++) {
			if( board.getTiles()[y][x].getOccupant() instanceof Zombie) {
				return new Integer(x);
			}
		}
		return null;
	}
	
	private static void advancePlants() {

		for (int y = 0; y < board.getTiles().length; y++) {
			for (int x = 0; x < PLACE_AREA_WIDTH; x++) {
				if( board.getTiles()[y][x].getOccupant() instanceof Plant) {
					Plant plant = (Plant)board.getTiles()[y][x].getOccupant();
					
					if (plant.getCoolDown() > 0) {
						plant.setCoolDown(plant.getCoolDown()-1);
					}else if (plant.getCoolDown() > -1){
						plant.setCoolDown(plant.getAtkSpd());
						
						view.announce("TEMP MSG: plant at " + Integer.toString(x) + "," + Integer.toString(y) + " shot");
						Integer bulletHit = getHitZombieX(y);
						if (bulletHit != null) {
							Zombie shotZombie = (Zombie) board.getTiles()[y][bulletHit].getOccupant();
							int newHP = shotZombie.getHp() - ((Plant)board.getTiles()[y][x].getOccupant()).getDamage();
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
			//wait in line behind zombie ahead
			if (newX > 0 && board.getTiles()[y][newX-1].getOccupant() instanceof Zombie ) {
				break;
			//attack plant
			} else if  (newX > 0 && board.getTiles()[y][newX-1].getOccupant() instanceof Plant ) {
				Plant attacking = (Plant) board.getTiles()[y][newX-1].getOccupant();
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
				board.placeEntity(newX,y,new Zombie( zombie.getName(), zombie.getHp(), zombie.getDamage(), zombie.getMovSpd()));
			}else {
				view.announce("TEMP MSG: a zombie broke through on row " + Integer.toString(y));
			}
			board.removeEntity(x, y);
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
		int newPosY = ThreadLocalRandom.current().nextInt(0, board.HEIGHT);
		board.placeEntity(board.WIDTH-1,newPosY, new Zombie("a zombie", 60,20,2));
	}

	
	public static void main(String[] args) {
		
		board = new Board();
		view = new ASCIIView(board);
		plantTypes = createPlantTypes();
		zombieTypes = createZombieTypes();
		view.announce("TEMP MSG: Move options...\n\tplace <name> <x> <y>\n\t\twhere name is either Peashooter, Melon-pult, or Chestnut\n\tremove <x> <y>\n\tskip\n\t\ttyping nothing and just hitting enter should work too");
	}


}

/*	
	public static void inputPlacePlant(int x, int y, String name, int hp, int damage, int atkSpd, int cost) {
		board.placeEntity(x, y, new Plant(name, hp, damage, atkSpd, cost));
	}
	
	
	public static void inputPlacePlant(String name, int x, int y) {
		name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase(); //handle any case
		////inputPlacePlant(x,y, name, HP, DMG, ATTKSPEED, COST );
		if (name.equals("Peashooter")) {
			inputPlacePlant(x,y, name, 50, 20, 2, 200 );
		}else if (name.equals("Melon-pult")) {
			inputPlacePlant(x,y, name, 50, 150, 8, 250 );
		}else if (name.equals("Chestnut")) {
			inputPlacePlant(x,y, name, 400, -1, -1, 300 );
		}else {
			System.out.println("ERROR: unknown name given to inputPlacePlant, " + name);
			inputSkipTurn();
		}
>>>>>>> mergeWithTrevor
	}
	
	
	public static void inputRemovePlant(int x, int y) {
		Plant toDelete = (Plant) board.getTiles()[y][x].getOccupant();
		int reclaimed = (int) (toDelete.getCost() * SELL_PERCENT);
		System.out.println("TEMP MSG: player should receive " + reclaimed + " cash for selling plant");
		board.removeEntity(x, y);
	}
	
	
	public static void inputSkipTurn() {
		
	}
*/
