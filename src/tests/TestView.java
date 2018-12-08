package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import mainPackage.Board;
import mainPackage.Controller;
import mainPackage.GraphicsView;
import mainPackage.ZombieSpawnSettings;
import mainPackage.plants.Melonpult;
import mainPackage.plants.Repeater;
import mainPackage.plants.Sunflower;
import mainPackage.plants.Wallnut;

public class TestView {
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
	 * ensure starting a game works as expected
	 */
	@Test
	public void testStartGame() {
		graphicsView.getStartGameButton().doClick();
		assertEquals()
	}
	
	/**
	 * ensure placing plants works as expected
	 */
	@Test
	public void testPlacePlant() {
		graphicsView.getStartGameButton().doClick();
		board.addSun( - Controller.START_SUN );
		
		assertEquals(board.getSun(),0);
		board.addSun( 50 );

		graphicsView.getWallnutButton().doClick();
		graphicsView.clickBoardButton(0,0);		
		board.addSun( 150 );
		graphicsView.getSunflowerButton().doClick();
		graphicsView.clickBoardButton(0,1);		
		board.addSun( 200 );
		graphicsView.getRepeaterButton().doClick();
		graphicsView.clickBoardButton(0,2);	
		board.addSun( 300 );
		graphicsView.getMelonButton().doClick();
		graphicsView.clickBoardButton(0,3);	

		graphicsView.getWallnutButton().doClick();
		graphicsView.clickBoardButton(1,0);	
		board.addSun(300);
		graphicsView.getMelonButton().doClick();
		graphicsView.clickBoardButton(0,0);	
		
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
	
	/**
	 * ensure the undo and redo work as expected
	 */
	@Test
	public void testEndUndoAndRedoTurn() {
		graphicsView.getStartGameButton().doClick();
		graphicsView.getEndTurnButton().doClick();
		assertEquals("ensure that the game was advanced a turn by clicking end turn",
				board.getTurn(),1);
		assertEquals("ensure board states for undoing are being stored",
				board.getBoardStates().size(),2);
		graphicsView.getEndTurnButton().doClick();
		assertEquals("turns should always increase on clicking end turn",board.getTurn(),2);
		assertEquals("the stored board states did not increase",board.getBoardStates().size(),3);
		graphicsView.getUndoButton().doClick();
		assertEquals("the stored board states did not decrease from an undo",board.getBoardStates().size(),2);
		assertEquals("The stored undone board states did not increase from an undo",board.getUndoneBoardStates().size(),1);
		graphicsView.getRedoButton().doClick();
		assertEquals("The stared board states should increase again from a redo",board.getBoardStates().size(),3);
		assertEquals("The stored undone board states should now descrease from a redo",board.getUndoneBoardStates().size(),0);
	}
	
	/**
	 * ensures that the function for preventing incorrect zombie settings from being entered in the view works correctly
	 */
	@Test
	public void testVerifyZombieSettings() {
		//If a test fails it is returning false when it should be a valid input or true when it is an invalid input
		assertEquals(graphicsView.verifyZombieSettings(new ZombieSpawnSettings("doomer",0)),true  );
		assertEquals(graphicsView.verifyZombieSettings(new ZombieSpawnSettings("Doomer",0)),true  );
		assertEquals(graphicsView.verifyZombieSettings(new ZombieSpawnSettings("boomer",0)),true  );
		assertEquals(graphicsView.verifyZombieSettings(new ZombieSpawnSettings("zoomer",0)),true  );
		assertEquals(graphicsView.verifyZombieSettings(new ZombieSpawnSettings("basic",0)),true  );
		assertEquals(graphicsView.verifyZombieSettings(new ZombieSpawnSettings("invalid name",0)),false  );
		assertEquals(graphicsView.verifyZombieSettings(new ZombieSpawnSettings("doomer",-1)),false  );
		assertEquals(graphicsView.verifyZombieSettings(new ZombieSpawnSettings("invalid name",-1)),false  );
	}
}