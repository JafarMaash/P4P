package kalah;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private int numHouses;
    private Player currentPlayer;

    public Board(int numHouses, Player player1) {
        this.numHouses = numHouses;
        this.currentPlayer = player1;
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public boolean isGameOver() {
        return currentPlayer.areHousesEmpty();
    }

    public void updateCurrentPlayer() {
        this.currentPlayer = currentPlayer.getOpponent();
    }

    public void makeMove(String inputResponse) {
        int chosenHouseNumber = Integer.parseInt(inputResponse);
        House chosenHouse = currentPlayer.getHouses().get(chosenHouseNumber - 1);
        int amount = chosenHouse.getNumSeeds();
        chosenHouse.clearSeeds();
        spreadSeeds(chosenHouseNumber, amount);
    }

    private void spreadSeeds(int chosenHouseNumber, int amount) {
        List<BoardSpot> board = new ArrayList<>();
        Player opponent = currentPlayer.getOpponent();
        board.addAll(currentPlayer.getHouses());
        board.add(currentPlayer.getStore());
        board.addAll(opponent.getHouses());
        board.add(opponent.getStore());

        int index = chosenHouseNumber;
        while (amount > 0) {
            BoardSpot boardSpot = board.get(index);
            if ((boardSpot instanceof House) || (boardSpot.getPlayer() != opponent)) {
                boardSpot.incrementSeeds();
                amount--;
            }

            if (index == board.size() - 1) {
                index = 0;
            } else {
                index++;
            }

            if (amount == 0) {
                if (boardSpot instanceof House) {
                    handlePotentialCapture(boardSpot);
                    updateCurrentPlayer();
                }
            }
        }
    }

    private void handlePotentialCapture(BoardSpot lastHouseLanded) {
        boolean landedInCurrentPlayer = currentPlayer == lastHouseLanded.getPlayer();
        boolean lastHouseLandedHadZero = lastHouseLanded.getNumSeeds() == 1;
        if (landedInCurrentPlayer && lastHouseLandedHadZero) {
            int opponentHouseNumber = numHouses - ((House) lastHouseLanded).getNumber();
            House opponentHouse = currentPlayer.getOpponent().getHouses().get(opponentHouseNumber);
            int opponentNumSeeds = opponentHouse.getNumSeeds();
            if (opponentNumSeeds > 0) {
                opponentHouse.clearSeeds();
                int captureAmount = opponentNumSeeds + lastHouseLanded.getNumSeeds();
                lastHouseLanded.clearSeeds();
                currentPlayer.getStore().addSeeds(captureAmount);
            }
        }
    }

    public boolean isValidMove(String inputResponse) {
        return currentPlayer.isEmptyHouse(Integer.parseInt(inputResponse));
    }
}
