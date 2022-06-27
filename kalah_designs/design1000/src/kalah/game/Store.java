package kalah.game;


public class Store implements SeedContainer {
    private int seeds;

    public Store () {
        this.seeds = 0;
    }

    /**
     * Returns string representing the game element for the ASCII board
     * @return
     */
    public String getRepresentation () {
        return (getSeeds() > 9) ? getSeeds() + " " : " "+ getSeeds() +" ";
    }

    public int getSeeds() {
        return seeds;
    }

    public void setSeeds(int seeds) {
        this.seeds = seeds;
    }

    public void addSeeds(int seeds) {
        this.seeds += seeds;
    }

    public boolean isEmpty() {
        if (getSeeds() == 0) return true;
        return false;
    }
}