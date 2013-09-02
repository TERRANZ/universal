package ru.terra.universal.client;

import org.apache.log4j.Logger;

public class GameStateHolder {
    private Logger logger = Logger.getLogger(this.getClass());

    public static enum GameState {
        INIT, LOGIN, LOGGED_IN, CHAR_BOOT, SERVER_SELECTED, IN_WORLD;
    }

    private GameState gameState = GameState.INIT;
    private static GameStateHolder instance = new GameStateHolder();

    private GameStateHolder() {
    }

    public static GameStateHolder getInstance() {
        return instance;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        logger.info("Changing game state from " + getGameState().toString() + " to " + gameState.toString());
        this.gameState = gameState;
    }
}
