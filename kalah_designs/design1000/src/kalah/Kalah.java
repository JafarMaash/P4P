package kalah;

import com.qualitascorpus.testsupport.IO;
import com.qualitascorpus.testsupport.MockIO;
import kalah.ui.GameInterface;
import kalah.game.GameControl;
import kalah.game.IPlayer;
import kalah.game.KalahGameControl;
import kalah.game.Player;
import kalah.ui.Board;

/**
 * Entry point of game. Initialises game settings such as the
 * game controller, players and the output type of the game
 */
public class Kalah {

	private IO io;
	private GameInterface board;
	private IPlayer p1;
	private IPlayer p2;
	private GameControl gameControl;

	public static void main(String[] args) {
		new Kalah().play(new MockIO());
	}

	public void play(IO io) {
		this.io = io;
		this.p1 = new Player("P1", 1);
		this.p2 = new Player("P2", 2);
		this.board = new Board(io, p1, p2);

		startNewGame();
	}

	/**
	 * Starts new game
	 */
	public void startNewGame () {
		// Kalah could be one of the games, and this class can be used to start and handle multiple games
		this.gameControl = new KalahGameControl(board, p1, p2);
		gameControl.chooseStartingPlayer(p1);
		gameControl.start();
	}

	public void stopGame () {
		gameControl.gameOver();
	}
}