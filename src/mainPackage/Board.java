package mainPackage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import mainPackage.plants.Plant;
import mainPackage.zombies.Zombie;

/**
 * A class that represents a board that the plants and zombies move on
 * Check GitHub for authors
 */

public class Board {
	
	private Entity[][] entities;
	public static final int WIDTH = 20;
	public static final int HEIGHT = 10;
	private int sunPoints;
	private LinkedList<View> views;
	private int turn;
	private int numZombies;
	private String levelName;
	private Level level;
	public Stack<String> boardStates;
	private Stack<String> undoneBoardStates;
	
	/**
	 * Creates a new board
	 */
	public Board() {
		entities = new Entity[HEIGHT][WIDTH];
		turn = 0;
		views = new LinkedList<View>();
		boardStates = new Stack<String>();
		undoneBoardStates = new Stack<String>();
		sunPoints = 0;
	}
	
	/**
	 * creates a new board from a 2d array of Entities
	 * @param tiles Tiles to make the board out of
	 */
	public Board(Entity[][] entities) {
		this();
		setTiles(entities);
	}
	
	/**
	 * Sets tiles on the board to a 2d array of tiles
	 * if the array is smaller then the board, only some of the board will be filled
	 * @param tiles the tiles to fill the board with
	 */
	public void setTiles(Entity[][] tiles) {
		checkCoords(tiles[0].length - 1, tiles.length - 1);
		for (int y = 0; y < tiles.length; y++) {
			for (int x = 0; x < tiles[0].length; x++) {
				this.entities[y][x] = tiles[y][x];
			}
		}
	}
	
	/**
	 * @return all the tiles
	 */
	public Entity[][] getTiles() {
		return entities;
	}
	
	/**
	 * gets the entity at specified location
	 * @param x the x coordinate of the entity to remove
	 * @param y the y coordinate of the entity to remove
	 * @return the entity at (x, y)
	 */
	public Entity getEntity(int x, int y) {
		checkCoords(x, y);
		return entities[y][x];
	}
	
	/**
	 * @return true if entity was placed successfully, false otherwise
	 */
	public boolean placeEntity(int x, int y, Entity e) {
		checkCoords(x, y);
		if (entities[y][x] == null) {
			entities[y][x] = e;
			if (e instanceof Zombie) {
				numZombies++;
			}
			updateEntity(x, y);
			return true;
		}
		return false;
	}
	
	/**
	 * removes the entity at the specified location
	 * @param x x coordinate of the entity to remove
	 * @param y y coordinate of the entity to remove
	 */
	public void removeEntity(int x, int y) {
		checkCoords(x, y);
		Entity e = entities[y][x];
		if (e instanceof Zombie) {
			numZombies--;
		}
		entities[y][x] = null; 
		updateEntity(x,y);
	}
	
	/**
	 * ensures that x and y are valid coords, throws an error if they're not
	 * @param x x coord to check
	 * @param y y coord to check
	 */
	public void checkCoords(int x, int y) {
		if (x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT) {
			String err = String.format("invalid board coordinates: (x = %d, y = %d)", x, y);
			throw new IllegalArgumentException(err);
		}
	}
	
	/**
	 * @return the current sunpoints on the board
	 */
	public int getSun() {
		return sunPoints;
	}
	
	/**
	 * tries to spend some sun points
	 * @param cost the amount of sun points to spend
	 * @return true if sunpoints were succesfully spent, false otherwise (insufficient funds)
	 */
	public boolean spendSun(int cost) {
		if (cost <= sunPoints) {
			addSun(-cost);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * adds sunpoints to board
	 * @param sun amount of sunpoints to add
	 */
	public void addSun(int sun) {
		sunPoints += sun;
		updateSun(sunPoints);
	}
	
	/**
	 * sets sunpoints to the specified value
	 * @param sun amount of sun points
	 */
	public void setSun(int sun) {
		sunPoints = sun;
		updateSun(sunPoints);
	}
	
	/**
	 * clear the board
	 */
	public void wipe() {
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				removeEntity(x,y);
			}
		}
		setSun(0);
		turn = 0;
	}
	
	/**
	 * Add a view to the list fo views that wish to be notified by this model
	 * @param view
	 */
	public void registerView(View view) {
		views.add(view);
	}
	
	/**
	 * Notifies the views to update the given board position
	 * @param x
	 * @param y
	 */
	private void updateEntity(int x, int y) {
		for (View view : views) {
			view.updateEntity(getEntity(x, y), x, y);
		}
	}
	
	/**
	 * Notifies the views to update their sun total
	 * @param sun
	 */
	private void updateSun(int sun) {
		for (View view : views) {
			view.updateSun(sun);
		}
	}
	
	/**
	 * converts this Board to xml
	 * @return a String representing the current state of the Board in XML format
	 */
	public String toXML() {
		try {
			Document xml  = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element rootElm = xml.createElement("board");
			Element allTilesElm = xml.createElement("tiles");
			Element sunElm = xml.createElement("sunPoints");
			Element turnElm = xml.createElement("turn");
			Element levelElm = xml.createElement("level");
			Element widthElm = xml.createElement("WIDTH");
			Element heightElm = xml.createElement("HEIGHT");
			for (Entity[] row: entities) {
				Element rowElm = xml.createElement("row");
				for (Entity e: row) {
					Element tileElm = xml.createElement("tile");
					Entity occupant = e;
					if (occupant != null) {
						String occupantType;
						if (occupant instanceof Zombie) 
							occupantType = "zombie";
						else 
							occupantType =  "plant";
						Element occupantElm = xml.createElement(occupantType);
						Attr hpAttr = xml.createAttribute("hp");
						hpAttr.setValue(String.format("%d", occupant.getHp()));
						Attr dmgAttr = xml.createAttribute("damage");
						dmgAttr.setValue(String.format("%d", occupant.getDamage()));
						occupantElm.setAttributeNode(hpAttr);
						occupantElm.setAttributeNode(dmgAttr);
						occupantElm.appendChild(xml.createTextNode(e.getName()));
						tileElm.appendChild(occupantElm);
					}
					rowElm.appendChild(tileElm);
				}
				allTilesElm.appendChild(rowElm);
			}
			sunElm.appendChild(xml.createTextNode(String.format("%d", sunPoints)));
			widthElm.appendChild(xml.createTextNode(String.format("%d", WIDTH)));
			heightElm.appendChild(xml.createTextNode(String.format("%d", HEIGHT)));
			turnElm.appendChild(xml.createTextNode(String.format("%d", turn)));
			levelElm.appendChild(xml.createTextNode(levelName));
			rootElm.appendChild(allTilesElm);
			rootElm.appendChild(sunElm);
			rootElm.appendChild(turnElm);
			rootElm.appendChild(levelElm);
			rootElm.appendChild(widthElm);
			rootElm.appendChild(heightElm);
			xml.appendChild(rootElm);
			return StringUtils.XMLToString(xml);
		} catch (Exception e) {
			e.printStackTrace();
	    	return null;
	    }
	}
	
	/**
	 * Loads in gamestate from an xml string
	 * @param xml
	 */
	public void setXML(String xml) {
		try {
			this.wipe();
			SAXParser sax = SAXParserFactory.newInstance().newSAXParser();
			sax.parse(new InputSource(new StringReader(xml)), new DefaultHandler() {
				int currentHp;
				String currentTag;
				int currentRow = 0;
				int currentColumn = 0;
				
				@Override
				public void startElement(String u, String ln, String qName, Attributes attributes) {
					currentTag = qName;
					if (qName == "plant" || qName == "zombie") {
						currentHp = 0;
						for (int i = 0; i < attributes.getLength(); i ++) {
							if (attributes.getQName(i).equals("hp")) {
								currentHp = Integer.valueOf(attributes.getValue(i));
								break;
							}
						}
					}
				}
				
				@Override
				public void endElement(String url, String localName, String qName) {
					currentTag = "";
					switch (qName) {
					case "tile":
						currentColumn ++;
						break;
					case "row":
						currentColumn = 0;
						currentRow ++;
						break;
					}
				}
				
				@Override
				public void characters(char[] ch, int start, int len) {
					String data = new String(ch, start, len);
					switch (currentTag) {
					case "tile":
						break;
					case "sunPoints":
						setSun(Integer.valueOf(data));
						break;
					case "plant":
						Plant newPlant = EntityFactory.makePlant(data);
						newPlant.setHp(currentHp);
						placeEntity(currentColumn, currentRow, newPlant);
						break;
					case "zombie":
						Zombie newZombie = EntityFactory.makeZombie(data);
						newZombie.setHp(currentHp);
						placeEntity(currentColumn, currentRow, newZombie);
						break;
					case "turn":
						turn = Integer.valueOf(data);
						break;
					case "level":
						setLevel(data);
						break;
					}
				}
			});
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * writes this board's current state to an xml file
	 * @param destination to write the the board to
	 */
	public boolean toXMLFile(String destination) {
		try {
			BufferedWriter file = new BufferedWriter(new FileWriter(destination));
			file.write(toXML());
			file.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 *  Move an entity from one location to another
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public void moveEntity(int x1, int y1, int x2, int y2) {
		Entity e = getEntity(x1,y1);
		removeEntity(x1, y1);
		placeEntity(x2,y2,e);
	}
	
	/**
	 * Update state at the end of a turn
	 */
	public void endTurn() {
		spawnZombies();
		boardStates.push(toXML());
		
		// Clears the redo stack, since this "timeline" is finalized with the ending of a turn
		undoneBoardStates.removeAllElements();
		turn++;
		
		//Check for win condition
		if (numZombies == 0 && turn > level.lastWave()) {
			drawWinScreen();
		}
	}
	
	/**
	 * Notfies the views to draw the win screen
	 */
	private void drawWinScreen()
	{
		for (View view : views) {
			view.drawWinScreen();
		}
	}
	
	/**
	 * Spawns zombies if there are any zombies to spawn on the current turn
	 */
	private void spawnZombies()
	{
		List<String> wave = level.getSpawn(turn);
		if (wave != null) {
			for (String zombieType : wave) {
				boolean placed = false;
				// Spawn zombies at random locations
				// pick a new location until one is found that is not occupied
				do {
					int newPosY = ThreadLocalRandom.current().nextInt(0, HEIGHT);
					placed = placeEntity(WIDTH-1,newPosY, EntityFactory.makeZombie(zombieType));
				} while (placed == false);
			}
		}
	}
	
	/**
	 * Undoes the current turn and reverts to the previous state
	 * @return Whether the undo was successful or not
	 */
	public boolean undo() {
		if (boardStates.size() > 1) { //can only undo if you have both the current state and some previous state in the stack
			undoneBoardStates.push(boardStates.pop()); //put current state in undone stack
			setXML(boardStates.peek());
			System.out.println(turn);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Redoes an undone turn
	 * @return Whether the redo happened or not
	 */
	public boolean redo() {
		if (! undoneBoardStates.isEmpty()) {
			String undoneState = undoneBoardStates.pop();
			boardStates.push(undoneState);
			setXML(undoneState);
			System.out.println(turn);
			return true;
		} else {
			return false;
		}
	}
	
	
	// Debugging methods for testing
	public Stack<String> getBoardStates(){
		return boardStates;
	}
	
	public Stack<String> getUndoneBoardStates(){
		return undoneBoardStates;
	}
	
	public int getTurn() {
		return turn;
	}
	
	private static Level loadLevel(String filename) {
		try
		{
			System.out.println("loading file: "+filename);
			String xml = new String(Files.readAllBytes(Paths.get(filename)));
			Level level = new Level();
			level.setXML(xml);
			return level;
			
		} catch (IOException e)
		{
			return null;
		}
	}
	
	public void setLevel(String filename) {
		if (!filename.equals(levelName)) {
			levelName = filename;
			level = loadLevel(filename);
		}
	}

	public void resetUndo()
	{
		boardStates.clear();
		undoneBoardStates.clear();
		
		boardStates.push(toXML());
	}
}
