import java.util.HashMap;

public class Controller {
	private Board board;
	private HashMap<String, Plant> plantTypes;
	private HashMap<String, Zombie> zombieTypes;
	
	public Controller() {
		plantTypes = createPlantTypes();
		zombieTypes = createZombieTypes();
	}
	
	/**
	 * register all plant types here
	 */
	private HashMap<String, Plant> createPlantTypes() {
		HashMap<String, Plant> plants = new HashMap<String, Plant>();
		plants.put("basic", new Plant("basic", 1, 1, 1, 1));
		return plants;
	}
	
	/**
	 * register all zombie types here
	 */
	private HashMap<String, Zombie> createZombieTypes() {
		return new HashMap<String, Zombie>();
	}
	
	/**
	 * register all command types here
	 */
	public String parseText(String commandStr) {
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
	
	private String placePlant(String[] args) {
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
	
	private String tileInfo(String[] args) {
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
	
	private int[] strCoordsToInt(String strx, String stry) {
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
	
	private String endTurn(String[] args) {
		return "I think Trevor implemented this";
	}
}
