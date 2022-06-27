package kalah.ui;

import com.qualitascorpus.testsupport.IO;
import kalah.game.GameState;
import kalah.game.IPlayer;
import kalah.game.SeedContainer;
import kalah.ui.GameInterface;

/**
 * Responsible for outputting ASCII representing the Mancala board
 */
public class Board implements GameInterface {

    private final String BOARD_BORDER;
    private final String BOARD_MID_BORDER;
    private static final String EMPTY_HOUSE_ERROR_MESSAGE = "House is empty. Move again.";

    private IO io;
    private IPlayer p1;
    private IPlayer p2;

    public Board (IO mockIO, IPlayer p1, IPlayer p2) {
        this.io = mockIO;
        this.p1 = p1;
        this.p2 = p2;

        BOARD_BORDER = "+----+-------+-------+-------+-------+-------+-------+----+";
        BOARD_MID_BORDER = "|    |-------+-------+-------+-------+-------+-------|    |";
    }

    /**
     * Prints the board
     */
    public void outputBoard () {
        String p1Houses = buildBoardBottom();
        String p2Houses = buildBoardTop();

        io.println(BOARD_BORDER);
        io.println(p2Houses);
        io.println(BOARD_MID_BORDER);
        io.println(p1Houses);
        io.println(BOARD_BORDER);
    }

    /**
     * Builds the top half of the board
     * @return
     */
    private String buildBoardTop () {
        StringBuilder sb = new StringBuilder();
        sb.append("| "+ p2.getName() +" | ");
        sb.append(getPlayerRepresentation(p2));
        sb.append(p1.getStore().getRepresentation() + "|");

        return sb.toString();
    }

    /**
     * Builds the bottom half of the board
     * @return
     */
    private String buildBoardBottom () {
        StringBuilder sb = new StringBuilder();
        sb.append("| "+ p2.getStore().getRepresentation() +"| ");
        sb.append(getPlayerRepresentation(p1));
        sb.append(p1.getName() +" |");

        return sb.toString();
    }

    /**
     * Signals players the game is over
     * @param playerQuit
     */
    public void outputGameOver (boolean playerQuit) {
        int p1Seeds = p1.getTotalNumSeeds();
        int p2Seeds = p2.getTotalNumSeeds();

        String winner;
        if (p1Seeds > p2Seeds) {
            winner = "Player 1 wins!";
        } else if (p2Seeds > p1Seeds) {
            winner = "Player 2 wins!";
        } else {
            winner = "A tie!";
        }

        io.println("Game over");
        outputBoard();

        if (!playerQuit) {
            io.println("\tplayer 1:" + p1Seeds);
            io.println("\tplayer 2:" + p2Seeds);
            io.println(winner);
        }
    }

    /**
     * Outputs error message for when an empty house is selected
     */
    public void outputEmptyHouseError () {
        io.println(EMPTY_HOUSE_ERROR_MESSAGE);
    }

    public void outputErrorMessage (GameState errorType) {
        switch (errorType) {
            case EMPTY_HOUSE_SELECTED:
                outputEmptyHouseError();
        }
    }

    /**
     * Obtains the house the player has selected
     * @param playerName
     * @return
     */
    public int getPlayerInput (String playerName) {
        String message = "Player "+ playerName +"'s turn - Specify house number or 'q' to quit: ";
        int houseChosen = io.readInteger(message, 1, 7, -1, "q");
        return houseChosen;
    }

    /**
     * Gets string representation of a player's houses
     * @param player
     * @return
     */
    public String getPlayerRepresentation(IPlayer player) {
        StringBuilder allHouses = new StringBuilder();
        if (player.getId() == 1) {
            for (SeedContainer h : player.getHouses()) {
                allHouses.append(h.getRepresentation());
            }
        } else {
            for (int i = player.getHouses().size() - 1; i >= 0; i--) {
                allHouses.append(player.getHouses().get(i).getRepresentation());
            }
        }

        return allHouses.toString();
    }

}