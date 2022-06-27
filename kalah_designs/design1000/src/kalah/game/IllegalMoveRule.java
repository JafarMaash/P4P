package kalah.game;

/**
 * Rule defined to check whether the move a player makes is legal or not
 */
public class IllegalMoveRule implements GameRules {
    public GameState executeRule(IPlayer player1, IPlayer player2, int houseIndex, GameState state) {
        if (state == GameState.CHECK_ILLEGAL_MOVE) {
            if (emptyHouseSelected(player1, houseIndex)) {
                return GameState.EMPTY_HOUSE_SELECTED;
            }
        }
        return GameState.CONTINUE_GAME;
    }

    /**
     * Checks whether the user has selected an empty house to start sowing with
     * @param player
     * @param houseIndex
     * @return
     */
    public boolean emptyHouseSelected (IPlayer player, int houseIndex) {
        if (player.getHouses().get(houseIndex).isEmpty()) {
            return true;
        }

        return false;
    }
}
