package kalah;

import com.qualitascorpus.testsupport.IO;

import print.BoardPrinter;

/**
 * Class that represents a game of mancala.
 * Both players and houses are 0-indexed.
 * @author????????
 *
 */
public class Board {
	private static final int NUM_PLAYERS = 2;
	private final int numHouses;
	
	private final Row[] rows;
	private final BoardPrinter printer;
	private int playerTurn;
	
	public Board(int numHouses, int numStartSeeds, int startingPlayer, BoardPrinter printer) {
		this.numHouses = numHouses;
		this.printer = printer;
		this.playerTurn = startingPlayer;
		
		rows = new Row[NUM_PLAYERS];
		for (int i = 0; i < NUM_PLAYERS; i++) {
			rows[i] = new Row(numHouses, numStartSeeds);
		}
	}
	
	/**
	 * Do a move starting at a specific location
	 * @param location
	 * @return false if the move is performed on a spot with no stones
	 */
	public boolean doMove(int location) {
		//We are using array-based indices, but the displayed board starts at 1, not 0.
		location--;
		
		//We start by looking at our selected house.
		Row currentRow = rows[playerTurn];
		int currentRowIndex = playerTurn;
		
		//Needed later so we only insert into our own store.
		int startingPlayerTurn = playerTurn;
		
		
		int numSeeds = currentRow.takeSeeds(location);
		if (numSeeds == 0) {
			return false; //Invalid move, let the caller handle what to do
		}
		
		int pos = location + 1;
		boolean lastSeed = false;
		boolean getAnotherTurn = false;
		while (numSeeds > 0) {
			if (numSeeds == 1){
				lastSeed = true;
			}
			
			if (pos > numHouses || (pos == numHouses && startingPlayerTurn != currentRowIndex)) {
				// Skip the current position if it is out of bounds or if it is not our store
				pos = 0;
				//Change to the next row
				currentRowIndex = getNextPlayer(currentRowIndex, NUM_PLAYERS);
				currentRow = rows[currentRowIndex];
			}
			
			int newNumSeeds = currentRow.insertSeed(pos);
			
			if (lastSeed && currentRowIndex == startingPlayerTurn) {
				// If we inserted the final seed into one of our houses, and it was previously empty
				if (pos < numHouses && newNumSeeds == 1 ) {
					//Capture opponents seeds. Need to swap the row and 
					//mirror the position in a two-player game.
					int opponentSeeds = rows[rows.length - 1 - currentRowIndex].takeSeeds(numHouses - 1 - pos);
					if (opponentSeeds > 0) { // Opponent must have seeds in their house
						int playerSeeds = currentRow.takeSeeds(pos);
						currentRow.addToStore(opponentSeeds + playerSeeds);
					}
					
				} else if (pos == numHouses) {
					//Get extra turn
					getAnotherTurn = true;
				}
			}
			numSeeds--;
			pos++;
		}
		
		//Change to the next player
		if (!getAnotherTurn) {
			playerTurn = getNextPlayer(playerTurn, NUM_PLAYERS);
		}
		
		return true;
	}
	
	
	//Used by Kalah.java to print which players turn it is currently.
	//playerTurn is 0-indexed internally.
	public int getPlayer() {
		return playerTurn + 1;
	}
	
	//Over if the current player has no moves.
	public boolean isGameOver() {
		int[] houses = rows[playerTurn].getHouses();
		for (int j = 0; j < houses.length; j++) {
			if (houses[j] != 0) {
				return false;
			}
		}
		return true;
	}
	
	//**Printing methods***
	
	/**
	 * Print out the current state of the game.
	 * The reason we convert Rows into primitive arrays is so that
	 * BoardPrinter is not dependent on Row.
	 * @param io
	 */
	public void printState(IO io) {
		
		int[][] allHouses = new int[NUM_PLAYERS][numHouses];
		int[] allStores = new int[numHouses];
		for (int i = 0; i < allHouses.length; i++) {
			allHouses[i] = rows[i].getHouses();
			allStores[i] = rows[i].getStore();
		}
		printer.printState(io, allHouses, allStores);
	}

	/**
	 * Print out the final state of the board, and the winner.
	 * @param io
	 */
	public void printResult(IO io) {
		printState(io);
		int[] scores = new int[NUM_PLAYERS];
		for (int i = 0; i < scores.length; i++) {
			scores[i] = rows[i].getScore();
		}
		printer.printResult(io, scores);
	}
	
	//**Private methods**
	
	/**
	 * Cycles to the next player/row.
	 * This can be reused for both rows and players as rows are mapped to players.
	 * @param currentPlayer
	 * @param maxPlayers
	 * @return
	 */
	private int getNextPlayer(int currentPlayer, int maxPlayers) {
		currentPlayer++;
		if (currentPlayer == maxPlayers) {
			return 0;
		} else {
			return currentPlayer;
		}
	}
}
