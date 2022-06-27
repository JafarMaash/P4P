package kalah.game;

/**
 * Represents one of the "houses" or "pits" a player has
 */
public class House implements SeedContainer {

    private static final int NUM_STARTING_SEEDS = 4;
    private int houseIndex;
    private int seeds;

    public House (int houseIndex) {
        this.houseIndex = houseIndex+1;
        this.seeds = NUM_STARTING_SEEDS;
    }

    /**
     * Forms container into a string format for display on the board
     * @return
     */
    public String getRepresentation () {
        StringBuilder houseRep = new StringBuilder();
        houseRep.append(getHouseIndex());

        // Checking for spacing for single and double digit numbers
        String cell = (getSeeds() > 9) ? "["+ getSeeds() +"]" : "[ "+ getSeeds() +"]";
        houseRep.append(cell);

        // Appending seperator
        houseRep.append(" | ");

        return houseRep.toString();
    }

    /**
     * Checks if there are no seeeds within
     * @return
     */
    public boolean isEmpty() {
        if (getSeeds() == 0) return true;
        return false;
    }

    public int getSeeds() {
        return this.seeds;
    }

    public void setSeeds(int seeds) {
        this.seeds = seeds;
    }

    public void addSeeds (int seeds) {
        this.seeds += seeds;
    }

    public int getHouseIndex () {
        return this.houseIndex;
    }

    public int getNumStartingSeeds () { return NUM_STARTING_SEEDS; }
}