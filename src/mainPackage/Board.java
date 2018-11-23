package mainPackage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A class that represents a board that the plants and zombies move on
 * Check GitHub for authors
 */

public class Board {
	
	private Tile[][] tiles;
	public static final int WIDTH = 20;
	public static final int HEIGHT = 10;
	private int sunPoints;
	private LinkedList<View> views;
	
	/**
	 * Creates a new board
	 */
	public Board() {
		this.tiles = new Tile[HEIGHT][WIDTH];
		
		views = new LinkedList<View>();
		
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				this.tiles[y][x] = new Tile();
			}
		}
		sunPoints = 0;
	}
	
	/**
	 * creates a new board from a 2d array of Tiles
	 * @param tiles Tiles to make the board out of
	 */
	public Board(Tile[][] tiles) {
		this();
		setTiles(tiles);
	}
	
	/**
	 * Sets tiles on the board to a 2d array of tiles
	 * if the array is smaller then the board, only some of the board will be filled
	 * @param tiles the tiles to fill the board with
	 */
	public void setTiles(Tile[][] tiles) {
		checkCoords(tiles[0].length - 1, tiles.length - 1);
		for (int y = 0; y < tiles.length; y++) {
			for (int x = 0; x < tiles[0].length; x++) {
				this.tiles[y][x] = tiles[y][x];
			}
		}
	}
	
	/**
	 * @return all the tiles
	 */
	public Tile[][] getTiles() {
		return tiles;
	}
	
	/**
	 * @param x x location of the tile
	 * @param y y location of the tile
	 * @return the tile located at (x, y) on the board
	 */
	public Tile getTile(int x, int y) {
		checkCoords(x, y);
		return tiles[y][x];
	}
	
	/**
	 * @return true if entity was placed successfully, false otherwise
	 */
	public boolean placeEntity(int x, int y, Entity e) {
		checkCoords(x, y);
		if (tiles[y][x].getOccupant() == null) {
			tiles[y][x].setOccupant(e);
			updateEntity(x, y);
			return true;
		}
		return false;
	}
	
	/**
	 * gets the tile with the specified entity on it
	 * @param e the entity to search for
	 * @return null if entity is not found, otherwise returns the Tile the entity is on
	 */
	public Tile getEntityTile(Entity e) {
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				if (tiles[y][x].getOccupant() == e) {
					return tiles[y][x];
				}
			}
		}
		return null;
	}
	
	/**
	 * moves the specified entity to the specified x, y coordinates
	 * @param x x coordinate to move e to
	 * @param y y coordinate to move e to
	 * @param e the entity to move
	 * @return true if entity was moved succesfully, false otherwise
	 */
	public boolean moveEntity(int x, int y, Entity e) {
		checkCoords(x, y);
		Tile startTile = getEntityTile(e);
		if (startTile != null && tiles[y][x].getOccupant() == null) {
			startTile.setOccupant(null);
			updateEntity(x+1, y); //TODO: SUPER HACKY PLS FIX
			placeEntity(x, y, e);
			System.out.println("moved zomb to "+ Integer.toString(x));
			return true;
		}
		System.out.println("failed move");
		return false;
	}
	
	/**
	 * removes the entity at the specified location
	 * @param x x coordinate of the entity to remove
	 * @param y y coordinate of the entity to remove
	 */
	public void removeEntity(int x, int y) {
		checkCoords(x, y);
		tiles[y][x].setOccupant(null);
		updateEntity(x,y);
	}
	
	/**
	 * gets the entity at specified location
	 * @param x the x coordinate of the entity to remove
	 * @param y the y coordinate of the entity to remove
	 * @return the entity at (x, y)
	 */
	public Entity getEntity(int x, int y) {
		return getTile(x,y).getOccupant();
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
	
	public void wipe() {
		this.tiles = new Tile[HEIGHT][WIDTH];
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				this.tiles[y][x] = new Tile();
				updateEntity(x, y);
			}
		}
		sunPoints = 0;
		updateSun(sunPoints);
		
	}
	
	public void registerView(View view) {
		views.add(view);
	}
	
	private void updateEntity(int x, int y) {
		for (View view : views) {
			view.updateEntity(getEntity(x, y), x, y);
		}
	}
	
	private void updateSun(int sun) {
		for (View view : views) {
			view.updateSun(sun);
		}
	}
	
	/**
	 * converts this Board to xml
	 * @return a String representing the current state of the Board in XML format
	 * @throws ParserConfigurationException
	 * @throws TransformerException 
	 */
	public String toXML() {
		try {
			Document xml  = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element rootElm = xml.createElement("board");
			Element allTilesElm = xml.createElement("tiles");
			Element sunElm = xml.createElement("sunPoints");
			Element widthElm = xml.createElement("WIDTH");
			Element heightElm = xml.createElement("HEIGHT");
			for (Tile[] row: tiles) {
				Element rowElm = xml.createElement("row");
				for (Tile t: row) {
					Element tileElm = xml.createElement("tile");
					Entity occupant = t.getOccupant();
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
						occupantElm.appendChild(xml.createTextNode(t.getOccupant().getName()));
						tileElm.appendChild(occupantElm);
					}
					rowElm.appendChild(tileElm);
				}
				allTilesElm.appendChild(rowElm);
			}
			sunElm.appendChild(xml.createTextNode(String.format("%d", sunPoints)));
			widthElm.appendChild(xml.createTextNode(String.format("%d", WIDTH)));
			heightElm.appendChild(xml.createTextNode(String.format("%d", HEIGHT)));
			rootElm.appendChild(allTilesElm);
			rootElm.appendChild(sunElm);
			rootElm.appendChild(widthElm);
			rootElm.appendChild(heightElm);
			xml.appendChild(rootElm);
			StringWriter sw = new StringWriter();
			TransformerFactory tf = TransformerFactory.newInstance();
	        Transformer transformer = tf.newTransformer();
	        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
	        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	        transformer.transform(new DOMSource(xml), new StreamResult(sw));
	        return sw.toString();
		} catch (Exception e) {
			e.printStackTrace();
	    	return null;
	    }
	}
	
	public static Board fromXML(String xml) {
		try {
			SAXParser sax = SAXParserFactory.newInstance().newSAXParser();
			Board newBoard = new Board();
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
						newBoard.setSun(Integer.valueOf(data));
						break;
					case "plant":
						Plant newPlant = EntityFactory.makePlant(data);
						newPlant.setHp(currentHp);
						newBoard.placeEntity(currentColumn, currentRow, newPlant);
						break;
					case "zombie":
						Zombie newZombie = EntityFactory.makeZombie(data);
						newZombie.setHp(currentHp);
						newBoard.placeEntity(currentColumn, currentRow, newZombie);
						break;
					}
				}
			});
			return newBoard;
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
