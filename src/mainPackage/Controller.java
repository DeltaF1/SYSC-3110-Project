package mainPackage;
/**
 * Entry point of the program: the "Controller" in the so called "Model View Controller" pattern
 * Check GitHub for authors
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;

import mainPackage.plants.Plant;
import mainPackage.plants.Sunflower;
import mainPackage.zombies.Zombie;


public class Controller {
	public final static int START_SUN = 150;
	
	private static Board board;
	private static View view;
	public static final int PLACE_AREA_WIDTH = 5; //width of area that a player can place plants
	public static DefaultListModel levelListModel;
	
	private static Level loadLevel(String filename) {
		try
		{
			String xml = new String(Files.readAllBytes(Paths.get(filename)));
			Level level = new Level();
			level.setXML(xml);
			return level;
			
		} catch (IOException e)
		{
			return null;
		}
	}
	
	/*
	 * TODO: call loadLevel first to go to PRELEVEL state and show the player which zombies will be in the level
	 * stats the game
	 */
	public static void startGame(String level) {
		setUpGame(board);
		board.setLevel(loadLevel(level));
		view.announce("Level start!");
		view.drawGame();
	}
	
	public static void loadGame(String savePath) {
		try {
			String xml = new String(Files.readAllBytes(Paths.get(savePath)));
			board.setXML(xml);
			view.drawGame();
		} catch (Exception e) {
			view.announce("Couldn't load save!");
		}
	}
	
	/**
	 * places a plant
	 * @param args user specified arguments (plant name and coordinates)
	 */
	public static void placePlant(String plantName, int x, int y) {
		Plant selectedPlant = EntityFactory.makePlant(plantName);
		if (selectedPlant == null) {
			view.announce("\"" + plantName + "\"" + " is not a valid plant name");
			return;
		}

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
	public static void tileInfo(int x, int y) {
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
	 * ends the current turn
	 * @param args extra arguments specified by player (currently unused)
	 */
	public static void endTurn() {
			advancePlants();
			advanceZombies();
			board.endTurn();
	}
	
	/**
	 * return the game to its state before the most recent turn
	 */
	public static void undoTurn() {
		if (board.undo()) {
			view.announce("Undid turn");
		} else {
			view.announce("Nothing to undo!");
		}
	}
	
	/**
	 * undo an undoTurn
	 */
	public static void redoTurn() { 
		if (board.redo()) {
			view.announce("Redid turn");
		} else {
			view.announce("Nothing to redo!");
		}
	}
	
	/**
	 * saves the game
	 * @param destination the file path to save the game to
	 */
	public static void saveGame(String destination) {
		if (board.toXMLFile(destination)) {
			view.announce("Saved game successfully");
		} else {
			view.announce("There was an error saving the file!");
		}
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
					} else if (plant.getCoolDown() >= 0){
						plant.setCoolDown(plant.getAtkSpd());
						
						System.out.println("TEMP MSG: plant at " + Integer.toString(x) + "," + Integer.toString(y) + " shot");
						Integer bulletHit = getHitZombieX(y);
						if (bulletHit != null) {
							Zombie shotZombie = (Zombie) board.getEntity(bulletHit, y);
							int newHP = shotZombie.getHp() - ((Plant)board.getEntity(x,y)).getDamage();
							System.out.println("TEMP MSG: plant hit zombie at " + Integer.toString(bulletHit) + "," + Integer.toString(y) + " reducing it's health to " + Integer.toString(newHP));
							if (newHP <= 0) {
								board.removeEntity(bulletHit, y);
							} else {
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
		int oldX = x;
		Zombie zombie = (Zombie)board.getEntity(x,y);
		Entity encounteredEntity = null;
		//move zomb forward until it hits an entity or reaches end of map
		for (int i = 0; i < (zombie.getMovSpd()) && (x > 0); i ++) {
			encounteredEntity = board.getEntity(x-1, y);
			if (encounteredEntity == null) { //walk forward if we can
				x --;
			} else { //stop walking when we bump into entity
				break;
			}
		}
		if (x > 0) { //zomb hasn't gotten to end of map
			board.moveEntity(oldX, y, x, y);
			//attack plant if in front of a plant
			if  (encounteredEntity instanceof Plant ) {
				Plant attacking = (Plant) encounteredEntity;
				int newHP = attacking.getHp() - zombie.getDamage();
				view.announce("The plant at " + Integer.toString(x - 1) + "," + Integer.toString(y) +" had its health reduced to " + Integer.toString(newHP));
				if (newHP <= 0) {
					board.removeEntity(x - 1, y);
				} else {
					attacking.setHp(newHP);
				}
			}
		} else { //zomb has reached end of map, game over
			view.drawGameOver();
		}
	}
	
	/**
	 * Complete the turn for all currently placed zombies
	 */
	private static void advanceZombies() {		
		
		for (int y = 0; y < board.getTiles().length; y++) {
			for (int x = 0; x <  board.getTiles()[y].length; x++) {
				if( board.getEntity(x, y) instanceof Zombie) {
					advanceZombie(x,y);				
				}
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
		
		//TOFIX
		board.boardStates.push(board.toXML());

	}
	
	static EditableLevel editorLevel;
	/**
	 * tells the model to add a zombie with the ZombieSpawnSettings aZombieSettings
	 * @param aZombSettings
	 */
	public static void editorAddZombie(ZombieSpawnSettings aZombSettings) {
		editorLevel.addSpawn(  aZombSettings.getSpawnTurn(), aZombSettings.getName() );
		System.out.println(editorLevel.toXML());
	}
	
	/**
	 * tells the model to select the zombie at the given index
	 * @param index
	 */
	public static void editorSelectZombie(ZombieSpawnSettings sel) {
		editorLevel.setSelected(sel);
	}
	
	/**
	 * tells the model to remove the zombie at the currently selected index
	 */
	public static void editorRemoveZombie() {
		editorLevel.removeSpawn();
	}
	
	/**
	 * tells the model to modify the settings of the currently selected zombie
	 * @param aZombSettings
	 */
	public static void editorEditZombie(ZombieSpawnSettings aZombSettings) {
		editorLevel.removeSpawn();
		editorLevel.addSpawn(aZombSettings.getSpawnTurn(), aZombSettings.getName());
		//board.editorEditZombie(aZombSettings);
	}
	
	public static void writeEditorFileToDisk(String fname) {
		// TODO Auto-generated method stub
		
		editorLevel.writeToDisk(fname);
	}
	
	public static void resetEditorLevel() {
		editorLevel = new EditableLevel(view);
	}
	
	public static EditableLevel getEditorLevel() {
		return editorLevel;
	}
	
	public static void refreshLevels() {
		levelListModel.clear();
		File levelDir = new File("levels");
		for (File level : levelDir.listFiles()) {
			if (level.isFile()) {
				levelListModel.addElement(level.getPath());
			}
		}
	}
	
	/**
	 * The initialization for when the controller's main is run - also used in testing to be able to set a desired board and view
	 * @param aBoard
	 * @param aView
	 */
	public static void controllerInit(Board aBoard, GraphicsView aView) {
		board = aBoard;
		
		view = aView;
		view.drawMenu();
		board.registerView(view);
		
		editorLevel =  new EditableLevel(view);
		//editorLevel.setXML("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><level><allWaves><wave><turn>1</turn><zombieTypes><zombieType>basic</zombieType><zombieType>boomer</zombieType><zombieType>doomer</zombieType></zombieTypes></wave><wave><turn>2</turn><zombieTypes><zombieType>zoomer</zombieType></zombieTypes></wave><wave><turn>3</turn><zombieTypes><zombieType>basic</zombieType></zombieTypes></wave></allWaves><currentWave>0</currentWave></level>\r\n");
	}
	
	public static void main(String[] args) {
		levelListModel = new DefaultListModel<String>();
		
		controllerInit(new Board(),new GraphicsView());
	}


}
