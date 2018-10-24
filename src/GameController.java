public class GameController{

    private int totalSun;

    public GameController(){
        totalSun = 0;
    }

    public void sunPlusPlus(){
        totalSun++;
    }
    
    public int getSun(){
        return totalSun;
    }

}
