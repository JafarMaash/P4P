package kalah;

import com.qualitascorpus.testsupport.IO;
import com.qualitascorpus.testsupport.MockIO;

import print.BoardPrinter;
import print.TwoPlayerBoardPrinter;

/**
 * This class is the starting point for the Modifiability Assignment.
 */
public class Kalah {
	private static final String QUIT_STRING = "q";
	private static final int QUIT_VALUE = -1;
	
	private static final int NUM_HOUSES = 6;
	private static final int NUM_SEEDS = 4;
	private static final int STARTING_PLAYER = 0;
	
	public static void main(String[] args) {
		new Kalah().play(new MockIO());
	}
	public void play(IO io) {
		BoardPrinter printer = new TwoPlayerBoardPrinter();
		Board board = new Board(NUM_HOUSES, NUM_SEEDS, STARTING_PLAYER, printer);
		int selectedHouse;
		while (true) {
			board.printState(io);
			
			if (board.isGameOver()) {
				io.println("Game over");
				board.printResult(io);
				break;
			}
			// io.readInteger deals with values outside the specified range.
			selectedHouse = io.readInteger("Player P" + board.getPlayer() + "'s turn - Specify house number or 'q' to quit: ", 1, NUM_HOUSES, QUIT_VALUE, QUIT_STRING);
			if (selectedHouse == QUIT_VALUE) {
				io.println("Game over");
				board.printState(io);
				break;
			}
			
			//Check for invalid move
			if (!board.doMove(selectedHouse)) {
				io.println("House is empty. Move again.");
				continue;
			}
		}
	}
}
