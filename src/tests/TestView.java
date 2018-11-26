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
	
	
	
	
	/*
	 
	 package mainPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class GraphicsView implements View
{
	private JFrame frame;
	private JPanel menuPanel;
	
	private JPanel boardPanel;
	private BoardButton[][] boardButtons;
	private JTextPane sunInfo;
	private JTextPane announcements;
	
	private JPanel endPanel;
	private JLabel statusText;
	private PlantButton selectedPlantButton;
	

	private class BoardButton extends JButton {
		private int x,y;
		public BoardButton(int x, int y) {
			super();
			this.x = x;
			this.y = y;
			
//			setOpaque(false);
//			setContentAreaFilled(false);
//			setBorderPainted(false);
			
			setPreferredSize(new Dimension(48,48));
			
			addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					clickButton((BoardButton)e.getSource(), x, y);
					System.out.println(x + "," + y+" clicked");
				}
			});
		}
	}
	

	private class PlantButton extends JButton {
		public String plantType;
		
		public PlantButton (String plantType) {
			super();
			
			setPreferredSize(new Dimension(48,48));
			
			this.plantType = plantType;
			
			setIcon(Images.getIcon(plantType));
			
			PlantButton thisButton = this;
			
			addActionListener(new ActionListener()
			{
				
				@Override
				public void actionPerformed(ActionEvent e)
				{
					// TODO Auto-generated method stub
					if (selectedPlantButton != null) {
						selectedPlantButton.setBorder(new LineBorder(Color.GRAY));
					}
					if (selectedPlantButton == thisButton) {
						selectedPlantButton = null;
					} else {
						selectedPlantButton = thisButton;
						thisButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
					}
				}
			});
		}
		

		String getPlantType() {
			return plantType;
		}
	}
	
	private class MenuPanel extends JPanel {
		private BufferedImage bgImage;
		
		public MenuPanel () {
			super();
			try {
				bgImage = ImageIO.read(new File("images/splash.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			setPreferredSize(new Dimension(bgImage.getWidth(), bgImage.getHeight()));
		}
		@Override
		  protected void paintComponent(Graphics g) {

		    super.paintComponent(g);
		    g.drawImage(bgImage, 0, 0, null);
		}
	}
	
	public GraphicsView()
	{
		frame = new JFrame("Plants Vs. Zombies - Now with time travel!");
		
		// Menu Bar setup
			JMenuBar menuBar = new JMenuBar();
			JMenu fileMenu = new JMenu("File");
	
			menuBar.add(fileMenu);
			
			frame.setJMenuBar(menuBar);
		
		// Main menu panel setup
			menuPanel = new MenuPanel();
			JButton startGame = new JButton("Start Game");
			
			startGame.addActionListener(new ActionListener()
			{
				
				@Override
				public void actionPerformed(ActionEvent e)
				{
					Controller.startGame();
					refreshFrame();
					//frame.setSize(new Dimension(960,650)); //pretty weird but if we don't resize then the size of the frame is all messed up... calling frame.pack() is supposed to fix this but for some reason it doesn't
				}
			});
			
			menuPanel.add(startGame);
			
		// Board panel setup
			boardPanel = new JPanel();
			boardPanel.setLayout(new BoxLayout(boardPanel, BoxLayout.PAGE_AXIS));

			// Create a panel that contains the grid of buttons 
			JPanel gameBoardPanel = new JPanel();
			gameBoardPanel.setLayout(new GridLayout(Board.HEIGHT, Board.WIDTH));

			// Generate buttons
			boardButtons = new BoardButton[Board.HEIGHT][Board.WIDTH];
			for (int i = 0; i < Board.HEIGHT; i++) {
				for (int j = 0; j < Board.WIDTH; j++) {
					BoardButton button = new BoardButton(j, i);
					boardButtons[i][j] = button;
					gameBoardPanel.add(button);
				}
			}
			
			boardPanel.add(gameBoardPanel);
			
			JPanel controlsAndInfoPanel = new JPanel(); //for the buttons and announcement
			controlsAndInfoPanel.setLayout(new BoxLayout(controlsAndInfoPanel, BoxLayout.PAGE_AXIS));
			
			sunInfo = new JTextPane();
			sunInfo.setEditable(false);
			sunInfo.setOpaque(false);
			sunInfo.setContentType("text/html");
			controlsAndInfoPanel.add(sunInfo);
			
			JButton endTurnButton = new JButton("End turn");
			endTurnButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					Controller.endTurn();
				}
			});
			
			JButton undoButton = new JButton("Undo turn");
			undoButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Controller.undoTurn();
				}
			});
			
			JButton redoButton = new JButton("Redo turn");
			redoButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Controller.redoTurn();
				}
			});
			
			// Create a panel to hold the buttons that choose which seed to plant
			JPanel controlsPanel = new JPanel();
			controlsPanel.add(endTurnButton);
			controlsPanel.add(undoButton);
			controlsPanel.add(redoButton);
			
			PlantButton sunflowerButton = new PlantButton("sunflower");
			PlantButton projButton = new PlantButton("proj");
			PlantButton repeaterButton = new PlantButton("repeater");
			PlantButton melonButton = new PlantButton("melon");
			PlantButton wallnutButton = new PlantButton("wallnut");
			controlsPanel.add(sunflowerButton);
			controlsPanel.add(projButton);
			controlsPanel.add(repeaterButton);
			controlsPanel.add(melonButton);
			controlsPanel.add(wallnutButton);
			
			controlsAndInfoPanel.add(controlsPanel);
			
			announcements = new JTextPane();
			announcements.setEditable(false);
			announcements.setOpaque(false);
			controlsAndInfoPanel.add(announcements);
			
			boardPanel.add(controlsAndInfoPanel, BorderLayout.SOUTH);

		// End screen panel setup
			endPanel = new JPanel();
			statusText = new JLabel("test");
			JButton mainMenuButton = new JButton("Main Menu");
			mainMenuButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					drawMenu();
				}
			});
			
			endPanel.add(statusText);
			endPanel.add(mainMenuButton);
			
		frame.setContentPane(menuPanel);
		frame.setMinimumSize(new Dimension(300, 300));
		frame.pack();
		frame.setVisible(true);
	}
	

	protected void clickButton(BoardButton button, int x, int y)
	{
		if (selectedPlantButton == null) {
			Controller.tileInfo(x, y);
		} else {
			Controller.placePlant(selectedPlantButton.getPlantType(), x, y);
		}
	}

	@Override

	public void drawGame()
	{
		if (frame.getContentPane() != boardPanel) {
			frame.setContentPane(boardPanel);
		}
	}
	
	public void updateEntity(Entity entity, int x, int y) {
		BoardButton button = boardButtons[y][x];
		if (entity == null || entity.getIcon() == null) {
			button.setIcon(Images.blankIcon);
		} else {
			button.setIcon(entity.getIcon());
		}
		refreshFrame();
	}
	
	public void updateSun(int sun) {
		sunInfo.setText("<b>Sun: " + sun + "</b>"); // It's kind of ugly but the easiest way to bold is with html tags
		centerText(sunInfo);
		refreshFrame();
	}
	
	@Override
	public void announce(String message)
	{
		announcements.setText(message);
		centerText(announcements);
	}
	
	private void centerText(JTextPane pane) {
		StyledDocument text = pane.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		text.setParagraphAttributes(0, text.getLength(), center, false);
	}

	@Override

	public void drawMenu()
	{
		frame.setContentPane(menuPanel);
		refreshFrame();
	}

	@Override

	public void drawGameOver()
	{
		statusText.setText("Game Over!");
		frame.setContentPane(endPanel);
		refreshFrame();
	}

	@Override

	public void drawWinScreen()
	{
		statusText.setText("You win!");
		frame.setContentPane(endPanel);
		refreshFrame();
	}

	private void refreshFrame() {
		frame.revalidate();
		frame.pack();
	}

}

	 
	 */
	
	
	
}
