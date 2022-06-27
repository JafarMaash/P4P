package print;

import com.qualitascorpus.testsupport.IO;

/**
 * Class that prints the state of houses and stores.
 * Row objects are not used as parameters in order to avoid a 
 * dependency between BoardPrinter and Row.
 * @author????????
 *
 */
public abstract class BoardPrinter {
	// This method is more likely to change
	public abstract void printState(IO io, int[][] allHouses, int[] allStores);
	
	// This method is less likely to change
	public void printResult(IO io, int[] scores) {
		final int numPlayers = scores.length;
		for (int i = 0; i < numPlayers; i++) {
			io.println("\tplayer " + (convertToPrintValue(i)) + ":" + scores[i]);
		}
		
		boolean tie = true;
		int winner = 1;
		int maxStoreSeeds = scores[0];
		for (int i = 1; i < numPlayers; i++) {
			int storeSeeds = scores[i];
			//If anyone has a different score from the maximum, then it is not a tie
			if (storeSeeds != maxStoreSeeds) {
				tie = false;
			}
			//New highest
			if (storeSeeds > maxStoreSeeds) {
				maxStoreSeeds = storeSeeds;
				winner = (convertToPrintValue(i));
			}
		}
		
		if (tie) {
			io.println("A tie!");
		} else {
			io.println("Player " + winner + " wins!");
		}
	}

	//Private methods for convenience
	/**
	 * Used for converting the internal representation of players to 
	 * the one we use for printing.
	 * Houses and players are both 0-indexed, this converts it to what 
	 * we need for printing.
	 * @param index
	 * @return
	 */
	protected int convertToPrintValue(int index) {
		return index + 1;
	}
	
}
