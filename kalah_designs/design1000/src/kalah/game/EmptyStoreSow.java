package kalah.game;

/**
 * This rule is defined for allowing the player to have another
 * turn when their empty store is sowed with 1 seed on
 */
public class EmptyStoreSow implements GameRules {

    public GameState executeRule(IPlayer player1, IPlayer player2, int seeds, GameState state) {

        // Checking if this event has occured
        if (state == GameState.CHECK_EMPTY_STORE_SOW && seeds == 0) {

            // Giving player extra turn
            player1.setTurn(!player1.getTurn());
            player2.setTurn(!player2.getTurn());

            return GameState.EMPTY_STORE_SOWED;
        }
        return GameState.CONTINUE_GAME;
    }

}
