package mainPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	
	/**
	 * A button that represents a square on the board grid
	 */
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
	
	/**
	 * A button that can be used to choose which seed to plant
	 */
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
		
		/*
		 * @return the plant type for this PlantButton
		 */
		String getPlantType() {
			return plantType;
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
					frame.setSize(new Dimension(960,650)); //pretty weird but if we don't resize then the size of the frame is all messed up... calling frame.pack() is supposed to fix this but for some reason it doesn't
				}
			});
			
			menuPanel.add(new JLabel("PvZ"));
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
			undoButton.addActionListener(new ActionListener() {
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
			controlsPanel.add(sunflowerButton);
			
			PlantButton projButton = new PlantButton("proj");
			controlsPanel.add(projButton);
			
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
	
	/**
	 * Handles click events from the board. If a plant is selected to plant, attempt to plant it via the controller
	 */
	protected void clickButton(BoardButton button, int x, int y)
	{
		if (selectedPlantButton == null) {
			Controller.tileInfo(x, y);
		} else {
			Controller.placePlant(selectedPlantButton.getPlantType(), x, y);
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
		/*
		for (int i = 0; i < Board.HEIGHT; i++) {
			for (int j = 0; j < Board.WIDTH; j++) {
				Entity entity = board.getEntity(j, i); 
				// Todo, create a map of types to ImageIcons?
				BoardButton button = boardButtons[i][j];
				if (entity instanceof Zombie) {
					button.setIcon(Images.zombieIcon);
				} else if (entity instanceof Plant) {
					if (entity instanceof Sunflower) {
						button.setIcon(Images.sunflowerIcon);
					} else if (entity instanceof ProjectilePlant) {
						button.setIcon(Images.peashooterIcon);
					}
				} else if (entity == null) {
					button.setIcon(Images.blankIcon);
				}

		}
		
		sunInfo.setText("<b>Sun: " + board.getSun() + "</b>"); //its kind of ugly but the easiest way to bold is with html tags
		centerText(sunInfo);
		
		System.out.println("Updated View!");
		refreshFrame();*/
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
	
	/*
	 * Centers text
	 * @param pane the JTextPane to center text in
	 */
	private void centerText(JTextPane pane) {
		StyledDocument text = pane.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		text.setParagraphAttributes(0, text.getLength(), center, false);
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
		frame.revalidate();
		frame.pack();
	}

}
