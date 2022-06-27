package kalah.game;

/**
 * Represents stores and houses
 */
public interface SeedContainer {

    /**
     * Returns string representing the game element for the ASCII board
     * @return
     */
    public String getRepresentation();

    public boolean isEmpty();

    public int getSeeds();

    public void setSeeds(int seeds);

    public void addSeeds(int seeds);
}
