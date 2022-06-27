package kalah;

/**
 * Class that represents a row of houses and a store.
 * @author????????
 *
 */
public class Row {
	private int[] houses;
	private int store;
	
	public Row(int numHouses, int numSeeds) {
		this.houses = new int[numHouses];
		for (int i = 0; i < numHouses; i++) {
			houses[i] = numSeeds;
		}
		store = 0;
	}
	
	public int[] getHouses() {
		return houses;
	}
	
	public int getStore() {
		return store;
	}
	
	/**
	 * Used when capturing an opponent's house - we need to 
	 * deposit multiple seeds into the store.
	 */
	public void addToStore(int seeds) {
		store += seeds;
	}
	
	/**
	 * Insert a single seed at a location.
	 * @param pos
	 * @return
	 */
	public int insertSeed(int pos) {
		if (pos < houses.length) {
			houses[pos]++;
			return houses[pos];
		} else if (pos == houses.length) {
			return store++;
		} else {
			throw new IllegalArgumentException("Trying to insert a seed past the board.");
		}
	}
	
	/**
	 * Remove the seeds from a location and return the amount.
	 * @param location
	 * @return
	 */
	public int takeSeeds(int location) {
		int numSeeds = houses[location];
		houses[location] = 0;
		return numSeeds;
	}
	
	public int getScore() {
		int score = 0;
		for (int i = 0; i < houses.length; i++) {
			score += houses[i];
		}
		score += store;
		return score;
	}
}