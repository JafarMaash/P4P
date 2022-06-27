package kalah.ui;


import kalah.game.GameState;

/**
 * Interface to be used for various types of user interfaces
 */
public interface GameInterface {

    /**
     * Outputs the Mancala board
     */
    public void outputBoard();

    /**
     * Gets player input
     * @param playerName
     * @return
     */
    public int getPlayerInput(String playerName);

    /**
     * Outputs the details of the finished game
     * @param playerQuit
     */
    public void outputGameOver(boolean playerQuit);

    /**
     * Outputs error message
     * @param errorType
     */
    public void outputErrorMessage (GameState errorType);
}