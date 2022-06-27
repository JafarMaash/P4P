package kalah.game;


import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player
 */
public class Player implements IPlayer {

    private static final int NUM_HOUSES = 6;
    private int id;
    private String name;
    private List<SeedContainer> houses;
    private SeedContainer store;
    private boolean isTurn;

    public Player (String name, int id) {
        this.id = id;
        this.name = name;
        this.houses = initHouses();
        this.store = new Store();
        this.isTurn = false;
    }

    /**
     * Moves the seeds within a player's own houses
     * @param houseSelected
     * @param seeds
     * @return
     */
    public int shiftSeeds (int houseSelected, int seeds) {
        for (int i = houseSelected; i < houses.size(); i++) {
            if (seeds <= 0) return 0;
            houses.get(i).addSeeds(1);
            seeds--;

            // Testing for capture
            if (houses.get(i).getSeeds() == 1 && seeds == 0) {
                return -i;
            }
        }
        return seeds;
    }

    /**
     * Initializing all houses
     * @return
     */
    private List<SeedContainer> initHouses () {
        List<SeedContainer> houses = new ArrayList<SeedContainer>();
        for (int i = 0; i < NUM_HOUSES; i++) {
            houses.add(new House(i));
        }

        return houses;
    }

    /**
     * Calculates seeds in houses and the players store
     * @return
     */
    public int getTotalNumSeeds () {
        int sum = 0;
        for (SeedContainer house : houses) {
            sum += house.getSeeds();
        }
        sum += getStore().getSeeds();
        return sum;
    }

    /**
     * Checks to see if all the houses are empty
     * @return
     */
    public boolean checkIfPlayerHousesEmpty () {
        for (SeedContainer h : getHouses()) {
            if (!h.isEmpty()) return false;
        }

        return true;
    }

    public String getName () {
        return this.name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public List<SeedContainer> getHouses() {
        return this.houses;
    }

    public SeedContainer getStore() {
        return this.store;
    }

    public boolean getTurn() {
        return this.isTurn;
    }

    public void setTurn(boolean isTurn) {
        this.isTurn = isTurn;
    }

    public int getId() {
        return id;
    }
}