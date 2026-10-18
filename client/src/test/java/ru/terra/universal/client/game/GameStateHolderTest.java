package ru.terra.universal.client.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class GameStateHolderTest {

    private final GameStateHolder holder = GameStateHolder.getInstance();

    @BeforeEach
    void reset() {
        holder.setNotifier(null);
        holder.setGameState(GameStateHolder.GameState.INIT);
    }

    @Test
    void startsInInitState() {
        assertEquals(GameStateHolder.GameState.INIT, holder.getGameState());
    }

    @Test
    void storesTheLastAssignedState() {
        holder.setGameState(GameStateHolder.GameState.LOGIN);
        assertEquals(GameStateHolder.GameState.LOGIN, holder.getGameState());

        holder.setGameState(GameStateHolder.GameState.LOGGED_IN);
        assertEquals(GameStateHolder.GameState.LOGGED_IN, holder.getGameState());
    }

    @Test
    void notifiesAboutTheTransition() {
        final List<String> transitions = new ArrayList<>();
        holder.setNotifier((oldState, newState) ->
            transitions.add(oldState + "->" + newState));

        holder.setGameState(GameStateHolder.GameState.LOGIN);
        holder.setGameState(GameStateHolder.GameState.LOGGED_IN);

        assertEquals(List.of("INIT->LOGIN", "LOGIN->LOGGED_IN"), transitions);
    }

    @Test
    void keepsTheStateWhenNoNotifierIsInstalled() {
        assertNull(holder.getNotifier());

        holder.setGameState(GameStateHolder.GameState.CHAR_BOOT);

        assertEquals(GameStateHolder.GameState.CHAR_BOOT, holder.getGameState());
    }

    @Test
    void exposesTheInstalledNotifier() {
        final GameStateChangeNotifier notifier = (oldState, newState) -> {
        };

        holder.setNotifier(notifier);

        assertSame(notifier, holder.getNotifier());
    }
}
