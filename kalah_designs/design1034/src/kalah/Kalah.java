package kalah;

import com.qualitascorpus.testsupport.IO;
import com.qualitascorpus.testsupport.MockIO;

public class Kalah {
    private int initialNumSeeds = 4;
    private int numHouses = 6;
    private Player player1;
    private Player player2;
    private Board board;
    private Boolean keepPlaying = true;
    private String quitCode = "q";
    private UI ui;

	public static void main(String[] args) {
		new Kalah().play(new MockIO());
	}
	public void play(IO io) {
	    ui = new UI(io);
	    init();

	    displayBoard(ui);
        ui.promptPlayer(board.getCurrentPlayer());

	    while (keepPlaying == true) {
            if (ui.getInputResponse().equals(quitCode)) {
                ui.gameOver();
                displayBoard(ui);
                keepPlaying = false;
            } else {
                if (board.isValidMove(ui.getInputResponse())) {
                    board.makeMove(ui.getInputResponse());

                    displayBoard(ui);

                    if (board.isGameOver()) {
                        ui.gameOver();
                        displayBoard(ui);
                        ui.finalScores(player1.getTotalSeeds(), player2.getTotalSeeds());
                        keepPlaying = false;
                    } else {
                        ui.promptPlayer(board.getCurrentPlayer());
                    }
                } else {
                    ui.invalidMove();
                    displayBoard(ui);
                    ui.promptPlayer(board.getCurrentPlayer());
                }

            }

        }

	}

    private void init() {
        player1 = new Player(initialNumSeeds, numHouses, 1);
        player2 = new Player(initialNumSeeds, numHouses, 2);
        player1.setOpponent(player2);
        player2.setOpponent(player1);
        board = new Board(numHouses, player1);
    }

    private void displayBoard(UI ui) {
        ui.displayBoard(numHouses, player1.getHouseValues(), player2.getHouseValues(), player1.getStore().getNumSeeds(), player2.getStore().getNumSeeds());
    }
}
