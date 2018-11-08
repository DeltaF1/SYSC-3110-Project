package mainPackage;

public interface View {
	
	public void drawBoard(Board board);
	
	public void announce(String message);
	
	public void drawMenu();
	
	public void drawGameOver();
	
	public void drawWinScreen();
	
}
