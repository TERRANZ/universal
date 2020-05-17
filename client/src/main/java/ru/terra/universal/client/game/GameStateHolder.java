package ru.terra.universal.client.game;

import org.apache.log4j.Logger;

public class GameStateHolder {

    private static GameStateHolder instance = new GameStateHolder();
    private Logger logger = Logger.getLogger(this.getClass());
    private GameStateChangeNotifier notifier;
    private GameState gameState = GameState.INIT;
    private GameStateHolder() {
    }

    public static GameStateHolder getInstance() {
        synchronized (instance) {
            return instance;
        }
    }

    public GameState getGameState() {
        synchronized (gameState) {
            return gameState;
        }
    }

    public void setGameState(GameState gameState) {
        synchronized (gameState) {
            logger.info("Changing game state from " + getGameState().toString() + " to " + gameState.toString());
            if (notifier != null) {
                notifier.onGameStateChange(getGameState(), gameState);
            }
            this.gameState = gameState;
        }
    }

    public GameStateChangeNotifier getNotifier() {
        return notifier;
    }

    public void setNotifier(GameStateChangeNotifier notifier) {
        this.notifier = notifier;
    }

    public static enum GameState {
        INIT, LOGIN, LOGGED_IN, CHAR_BOOT, SERVER_SELECTED, IN_WORLD;
    }
}
