package tests;
import mainPackage.*;
import mainPackage.zombies.BasicZombie;

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
	
	/**
	 * ensure that tiles can be set and retrieved again like expected
	 */
	@Test
	public void testSetAndGetTiles() {
		Entity[][] tiles = new Entity[Board.HEIGHT][Board.WIDTH];
		board.setTiles(tiles);
		
		Entity[][] getReturnedTiles = board.getTiles();
		for (int y = 0; y < Board.HEIGHT; y++) {
			for (int x = 0; x < Board.WIDTH; x++) {
				assertTrue("Unexpected tile instance at " + Integer.toString(x) + "," + Integer.toString(y),
						tiles[y][x] == getReturnedTiles[y][x]);
			}
		}
	}
	
	/**
	 * ensure that entities can be placed on tiles
	 */
	@Test
	public void testPlaceEntity() {
		Entity testEntity = new BasicZombie(null);
		board.placeEntity(1, 1, testEntity);
		for (int y = 0; y < Board.HEIGHT; y++) {
			for (int x = 0; x < Board.WIDTH; x++) {
				if(x == 1 && y == 1) {
					assertTrue("Expected entity to be occupant of " + Integer.toString(x) + "," + Integer.toString(y),
							board.getEntity(x, y) == testEntity);
				}else {
					assertFalse( "Entity should not be occupant of " + Integer.toString(x) + "," + Integer.toString(y),
							board.getEntity(x, y) == testEntity);
				}
			}
		}
	}
	
	/**
	 * Ensure that an entity can be moved to another tile
	 */
	@Test
	public void testMoveEntity() {
		Entity testEntity = new BasicZombie(null);
		board.placeEntity(1, 1,  testEntity);
		board.moveEntity( 1,1,0, 1);
		for (int y = 0; y < Board.HEIGHT; y++) {
			for (int x = 0; x < Board.WIDTH; x++) {
				if(x == 0 && y == 1) {
					assertTrue("Expected entity to be occupant of " + Integer.toString(x) + "," + Integer.toString(y),
							board.getEntity(x, y) == testEntity);
				}else {
					assertFalse( "Entity should not be occupant of " + Integer.toString(x) + "," + Integer.toString(y),
							board.getEntity(x, y) == testEntity);
				}
			}
		}
	}
	
	/**
	 * ensure that a entity can be removed from the board
	 */
	@Test
	public void testRemoveEntity() {
		Entity testEntity = new BasicZombie(null);
		board.placeEntity(1, 1,  testEntity);
		
		Entity[][] testEntities = new Entity[Board.HEIGHT][Board.WIDTH];
		
		for (int y = 0; y < Board.HEIGHT; y++) {
			for (int x = 0; x < Board.WIDTH; x++) {
				testEntities[y][x] = new BasicZombie(null);
				board.placeEntity(x, y, testEntities[y][x]);
			}
		}
		
		board.removeEntity(1, 1);
		
		for (int y = 0; y < Board.HEIGHT; y++) {
			for (int x = 0; x < Board.WIDTH; x++) {
				if(x == 1 && y == 1) {
					assertTrue("Occupant should be null at " + Integer.toString(x) + "," + Integer.toString(y),
							board.getEntity(x, y) == null);
				}else {
					assertTrue("Unexpected occupant at " + Integer.toString(x) + "," + Integer.toString(y),
							board.getEntity(x, y) == testEntities[y][x]);
				}
			}
		}

	}
	
	
	//@Test
	//public void testGetEntityTile() {
	//
	//}
	
	/**
	 * Ensure that checking if a coordinate is in bounds works
	 */
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

	/**
	 * Ensure that spending sun points works properly
	 */
	@Test
	public void testSpendSun() {
		assertEquals("Spending sun points should not be successfull, 0 sun points", board.spendSun(1),false);
		board.addSun(3);
		
		assertEquals("Should successfully spend sun points", board.spendSun(1),true);
		assertEquals("sun points was not decreased from 3 by 1 to 2 correcly", board.getSun(),2);
		assertEquals("Should successfully spend sun points", board.spendSun(2),true);
		assertEquals("sun points was not decreased from 2 by 2 to 0 correcly", board.getSun(),0);
	}
	
	/**
	 * ensure that adding sun points works properly
	 */
	@Test
	public void testAddSun() {
		board.addSun(1);
		assertEquals("sun should equal 1",board.getSun(),1);
		board.addSun(1);
		assertEquals("sun should equal 1",board.getSun(),2);
	}
	
	/**
	 * ensure that wiping the board properly resets everything
	 */
	@Test
	public void testWipe() {
		board.addSun(1);
		Entity[][] tiles = new Entity[Board.HEIGHT][Board.WIDTH];
		board.setTiles(tiles);
		board.wipe();
		for (int y = 0; y < Board.HEIGHT; y++) {
			for (int x = 0; x < Board.WIDTH; x++) {
				assertEquals("tile occupant was not removed at "  + Integer.toString(x) + "," + Integer.toString(y),
						board.getEntity(x, y), null);
			}
		}
		
		assertEquals("sunPoints were not reset to 0 ",board.getSun(),0);
	}
}
