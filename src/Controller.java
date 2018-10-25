import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Controller {
	
	private static ASCIIView view;
	private static Board model;
	public static final int PLACE_AREA_WIDTH = 5;
	public static final float SELL_PERCENT = 0.8f;//percent of cost reclaimed when sold
	
	/**
	 * Creates a plant and places it on the board
	 * 
	 * @ param x int  the x index to place at (min: 0, max: PLACE_AREA_WIDTH)
	 * @ param y int  the y index to place at (min: 0, max: Board.HEIGHT)
	 * @ param name String  the name to use for the plant
	 * @ param hp int  hit points of the plant
	 * @ param damage int  damage points of the plant Probably a good idea to set to -1 if atkSpd is -1 for clarity
	 * @ param atkSpd int  the amount of turns needed for a cooldown after shooting or -1 for never shoot
	 * @ param cost int  the amount of money needed to purchase the plant (not implemented yet)
	 * */
	public static void inputPlacePlant(int x, int y, String name, int hp, int damage, int atkSpd, int cost) {
		model.placeEntity(x, y, new Plant(name, hp, damage, atkSpd, cost));
	}
	
	/**
	 * Creates a plant preset by name and places it on the board
	 * 
	 * @ param x int  the x index to place at (min: 0, max: PLACE_AREA_WIDTH)
	 * @ param y int  the y index to place at (min: 0, max: Board.HEIGHT)
	 * @ param name String  the name to use for the plant/name of the preset
	 */
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
	}
	
	/**
	 * Creates a plant preset by name and places it on the board
	 * 
	 * @ param x int  the x index to place at (min: 0, max: PLACE_AREA_WIDTH)
	 * @ param y int  the y index to place at (min: 0, max: Board.HEIGHT)
	 * @ param name String  the name to use for the plant/name of the preset
	 */
	public static void inputRemovePlant(int x, int y) {
		Plant toDelete = (Plant) model.getTiles()[y][x].getOccupant();
		int reclaimed = (int) (toDelete.getCost() * SELL_PERCENT);
		System.out.println("TEMP MSG: player should receive " + reclaimed + " cash for selling plant");
		model.removeEntity(x, y);
	}
	
	/**
	 * stuff that should happen if no move is made by player
	 */
	public static void inputSkipTurn() {
		
	}
	
	/**
	 * Determine the X position of the closest zombie on the Y row (this must be the zombie hit by plant projectile)
	 * @param y int  the row to check for zombie hit
	 * @return x Integer  the x position of the hit or null if no zombie on row to be hit
	 */
	private static Integer getHitZombieX(int y) {
		for (int x = 0; x < model.getTiles()[y].length; x++) {
			if( model.getTiles()[y][x].getOccupant() instanceof Zombie) {
				return new Integer(x);
			}
		}
		return null;
	}
	
	/**
	 * Complete a turn for the plants. Cooldowns are tracked and plants ready to shoot do so.
	 */
	private static void advancePlants() {

		for (int y = 0; y < model.getTiles().length; y++) {
			for (int x = 0; x < PLACE_AREA_WIDTH; x++) {
				if( model.getTiles()[y][x].getOccupant() instanceof Plant) {
					Plant plant = (Plant)model.getTiles()[y][x].getOccupant();
					
					if (plant.getCoolDown() > 0) {
						plant.setCoolDown(plant.getCoolDown()-1);
					}else if (plant.getCoolDown() > -1){
						plant.setCoolDown(plant.getAtkSpd());
						
						System.out.println("TEMP MSG: plant at " + Integer.toString(x) + "," + Integer.toString(y) + " shot");
						Integer bulletHit = getHitZombieX(y);
						if (bulletHit != null) {
							Zombie shotZombie = (Zombie) model.getTiles()[y][bulletHit].getOccupant();
							int newHP = shotZombie.getHP() - ((Plant)model.getTiles()[y][x].getOccupant()).getDamage();
							System.out.println("TEMP MSG: plant hit zombie at " + Integer.toString(bulletHit) + "," + Integer.toString(y) + " reducing it's health to " + Integer.toString(shotZombie.getHP()));
							if (newHP <= 0) {
								model.removeEntity(bulletHit, y);
							}else {
								shotZombie.setHP(newHP);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Complete a turn for an individual zombie
	 * @param x int  the x index the zombie is located at
	 * @param y int  the y index the zombie is located at
	 */
	private static void advanceZombie(int x, int y) {
		int newX = x;
		Zombie zombie = (Zombie)model.getTiles()[y][x].getOccupant();
		for (int i = 1; i < zombie.getMovSpd() + 1; i ++) {
			//wait in line behind zombie ahead
			if (newX > 0 && model.getTiles()[y][newX-1].getOccupant() instanceof Zombie ) {
				break;
			//attack plant
			}else if  (newX > 0 && model.getTiles()[y][newX-1].getOccupant() instanceof Plant ) {
				Plant attacking = (Plant) model.getTiles()[y][newX-1].getOccupant();
				int newHP = attacking.getHP() - zombie.getDamage();
				System.out.println("TMP MSG: The plant at " + Integer.toString(newX-1) + "," + Integer.toString(y) +" had its health reduced to " + Integer.toString(newHP));
				if (newHP <= 0) {
					model.removeEntity(newX-1, y);
				}else {
					attacking.setHP(newHP);
				}
				break;
			//walk forward
			}else {
				newX = x - 1;
			}
		}
		if (newX != x) {
			if (newX >= 0) {
				model.placeEntity(newX,y,new Zombie( zombie.getName(), zombie.getHP(), zombie.getDamage(), zombie.getMovSpd()));
			}else {
				System.out.println("TEMP MSG: a zombie broke through on row " + Integer.toString(y));
			}
			model.removeEntity(x, y);
		}
	}
	
	/**
	 * Complete the turn for all currently placed zombies
	 */
	private static void advanceZombies() {		
		
		for (int y = 0; y < model.getTiles().length; y++) {
			for (int x = 0; x <  model.getTiles()[y].length; x++) {
				if( model.getTiles()[y][x].getOccupant() instanceof Zombie) {
					advanceZombie(x,y);				}
			}
		}
	}
	
	/**
	 * Handle new zombies spawned this turn
	 */
	private static void spawnZombies() {
		int newPosY = ThreadLocalRandom.current().nextInt(0, model.getTiles().length);
		model.placeEntity(model.getTiles()[0].length-1,newPosY, new Zombie("a zombie", 60,20,2));
	}
	
	public static String parseText(String input) {
		String[] words = input.split("\\s");
		String out = "";
		
		if (words[0].toLowerCase().equals("place")) 
		{
			inputPlacePlant(words[1],Integer.parseInt(words[2]),Integer.parseInt(words[3])  );
		}
		else if (words[0].toLowerCase().equals("remove")) 
		{
			inputRemovePlant(Integer.parseInt(words[1]),Integer.parseInt(words[2]) );
		}
		else if (words[0].toLowerCase().equals("skip") || input.equals("")) 
		{
			inputSkipTurn();
		}
		else if(words[0].equals("quit"))
		{
			System.exit(0);
		}
		else {
			return "ERROR: Unknown move type " + words[0];
		}
		advancePlants();
		advanceZombies();
		spawnZombies();
		view.draw();
		return out;
	}
	
	public static void main(String[] args) {
		model = new Board();
		view = new ASCIIView(model);
		System.out.println("TEMP MSG: Move options...\n\tplace <name> <x> <y>\n\t\twhere name is either Peashooter, Melon-pult, or Chestnut\n\tremove <x> <y>\n\tskip\n\t\ttyping nothing and just hitting enter should work too");
	}
}
