package kalah.game;

/**
 * Interface for rule classes
 */
public interface GameRules {

    /**
     * Must execute the rule defined by implementing class
     *
     * @param player1 IPlayer
     * @param player2 IPlayer
     * @param index int representing houseIndex or seeds
     * @param state GameState current state of the game
     * @return
     */
    public GameState executeRule(IPlayer player1, IPlayer player2, int index, GameState state);

}
