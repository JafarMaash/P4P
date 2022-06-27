package kalah.game;


import java.util.List;

/**
 * Represents a player
 */
public interface IPlayer {

    /**
     * Moves the seeds within a player's own houses
     * @param houseSelected
     * @param seeds
     * @return
     */
    public int shiftSeeds (int houseSelected, int seeds);

    /**
     * Calculates seeds in houses and the players store
     * @return
     */
    public int getTotalNumSeeds();

    /**
     * Checks to see if all the houses are empty
     * @return
     */
    public boolean checkIfPlayerHousesEmpty();

    public List<SeedContainer> getHouses();

    public SeedContainer getStore();

    public boolean getTurn();
    public void setTurn(boolean isTurn);

    public String getName ();
    public void setName (String name);

    public int getId();
}
