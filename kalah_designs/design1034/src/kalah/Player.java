package kalah;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private int id;
    private Player opponent;
    private Store store;
    private List<House> houses = new ArrayList<>();

    public Player (int initialNumSeeds, int numHouses, int id) {
        this.id = id;
        this.store = new Store(this, 0);
        initHouses(initialNumSeeds, numHouses);
    }

    private void initHouses(int initialNumSeeds, int numHouses) {
        int upperIndex = numHouses + 1;
        for (int i = 1; i < upperIndex; i++) {
            houses.add(new House(this, initialNumSeeds, i));
        }
    }

    public Store getStore() {
        return this.store;
    }

    public int getTotalSeeds() {
        int totalSeeds = 0;
        for (House house : houses) {
            totalSeeds += house.getNumSeeds();
        }

        return totalSeeds + store.getNumSeeds();
    }

    public Player getOpponent() {
        return this.opponent;
    }

    public List<House> getHouses() {
        return this.houses;
    }

    public int getId() {
        return this.id;
    }

    public void setOpponent(Player player) {
        this.opponent = player;
    }

    public boolean isEmptyHouse(int houseNumber) {
        return houses.get(houseNumber - 1).getNumSeeds() > 0;
    }

    public boolean areHousesEmpty() {
        for (House house : houses) {
            if (house.getNumSeeds() != 0) {
                return false;
            }
        }

        return true;
    }

    public int[] getHouseValues() {
        int[] houseValues = new int[houses.size()];
        for (int i = 0; i < houses.size(); i++) {
            houseValues[i] = houses.get(i).getNumSeeds();
        }
        return houseValues;
    }
}
