package kalah.game;


import kalah.ui.GameInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for governing game control flow such as rules and player turns
 */
public class KalahGameControl implements GameControl {

    private static final int QUIT_GAME_SIGNAL = -1;
    private GameInterface board;
    private IPlayer p1;
    private IPlayer p2;
    private List<GameRules> kalahRules;

    public KalahGameControl(GameInterface board, IPlayer p1, IPlayer p2) {
        this.p1 = p1;
        this.p2 = p2;
        this.board = board;

        this.kalahRules = new ArrayList<GameRules>();
        addGameRules();
    }

    /**
     * Starting the game
     */
    public void start () {
        while (true) {
            board.outputBoard();

            if (p1.getTurn()) {
                if (playerTurn(p1, p2) == GameState.QUIT_GAME) return;
            } else {
                if (playerTurn(p2, p1) == GameState.QUIT_GAME) return;
            }
        }
    }

    /**
     * Preparing for the execution of the player turn
     * @param player
     * @param otherPlayer
     * @return
     */
    public GameState playerTurn (IPlayer player, IPlayer otherPlayer) {
        // Checking if the game is over
        if (player.checkIfPlayerHousesEmpty()) {
            gameOver();
            return GameState.QUIT_GAME;
        } else {
            return executePlayerTurn(player, otherPlayer);
        }
    }

    /**
     * Executing the player turn
     * @param player
     * @param otherPlayer
     * @return
     */
    public GameState executePlayerTurn(IPlayer player, IPlayer otherPlayer) {
        int house = board.getPlayerInput(player.getName());
        if (house == QUIT_GAME_SIGNAL) {
            quitGame();
            return GameState.QUIT_GAME;
        }

        // Checking if house empty
       GameState state = applyGameRules(player, otherPlayer, GameState.CHECK_ILLEGAL_MOVE, house-1);
        if (state == GameState.EMPTY_HOUSE_SELECTED) {
            board.outputErrorMessage(GameState.EMPTY_HOUSE_SELECTED);
            return GameState.CONTINUE_GAME;
        }

        // Assigning seeds to other houses and stores
        int seedsLeftOver = player.shiftSeeds(house, player.getHouses().get(house-1).getSeeds());
        player.getHouses().get(house-1).setSeeds(0);
        changeTurn();

        // Checking capture
        applyGameRules(player, otherPlayer, GameState.CHECK_CAPTURE, seedsLeftOver);

        // Distributing seeds to other houses and player store
        while (seedsLeftOver > 0) {
            seedsLeftOver = sowSeeds(player, otherPlayer, seedsLeftOver);
        }

        return GameState.CONTINUE_GAME;
    }

    /**
     * Swapping player turns
     */
    public void changeTurn() {
        p1.setTurn(!p1.getTurn());
        p2.setTurn(!p2.getTurn());
    }

    /**
     * Distrubuting the seeds throughout the houses of both players
     * @param player
     * @param otherPlayer
     * @param seedsLeftOver
     * @return
     */
    public int sowSeeds (IPlayer player, IPlayer otherPlayer, int seedsLeftOver) {
        player.getStore().addSeeds(1);
        seedsLeftOver--;

        // Checking if last seed placed in player's store
        applyGameRules(player, otherPlayer, GameState.CHECK_EMPTY_STORE_SOW, seedsLeftOver);

        seedsLeftOver = otherPlayer.shiftSeeds(0, seedsLeftOver);
        seedsLeftOver = player.shiftSeeds(0, seedsLeftOver);

        // Checking capture
        applyGameRules(player, otherPlayer, GameState.CHECK_CAPTURE, seedsLeftOver);

        return seedsLeftOver;
    }

    /**
     * Applies game rules outlined
     * @param player
     * @param otherPlayer
     * @param state
     * @param index
     * @return
     */
    public GameState applyGameRules (IPlayer player, IPlayer otherPlayer, GameState state, int index) {
        for (GameRules rule : kalahRules) {
            GameState returnedState = rule.executeRule(player, otherPlayer, index, state);
            if (returnedState != GameState.CONTINUE_GAME) return returnedState;
        }

        return GameState.CONTINUE_GAME;
    }

    /**
     * Adding game rules
     */
    public void addGameRules () {
        this.kalahRules.add(new EmptyStoreSow());
        this.kalahRules.add(new CaptureRule());
        this.kalahRules.add(new IllegalMoveRule());
    }
//91
    public void chooseStartingPlayer(IPlayer player) {
        player.setTurn(true);
    }

    public void quitGame () {
        board.outputGameOver(true);
    }

    public void gameOver () {
        board.outputGameOver(false);
    }
}