package kalah;

public class BoardSpot {
    private int numSeeds;
    private Player player;

    public BoardSpot(Player player, int numSeeds) {
        this.numSeeds = numSeeds;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public int getNumSeeds() {
        return numSeeds;
    }

    public void incrementSeeds() {
        this.numSeeds++;
    }

    public void addSeeds(int amount) {
        this.numSeeds += amount;
    }

    public void clearSeeds() {
        this.numSeeds = 0;
    }
}
