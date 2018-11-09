package tests;
import mainPackage.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TestBoard {
	Board board;
	
	@Before
	public void setUp() {
		board =  new Board();
	}
	
	@Test
	public void testSetTiles() {
		Tile[][] tiles = new Tile[Board.HEIGHT][Board.WIDTH];
		for (int y = 0; y < Board.HEIGHT; y++) {
			for (int x = 0; x < Board.WIDTH; x++) {
				tiles[y][x] = new Tile();
			}
		}
		board.setTiles(tiles);
		
		Tile[][] getReturnedTiles = board.getTiles();
		for (int y = 0; y < Board.HEIGHT; y++) {
			for (int x = 0; x < Board.WIDTH; x++) {
				assertTrue("Unexpected tile instance at " + Integer.toString(x) + "," + Integer.toString(y),
						tiles[y][x] == getReturnedTiles[y][x]);
			}
		}
	}
	
	@Test
	public void testPlaceEntity() {
		Entity testEntity = new BasicZombie();
		board.placeEntity(1, 1, testEntity);
		for (int y = 0; y < Board.HEIGHT; y++) {
			for (int x = 0; x < Board.WIDTH; x++) {
				if(x == 1 && y == 1) {
					assertTrue("Expected entity to be occupant of " + Integer.toString(x) + "," + Integer.toString(y),
							board.getTile(x, y).getOccupant() == testEntity);
				}else {
					assertFalse( "Entity should not be occupant of " + Integer.toString(x) + "," + Integer.toString(y),
							board.getTile(x, y).getOccupant() == testEntity);
				}
			}
		}
	}
	
	@Test
	public void testMoveEntity() {
		Entity testEntity = new BasicZombie();
		board.placeEntity(1, 1,  testEntity);
		board.moveEntity(0, 1, testEntity);
		for (int y = 0; y < Board.HEIGHT; y++) {
			for (int x = 0; x < Board.WIDTH; x++) {
				if(x == 0 && y == 1) {
					assertTrue("Expected entity to be occupant of " + Integer.toString(x) + "," + Integer.toString(y),
							board.getTile(x, y).getOccupant() == testEntity);
				}else {
					assertFalse( "Entity should not be occupant of " + Integer.toString(x) + "," + Integer.toString(y),
							board.getTile(x, y).getOccupant() == testEntity);
				}
			}
		}
	}
	
	@Test
	public void testRemoveEntity() {
		Entity testEntity = new BasicZombie();
		board.placeEntity(1, 1,  testEntity);
		
		Entity[][] testEntities = new Entity[Board.HEIGHT][Board.WIDTH];
		
		for (int y = 0; y < Board.HEIGHT; y++) {
			for (int x = 0; x < Board.WIDTH; x++) {
				testEntities[y][x] = new BasicZombie();
				board.placeEntity(x, y, testEntities[y][x]);
			}
		}
		
		board.removeEntity(1, 1);
		
		for (int y = 0; y < Board.HEIGHT; y++) {
			for (int x = 0; x < Board.WIDTH; x++) {
				if(x == 1 && y == 1) {
					assertTrue("Occupant should be null at " + Integer.toString(x) + "," + Integer.toString(y),
							board.getTile(x, y).getOccupant() == null);
				}else {
					assertTrue("Unexpected occupant at " + Integer.toString(x) + "," + Integer.toString(y),
							board.getTile(x, y).getOccupant() == testEntities[y][x]);
				}
			}
		}

	}
	
	@Test
	public void testGetEntityTile() {

	}
	
	@Test
	public void testCheckCoords() {
		try{
			board.checkCoords(0,0);
			board.checkCoords(board.WIDTH-1,0);
			board.checkCoords(0,board.HEIGHT-1);
			board.checkCoords(board.WIDTH-1,board.HEIGHT-1);
		} catch (IllegalArgumentException e){
			assertTrue("unexpected check coords error",false);
		}
		
		try{
			board.checkCoords(-1,0);
			assertTrue("unexpected check coords pass",false);
		} catch (IllegalArgumentException e){}
		
		try{
			board.checkCoords(0,-1);
			assertTrue("unexpected check coords pass",false);
		} catch (IllegalArgumentException e){}
		
		try{
			board.checkCoords(-1,-1);
			assertTrue("unexpected check coords pass",false);
		} catch (IllegalArgumentException e){}
		
		try{
			board.checkCoords(board.WIDTH,0);
			assertTrue("unexpected check coords pass",false);
		} catch (IllegalArgumentException e){}
		
		try{
			board.checkCoords(0,board.HEIGHT);
			assertTrue("unexpected check coords pass",false);
		} catch (IllegalArgumentException e){}
		
		try{
			board.checkCoords(board.WIDTH,board.HEIGHT);
			assertTrue("unexpected check coords pass",false);
		} catch (IllegalArgumentException e){}
		
		
	}

	@Test
	public void testSpendSun() {
		assertEquals("Spending sun points should not be successfull, 0 sun points", board.spendSun(1),false);
		board.addSun(3);
		
		assertEquals("Should successfully spend sun points", board.spendSun(1),true);
		assertEquals("sun points was not decreased from 3 by 1 to 2 correcly", board.getSun(),2);
		assertEquals("Should successfully spend sun points", board.spendSun(2),true);
		assertEquals("sun points was not decreased from 2 by 2 to 0 correcly", board.getSun(),0);
	}
	
	@Test
	public void testAddSun() {
		board.addSun(1);
		assertEquals("sun should equal 1",board.getSun(),1);
		board.addSun(1);
		assertEquals("sun should equal 1",board.getSun(),2);
	}
	
	@Test
	public void testWipe() {
		board.addSun(1);
		Tile[][] tiles = new Tile[Board.HEIGHT][Board.WIDTH];
		for (int y = 0; y < Board.HEIGHT; y++) {
			for (int x = 0; x < Board.WIDTH; x++) {
				tiles[y][x] = new Tile();
			}
		}
		board.setTiles(tiles);
		board.wipe();
		for (int y = 0; y < Board.HEIGHT; y++) {
			for (int x = 0; x < Board.WIDTH; x++) {
				assertEquals("tile occupant was not removed at "  + Integer.toString(x) + "," + Integer.toString(y),
						board.getTile(x, y).getOccupant(),null);
			}
		}
		
		assertEquals("sunPoints were not reset to 0 ",board.getSun(),0);
	}
}