package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JButton;

import org.junit.Before;
import org.junit.Test;

import mainPackage.Board;
import mainPackage.Controller;
import mainPackage.Entity;
import mainPackage.EntityFactory;
import mainPackage.GraphicsView;
import mainPackage.Level;
import mainPackage.View;
import mainPackage.plants.Melonpult;
import mainPackage.plants.Plant;
import mainPackage.plants.ProjectilePlant;
import mainPackage.plants.Repeater;
import mainPackage.plants.Sunflower;
import mainPackage.plants.Wallnut;
import mainPackage.zombies.Zombie;

public class TestController {
	Board board;
	GraphicsView graphicsView;
	Controller controller;
	
	/**
	 * setup MVC before performing test 
	 */
	@Before
	public void init() {
		controller= new Controller();
		graphicsView = new GraphicsView();
		board = new Board();
		Controller.controllerInit(board, graphicsView);
	}
	
	/**
	 * ensure that setting up a game resets the sunpoints to the correct amount and clears all old entities	
	 */
	@Test
	public void testSetupGame() {
		board.addSun(1);//pretend sunpoints exist from a previous round
		Entity[][] tiles = new Entity[Board.HEIGHT][Board.WIDTH];// add non null entities to the board tiles
		board.setTiles(tiles);
		
		Controller.setUpGame(board);
		
		
		for (int y = 0; y < Board.HEIGHT; y++) {
			for (int x = 0; x < Board.WIDTH; x++) {
				assertEquals("tile occupant was not removed at "  + Integer.toString(x) + "," + Integer.toString(y),
						board.getEntity(x, y), null);
			}
		}
		
		assertEquals("sunPoints were not set to the starting amount ",board.getSun(),Controller.START_SUN);
	}
	
	/**
	 * ensure that setting up a game resets the sun points, clears old entities and sets the turn to 0
	 */
	@Test
	public void testStartGame() {
		Controller.startGame();
		for (int y = 0; y < Board.HEIGHT; y++) {
			for (int x = 0; x < Board.WIDTH; x++) {
				assertEquals("tile occupant was not removed at "  + Integer.toString(x) + "," + Integer.toString(y),
						board.getEntity(x, y), null);
			}
		}
		
		assertEquals("sunPoints were not set to the starting amount",board.getSun(),Controller.START_SUN);
	}
	
	/**
	 * ensure the placing a plant is handeled correctly
	 */
	@Test
	public void testPlacePlant() {
		Controller.startGame();
		board.addSun( - Controller.START_SUN );
		
		assertEquals(board.getSun(),0);
		board.addSun( 50 );
		Controller.placePlant("wallnut", 0,0);
		board.addSun( 150 );
		Controller.placePlant("sunflower",0,1);
		board.addSun( 200 );
		Controller.placePlant("repeater",0,2);
		board.addSun( 300 );
		Controller.placePlant("melon", 0,3);

		Controller.placePlant("wallnut",1,0); // shouldn't have enought money
		Controller.placePlant("melon",0,0); // cant place because another plant exists there
		Controller.placePlant("wallnut", 5, 0); // out of placing bounds
		
		assertTrue("A wallnut should have been placed at 0,0 and a melon should have failed to be placed on the existing entity",
				board.getEntity(0, 0) instanceof Wallnut );
		assertTrue("a sunflower should have been placed at 0,1", 
				board.getEntity(0, 1) instanceof Sunflower );
		assertTrue("a repreater should have been placed at 0,2",
				board.getEntity(0, 2) instanceof Repeater );
		assertTrue("a melonpult should have been placed at 0,3",
				board.getEntity(0, 3) instanceof Melonpult);
		assertTrue("a wallnut should have failed to place at 1,0 from lack of sunpoints",
				board.getEntity(1, 0) == null);
		assertTrue("a wallnut should have failed to place at 5,0 from being out of placement bounds",
				board.getEntity(5, 0) == null);
	}

	/*@Test
	public void testStrCoordsToInt() {
		int[] res = Controller.strCoordsToInt("0", "0");
		assertFalse("should have received a proper result for 0,0",res==null);
		assertEquals("x should be 0",res[0],0);
		assertEquals("y should be 0",res[1],1);
		res = strCoordsToInt("-1", "0");
		assertEquals("x should be too small for a proper result",res,null);
		res = strCoordsToInt("0", "-1");
		assertEquals("y should be too small for a proper result",res,null);
		res = strCoordsToInt("0", Integer.toString(Board.HEIGHT));
		assertEquals("y should be too big for a proper result",res,null);
		res = strCoordsToInt( Integer.toString(Board.WIDTH),"0");
		assertEquals("x should be too big for a proper result",res,null);
	}*/
	
	/**
	 * ensure that ending a turn is handled correctly
	 */
	@Test
	public void testEndTurn() {
		Controller.startGame();
		Controller.placePlant("sunflower", 0, 0);
		Controller.endTurn();
		assertEquals(board.getSun(),50  );
		Integer zombieY = null;
		Entity zombie = null;
		for (int y = 0; y < Board.HEIGHT; y++) {
			if (board.getEntity(Board.WIDTH-1, y) != null) {
				zombieY = new Integer(y);
				zombie = board.getEntity(Board.WIDTH-1, y);
				break;
			}
		}
		
		assertTrue("A zombie should have spawned at the end of the first turn", zombieY != null);
		
		board.addSun(200);
		Controller.endTurn();
		Controller.endTurn();
		
		assertTrue("The zombie should have advanced forward", board.getEntity(Board.WIDTH-1 - 2*1, zombieY.intValue()) == zombie);//should make with different move speeds
		Controller.placePlant("repeater", 1, zombieY.intValue());
		
		int startHealth = zombie.getHp();
		Controller.endTurn();
		
		Controller.endTurn();
		Controller.endTurn();
		Controller.endTurn();
		assertTrue("The zombie should have lost health to the plant", zombie.getHp() < startHealth);
		
	}
	

	/**
	 * ensure the undo and redo work as expected
	 */
	@Test
	public void testUndoAndRedoTurn() {
		
		Controller.startGame();
		Controller.placePlant("sunflower", 0, 0);
		Controller.endTurn();
		
		assertEquals("There should be two stored board states at this time",board.getBoardStates().size(),2);
		
		
		Entity zombie = null;
		Integer zombieY = null;
		for (int y = 0; y < Board.HEIGHT; y++) {
			if (board.getEntity(Board.WIDTH-1, y) != null) {
				zombieY = new Integer(y);
				zombie = board.getEntity(Board.WIDTH-1, y);
				break;
			}
		}
		assertTrue("expected a zombie to spawn",zombieY != null);
		Controller.endTurn();
		assertEquals(board.getBoardStates().size(),3);
		assertFalse("The zombie should have advanced forward" , board.getEntity(Board.WIDTH-2, zombieY.intValue()) == null);//should make work with different move speeds
		board.undo();
		assertEquals(board.getBoardStates().size(),2);
		assertEquals(board.getUndoneBoardStates().size(),1);
		assertTrue("The zombie didn't get moved to it's previous position", board.getEntity(Board.WIDTH-1, zombieY) != null);
		board.undo();
		assertEquals(board.getUndoneBoardStates().size(),2);
		assertEquals(board.getBoardStates().size(),1);
		assertEquals("The zombie should now have been undone from existance" , board.getEntity(Board.WIDTH-1, zombieY), null);
		assertEquals("The plant should now have been undone from existance" , board.getEntity(0, 0), null);
		board.redo();
		assertEquals(board.getUndoneBoardStates().size(),1);
		assertEquals(board.getBoardStates().size(),2);
		assertFalse("The zombie should be placed back at its original position" , board.getEntity(Board.WIDTH-1, zombieY) == null);
		assertFalse("The plant should be placed back at its original positon" , board.getEntity(0, 0)== null);
		board.redo();
		assertEquals(board.getUndoneBoardStates().size(),0);
		assertEquals(board.getBoardStates().size(),3);
		assertFalse("The zombie should have advanced forward" , board.getEntity(Board.WIDTH-2, zombieY) == null);//should make work with different movement speeds
		assertFalse("The plant should continue to exist" , board.getEntity(0, 0)== null);
		
	}
		
}
