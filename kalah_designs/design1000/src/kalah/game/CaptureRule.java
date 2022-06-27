package kalah.game;

/**
 * Rule defined for the capturing of the other player's seeds
 */
public class CaptureRule implements GameRules {
    public GameState executeRule(IPlayer player1, IPlayer player2, int houseIndex, GameState state) {

        // Making sure capture is to take place
        if (state == GameState.CHECK_CAPTURE && houseIndex < 0) {

            // Obtaining seeds from other player and setting seeds appropriately
            int oppositeHouse = houseIndex + player1.getHouses().size() - 1;
            int seedsInOppositeHouse = player2.getHouses().get(oppositeHouse).getSeeds();
            if (seedsInOppositeHouse != 0) {
                player2.getHouses().get(oppositeHouse).setSeeds(0);
                player1.getHouses().get(-houseIndex).setSeeds(0);
                player1.getStore().addSeeds(seedsInOppositeHouse + 1);

                return GameState.CAPTURED;
            }
        }
        return GameState.CONTINUE_GAME;
    }
}
