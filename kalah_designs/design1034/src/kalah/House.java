package kalah;

public class House extends BoardSpot{
    private int number;

    public House(Player player, int numSeeds, int number) {
        super(player, numSeeds);
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}
