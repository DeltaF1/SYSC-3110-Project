package mainPackage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class GraphicsView implements View
{
	private JFrame frame;
	private JPanel menuPanel;
	
	private JPanel boardPanel;
	private BoardButton[][] boardButtons;
	private JLabel sunLabel;
	
	private JPanel endPanel;
	private JLabel statusText;
	private String selectedPlant;
	
	private static ImageIcon zombieIcon = new ImageIcon("images/zombie.png");
	private static ImageIcon sunflowerIcon = new ImageIcon("images/sunflower.png");
	private static ImageIcon peashooterIcon = new ImageIcon("images/peashooter.png");
	private static ImageIcon blankIcon = new ImageIcon("images/blank.png");
	
	/**
	 * A button that represents a square on the board grid
	 */
	private class BoardButton extends JButton {
		private int x,y;
		public BoardButton(int x, int y) {
			super();
			this.x = x;
			this.y = y;
			
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
	
	/**
	 * A button that can be used to choose which seed to plant
	 */
	private class PlantButton extends JButton {
		public String plantType;
		
		public PlantButton (String plantType) {
			super();
			
			setPreferredSize(new Dimension(48,48));
			
			this.plantType = plantType;
			
			ImageIcon icon;
			switch (plantType) {
				default:
					icon = sunflowerIcon; 
			}
			setIcon(GraphicsView.getIcon(plantType));
			
			addActionListener(new ActionListener()
			{
				
				@Override
				public void actionPerformed(ActionEvent e)
				{
					// TODO Auto-generated method stub
					if (selectedPlant == plantType) {
						selectedPlant = null;
					} else {
						selectedPlant = plantType;
					}
				}
			});
		}
	}
	
	public GraphicsView()
	{
		frame = new JFrame("Plants Vs. Zombies - Now with graphics!");
		
		// Menu Bar setup
			JMenuBar menuBar = new JMenuBar();
			JMenu fileMenu = new JMenu("File");
	
			menuBar.add(fileMenu);
			
			frame.setJMenuBar(menuBar);
		
		// Main menu panel setup
			menuPanel = new JPanel();
			JButton startGame = new JButton("Start Game");
			
			startGame.addActionListener(new ActionListener()
			{
				
				@Override
				public void actionPerformed(ActionEvent e)
				{
					Controller.startGame();
					
				}
			});
			
			menuPanel.add(new JLabel("PvZ"));
			menuPanel.add(startGame);
			
		// Board panel setup
			boardPanel = new JPanel();

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
			
			JButton endTurnButton = new JButton("End turn");
			endTurnButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					Controller.endTurn();
				}
			});
			boardPanel.add(endTurnButton);
			
			// Create a panel to hold the buttons that choose which seed to plant
			JPanel plantPanel = new JPanel();
			
			PlantButton sunflowerButton = new PlantButton("sunflower");
			plantPanel.add(sunflowerButton);
			
			PlantButton projButton = new PlantButton("proj");
			plantPanel.add(projButton);
			
			boardPanel.add(plantPanel, BorderLayout.SOUTH);
			
			sunLabel = new JLabel("THIS TEXT SHOULD NOT BE SEEN");
			boardPanel.add(sunLabel);

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
	
	/**
	 * Handles click events from the board. If a plant is selected to plant, attempt to plant it via the controller
	 */
	protected void clickButton(BoardButton button, int x, int y)
	{
		if (selectedPlant == null) {
			Controller.tileInfo(x, y);
		} else {
			Controller.placePlant(selectedPlant, x, y);
			selectedPlant = null;
		}
	}

	@Override
	/**
	 * Draws out the model onto a grid of buttons
	 */
	public void drawBoard(Board board)
	{

		if (frame.getContentPane() != boardPanel) {
			frame.setContentPane(boardPanel);
		}
		
		for (int i = 0; i < Board.HEIGHT; i++) {
			for (int j = 0; j < Board.WIDTH; j++) {
				Entity entity = board.getEntity(j, i); 
				// Todo, create a map of types to ImageIcons?
				BoardButton button = boardButtons[i][j];
				if (entity instanceof Zombie) {
					button.setIcon(zombieIcon);
				} else if (entity instanceof Plant) {
					if (entity instanceof Sunflower) {
						button.setIcon(sunflowerIcon);
					} else if (entity instanceof ProjectilePlant) {
						button.setIcon(peashooterIcon);
					}
				} else if (entity == null) {
					button.setIcon(blankIcon);
				}
			}
		}
		
		sunLabel.setText("Sun: "+board.getSun());
		
		System.out.println("Updated View!");
		refreshFrame();

	}
	
	/**
	 * Returns the correct image to use for the given plant
	 * 
	 * @param plant
	 * @return The ImageIcon associated with the plant name
	 */
	private static ImageIcon getIcon(String plant) {
		switch(plant) {
			
			case "proj":
				return peashooterIcon;
			case "sunflower":
			default:
				return sunflowerIcon;
		}
	}
	
	@Override
	public void announce(String message)
	{
		System.out.println(message);
	}

	@Override
	/**
	 * Draws the menu panel
	 */
	public void drawMenu()
	{
		frame.setContentPane(menuPanel);
		refreshFrame();
	}

	@Override
	/**
	 * Draws the end-screen card and sets the status to game over 
	 */
	public void drawGameOver()
	{
		statusText.setText("Game Over!");
		frame.setContentPane(endPanel);
		refreshFrame();
	}

	@Override
	/**
	 * Draws the end-screen card and sets the status to win
	 */
	public void drawWinScreen()
	{
		statusText.setText("You win!");
		frame.setContentPane(endPanel);
		refreshFrame();
	}
	
	/*
	 * Redraws and resizes the JFrame 
	 */
	private void refreshFrame() {
		frame.pack();
		frame.revalidate();
	}

}
