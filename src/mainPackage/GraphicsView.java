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
import java.util.LinkedList;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import mainPackage.zombies.BasicZombie;
import mainPackage.zombies.Boomer;
import mainPackage.zombies.Doomer;
import mainPackage.zombies.Zoomer;

public class GraphicsView implements View
{
	private JFrame frame;
	private JPanel menuPanel;
	
	private JPanel boardPanel;
	
	//private JPanel editorPanel;
	
	private BoardButton[][] boardButtons;
	private JTextPane sunInfo;
	private JTextPane announcements;
	
	private JPanel endPanel;
	private JLabel statusText;
	private PlantButton selectedPlantButton;
	

	LinkedList<ZombieSpawnSettings> zombSettings = new LinkedList<ZombieSpawnSettings> ();

	
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
	
	/**
	 * sort a linked list of ZombieSpawnSettings by spawnTurn then alphabetical order of name
	 * @param unsorted list
	 * @return sorted list
	 */
	public LinkedList<ZombieSpawnSettings> sortZombSettings( LinkedList<ZombieSpawnSettings> unsorted ){
		unsorted = new LinkedList<ZombieSpawnSettings>(unsorted); //make a copy
		int size = unsorted.size();
		LinkedList<ZombieSpawnSettings> sorted = new LinkedList<ZombieSpawnSettings>();
		ZombieSpawnSettings selected = null;
		
		for(int i = 0; i < size; i++) {
			for (ZombieSpawnSettings curr : unsorted) {
				if (selected == null || selected.getSpawnTurn() > curr.getSpawnTurn() || (selected.getSpawnTurn() == curr.getSpawnTurn() && selected.getName().compareTo( curr.getName()  ) > 0 )) {
					selected = curr;
				}
			}
			sorted.add(selected);
			unsorted.remove(selected);
			selected = null;
		}
		
		return sorted;
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
	
	JButton startGame;
	public JButton getStartGameButton() {
		return startGame;
	}
	
	JButton openLevelEditor;
	public JButton getOpenLevelEditorButton() {
		return openLevelEditor;
	}
	
	//JButton addZombie;
	//JButton removeZombie;
	
	/**
	 * used by tests to simulate clicking on a button
	 * @param x
	 * @param y
	 */
	public void clickBoardButton(int x , int y){
		boardButtons[y][x].doClick();
	}
	
	JButton endTurnButton;
	public JButton getEndTurnButton() {
		return endTurnButton;
	}
	
	JButton undoButton;
	public JButton getUndoButton() {
		return undoButton;
	}
	
	JButton redoButton;
	public JButton getRedoButton() {
		return redoButton;
	}
	
	PlantButton sunflowerButton;// = new PlantButton("sunflower");
	public JButton getSunflowerButton() {
		return sunflowerButton;
	}
	
	
	PlantButton projButton;// = new PlantButton("proj");
	public JButton getProjButton() {
		return projButton;
	}
	
	PlantButton repeaterButton;// = new PlantButton("repeater");
	public JButton getRepeaterButton() {
		return repeaterButton;
	}
	
	PlantButton melonButton;// = new PlantButton("melon");
	public JButton getMelonButton() {
		return melonButton;
	}
	
	PlantButton wallnutButton;// = new PlantButton("wallnut");
	public JButton getWallnutButton() {
		return wallnutButton;
	}

	
	/// Editor Panel objects
	
    DefaultListModel<Object> zombieModel;
    JList<Object> zombieList;// = new JList(zombieModel);
    JScrollPane jScrollPane;// = new JScrollPane(zombieList);
    JPanel editorPanel;// = new JPanel();
	JButton addZombie;// = new JButton("add zombie");
	JButton removeZombie; // = new JButton("remove zombie");
	JButton editZombie;
	JButton saveLevel;
	JButton leaveEditor;
	
	
    ///BUDDY INFO LABELS
    JLabel jTypeLabel;// = new JLabel("Zombie Type:");
    //editorPanel.add(jTypeLabel);
    JLabel jSpawnTurnLabel;// = new JLabel("Spawn turn:");
    //editorPanel.add(jSpawnTurnLabel);
    
	
	/**
	 * returns whether or not a ZombieSpawnSettings object contains valid data
	 * @param aZombSetting
	 * @return whether or not the ZombieSpawnSettings object contains valid data
	 */
	public boolean verifyZombieSettings(ZombieSpawnSettings aZombSetting) {

		if (aZombSetting != null && aZombSetting.getSpawnTurn() >= 0 && 
				(aZombSetting.getName().toLowerCase().equals( "basic") ||
				aZombSetting.getName().toLowerCase().equals( "boomer") ||
				aZombSetting.getName().toLowerCase().equals(  "doomer") ||
				aZombSetting.getName().toLowerCase().equals( "zoomer") )) {
			return true;
		}
		return false;
	}
	
	/**
	 * creates a ZombieSpawnSettings object using user input
	 * @return the ZombieSpawnSettings object created
	 */
	public ZombieSpawnSettings getZombSettings() {
		ZombieSpawnSettings zombSettings = null;
		boolean firstLoop = true;
		while (! verifyZombieSettings(zombSettings)){
			if (!firstLoop) {
				JOptionPane.showMessageDialog(null, "Invalid input! Zombie Type should be 'basic','boomer','doomer' or 'zoomer'. Spawn Turn should be greater than -1.");
			}
			String zombieType = JOptionPane.showInputDialog("Zombie type: "); //TODO: handle invalid inputs
			try {
				int spawnTurn = Integer.parseInt(JOptionPane.showInputDialog("Spawn turn: "));
				zombSettings = new ZombieSpawnSettings(zombieType,spawnTurn);
			} catch( NumberFormatException e) {
				zombSettings = null;
			}
			firstLoop = false;
		}
		return zombSettings;
	}
	
	public GraphicsView()
	{
		frame = new JFrame("Plants Vs. Zombies - Now with time travel!");
		
		// Menu Bar setup
			JMenuBar menuBar = new JMenuBar();
			JMenu fileMenu = new JMenu("File");
			
			JMenuItem save = new JMenuItem("Save");
			save.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JButton openButton = new JButton();
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setCurrentDirectory(new File("."));
					fileChooser.showOpenDialog(openButton); //choose the file
					File selectedFile = fileChooser.getSelectedFile();
					if (selectedFile != null) { //if user chose a file instead of hitting "cancel"
						Controller.saveGame(selectedFile.getAbsolutePath());
					} else {
						announce("Game save canceled");
					}
				}
			});
			
			JMenuItem load = new JMenuItem("Load");
			load.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("unimplemented");
				}
			});
			
			fileMenu.add(save);
	
			menuBar.add(fileMenu);
			
			frame.setJMenuBar(menuBar);
		
		// Main menu panel setup
			menuPanel = new MenuPanel();
			startGame = new JButton("Start Game");
			openLevelEditor = new JButton("Open Level Editor");
			
			
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
			
			openLevelEditor.addActionListener(new ActionListener()
			{
				
				@Override
				public void actionPerformed(ActionEvent e)
				{
					drawLevelEditor();
					refreshFrame();
					//frame.setSize(new Dimension(960,650)); //pretty weird but if we don't resize then the size of the frame is all messed up... calling frame.pack() is supposed to fix this but for some reason it doesn't
				}
			});
			
			menuPanel.add(startGame);
			menuPanel.add(openLevelEditor);
		
		//level editor setup
		    zombieModel = new DefaultListModel<Object>();
		    zombieList = new JList<Object>(zombieModel);
		    jScrollPane = new JScrollPane(zombieList);
		    zombieList.addListSelectionListener( new ListSelectionListener() {
		        public void valueChanged(ListSelectionEvent listSelectionEvent) {
		        	if(!zombieList.getValueIsAdjusting()) {
		        		int selectedIndex = zombieList.getSelectedIndex();
		        		if (selectedIndex != -1) {
		        			Controller.editorSelectZombie(  editorZombList.get( selectedIndex )  );
		        		}else {
		        			Controller.editorSelectZombie(  null );
		        		}
		        	}
		        }
		    });
		    editorPanel = new JPanel();
		    editorPanel.add(jScrollPane );//, BorderLayout.WEST);
		    editorPanel.setLayout(new BoxLayout(editorPanel, BoxLayout.PAGE_AXIS));
			
		    //jButtonNewBuddy.addActionListener(this);
			addZombie = new JButton("add zombie");
			addZombie.addActionListener( new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						ZombieSpawnSettings inpSettings = getZombSettings();
						Controller.editorAddZombie( inpSettings );	
					}
				}
			);
						
			removeZombie = new JButton("remove zombie");
			removeZombie.addActionListener( new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						Controller.editorRemoveZombie();	//selected
					}
				}
			);
			
			editZombie = new JButton("edit zombie");
			editZombie.addActionListener( new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					
					Controller.editorEditZombie( getZombSettings() );	
				}
			}
		);
			
			saveLevel = new JButton("save level");
			saveLevel.addActionListener( new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					String levelName = "default";
					boolean notSaved = true;
				
					while ( notSaved || levelName.equals("default") ) {
						
						levelName = JOptionPane.showInputDialog("level name: ");
						
						if (levelName == null || levelName == "" || levelName == "default" || levelName == "default.xml") {
							JOptionPane.showMessageDialog(null, "you must enter a level name that is not empty or 'default'!");
						}else {
							notSaved = false;
							if (levelName.endsWith(".xml")){
								levelName = levelName.substring(0, levelName.length() - 4);
							}
							Controller.writeEditorFileToDisk( "levels//"+levelName + ".xml");
						}
					}
						
						
				}
			});

			
			
			leaveEditor = new JButton("leave editor");
			leaveEditor.addActionListener( new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					drawMenu();
				}
			}
		);

			editorPanel.add(addZombie);
			editorPanel.add(removeZombie);
			editorPanel.add(editZombie);
			editorPanel.add(leaveEditor);
			editorPanel.add(saveLevel);
			
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
			
			endTurnButton = new JButton("End turn");
			endTurnButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					Controller.endTurn();
				}
			});
			
			undoButton = new JButton("Undo turn");
			undoButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Controller.undoTurn();
				}
			});
			
			redoButton = new JButton("Redo turn");
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
			
			sunflowerButton = new PlantButton("sunflower");
			projButton = new PlantButton("proj");
			repeaterButton = new PlantButton("repeater");
			melonButton = new PlantButton("melon");
			wallnutButton = new PlantButton("wallnut");
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
	public void drawGame()
	{
		if (frame.getContentPane() != boardPanel) {
			frame.setContentPane(boardPanel);
		}
	}
	
	/**
	 * Draws the pane for the level editor
	 */
	public void drawLevelEditor() {
		if(frame.getContentPane() != editorPanel) {
			frame.setContentPane(editorPanel);
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

	/**
	 * updates the list of zombies that are in the editor spawn list
	 */
	LinkedList< ZombieSpawnSettings > editorZombList;
	@Override
	public void updateZombSettings(TreeMap<Integer, LinkedList<String>> spawns) {
		editorZombList = new LinkedList<ZombieSpawnSettings>();
		for (int wave : spawns.keySet()) {
			for (String zombieType : spawns.get(wave)) {
				editorZombList.add( new ZombieSpawnSettings( zombieType, wave  ) );
			}			
		}
		editorZombList = sortZombSettings( editorZombList );
		
		zombieModel.removeAllElements();
		for (ZombieSpawnSettings z : editorZombList) {
			zombieModel.addElement(  z.getSpawnTurn() + " → " + z.getName() );
		}
		
	}

	/*@Override
	public void updateZombSettings(LinkedList<ZombieSpawnSettings> zombSettings) {
		// TODO Auto-generated method stub
		
	}*/
	
	/*public void updateZombSettings(LinkedList<ZombieSpawnSettings> zombSettings) {
		zombieModel.removeAllElements();
		for (ZombieSpawnSettings z: zombSettings) {
			zombieModel.addElement(  z.getSpawnTurn() + " > " + z.getName() );
		}
	}*/
	
}
