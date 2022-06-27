package kalah;

import com.qualitascorpus.testsupport.IO;

public class UI {
    private IO io;
    private String inputResponse;

    public UI(IO io) {
        this.io = io;
    }

    public String getInputResponse() {
        return this.inputResponse;
    }

    public void displayBoard(int numHouses, int[] p1HouseSeeds, int[] p2HouseSeeds, int p1StoreSeeds, int p2StoreSeeds) {
        String p1Row = generateP1Row(p1HouseSeeds, p2StoreSeeds, numHouses);
        String p2Row = generateP2Row(p2HouseSeeds, p1StoreSeeds, numHouses);
        io.println(generateOuterBoard(numHouses));
        io.println(p2Row);
        io.println(generateInnerBoard(numHouses));
        io.println(p1Row);
        io.println(generateOuterBoard(numHouses));
    }

    private String spaceNumber(int number) {
        String space = number < 10 ? " " : "";
        return space + number;
    }

    private String generateP1Row(int[] houseSeeds, int storeSeeds, int numHouses) {
        String p1Row = "";
        for (int i = 0; i < numHouses; i++) {
            int seeds = houseSeeds[i];
            int num = i + 1;
            if (numHouses == num) {
                p1Row += "] | " + num + "[" + spaceNumber(seeds) + "] | P1 |";
            } else if (num == 1) {
                p1Row += "| " + spaceNumber(storeSeeds) + " | 1[" + spaceNumber(seeds);
            } else {
                p1Row += "] | " + num + "[" + spaceNumber(seeds);
            }
        }

        return p1Row;
    }

    private String generateP2Row(int[] houseSeeds, int storeSeeds, int numHouses) {
        String p2Row = "";
        for (int i = numHouses - 1; i >= 0; i--) {
            int seeds = houseSeeds[i];
            int num = i + 1;
            if (numHouses == num) {
                p2Row += "| P2 | " + num + "[" + spaceNumber(seeds);
            } else if (num == 1) {
                p2Row +=  "] | 1[" + spaceNumber(seeds) + "] | " + spaceNumber(storeSeeds) + " |";
            } else {
                p2Row += "] | " + num + "[" + spaceNumber(seeds);
            }
        }

        return p2Row;
    }

    private String generateOuterBoard(int numHouses) {
        return "+----+" + generateBoardPieces(numHouses) + "+----+";
    }

    private String generateInnerBoard(int numHouses) {
        return "|    |" + generateBoardPieces(numHouses) + "|    |";
    }

    private String generateBoardPieces(int numHouses) {
        String boardPieces = "";
        for (int i = 0; i < numHouses; i++) {
            boardPieces += "-------";
            if (i != numHouses -1) {
                boardPieces += "+";
            }
        }

        return boardPieces;
    }

    public void promptPlayer(Player currentPlayer) {
        inputResponse = io.readFromKeyboard("Player P" + currentPlayer.getId() + "'s turn - Specify house number or 'q' to quit: ");
    }

    public void invalidMove() {
        io.println("House is empty. Move again.");
    }

    public void gameOver() {
        io.println("Game over");
    }

    public void finalScores(int totalSeedsP1, int totalSeedsP2) {
        io.println("\tplayer 1:" + totalSeedsP1);
        io.println("\tplayer 2:" + totalSeedsP2);

        if (totalSeedsP1 == totalSeedsP2) {
            io.println("A tie!");
        } else if (totalSeedsP1 > totalSeedsP2) {
            io.println("Player 1 wins!");
        } else {
            io.println("Player 2 wins!");
        }
    }
}
