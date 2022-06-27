package kalah.game;

/**
 * Outlines the functions every game implemented must observe
 */
public interface GameControl {

    /**
     * Starting the game
     */
    public void start();

    /**
     * Player has decided to quit
     */
    public void quitGame();

    /**
     * Game is over
     */
    public void gameOver();

    /**
     * Give next player a turn
     */
    public void changeTurn();

    /**
     * Make the player supplied have their turn first
     * @param player
     */
    public void chooseStartingPlayer(IPlayer player);

    /**
     * Adding game rules
     */
    public void addGameRules ();
}