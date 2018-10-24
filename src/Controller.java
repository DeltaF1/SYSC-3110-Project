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
		int x, y;
		try {
			x = Integer.parseInt(args[1]);
			y = Integer.parseInt(args[2]);
			board.checkCoords(x, y);
		} catch (IllegalArgumentException err) { //parseInt and checkCoords throw the same type of error
			return String.format("invalid board coordinates: (x = %s, y = %s)", args[1], args[2]);
		}
		if (board.placeEntity(x, y, selectedPlant)) {
			return String.format("%s placed successfully at (%d, %d)", plantName, x, y);
		} else {
			return "There's already something there, put your plant somewhere else.";
		}
	}
	
	private String tileInfo(String[] args) {
		
	}
	
	private String endTurn(String[] args) {
		return "I think Trevor implemented this";
	}
}
