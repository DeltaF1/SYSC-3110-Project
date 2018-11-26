package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import mainPackage.Board;
import mainPackage.Controller;
import mainPackage.GraphicsView;
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
	
	@Test
	public void testStartGame() {
		graphicsView.getStartGameButton().doClick();
		assertEquals( board.getTurn(), 0);
		for (int y = 0; y < Board.HEIGHT; y++) {
			for (int x = 0; x < Board.WIDTH; x++) {
				assertEquals("tile occupant was not removed at "  + Integer.toString(x) + "," + Integer.toString(y),
						board.getEntity(x, y), null);
			}
		}
		
		assertEquals("sunPoints were not set to the starting amount",board.getSun(),Controller.START_SUN);
		
/*		sunflowerButton = new PlantButton("sunflower");
		PlantButton projButton = new PlantButton("proj");
		PlantButton repeaterButton = new PlantButton("repeater");
		PlantButton melonButton = new PlantButton("melon");
		PlantButton wallnutButton*/
		
		
	}
	
	
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
		assertEquals(board.getTurn(),1);
		assertEquals(board.getBoardStates().size(),2);
		graphicsView.getEndTurnButton().doClick();
		assertEquals(board.getTurn(),2);
		assertEquals(board.getBoardStates().size(),3);
		graphicsView.getUndoButton().doClick();
		assertEquals(board.getBoardStates().size(),2);
		assertEquals(board.getUndoneBoardStates().size(),1);
		graphicsView.getRedoButton().doClick();
		assertEquals(board.getBoardStates().size(),3);
		assertEquals(board.getUndoneBoardStates().size(),0);
	}
	
}