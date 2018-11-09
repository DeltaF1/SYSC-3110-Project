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
	private JPanel endPanel;
	private String selectedPlant;
	
	private static ImageIcon zombieIcon = new ImageIcon("images/zombie.png");
	private static ImageIcon sunflowerIcon = new ImageIcon("images/sunflower.png");
	private static ImageIcon peashooterIcon = new ImageIcon("images/peashooter.png");
	private static ImageIcon blankIcon = new ImageIcon("images/blank.png");
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
	
	public GraphicsView(Board board)
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
			
			JPanel gameBoardPanel = new JPanel();
			gameBoardPanel.setLayout(new GridLayout(board.HEIGHT, board.WIDTH));
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
			JPanel plantPanel = new JPanel();
			
			PlantButton sunflowerButton = new PlantButton("sunflower");
			plantPanel.add(sunflowerButton);
			
			PlantButton projButton = new PlantButton("proj");
			plantPanel.add(projButton);
			
			boardPanel.add(plantPanel, BorderLayout.SOUTH);
			
			
		frame.setContentPane(menuPanel);
		frame.pack();
		frame.setVisible(true);
	}
	
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
	public void drawBoard(Board board)
	{
		System.out.println("Updated View!");
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
		refreshFrame();

	}

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
	public void drawMenu()
	{
		frame.setContentPane(menuPanel);
		refreshFrame();

	}

	@Override
	public void drawGameOver()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void drawWinScreen()
	{
		// TODO Auto-generated method stub

	}
	
	private void refreshFrame() {
		frame.pack();
		frame.revalidate();
	}

}
