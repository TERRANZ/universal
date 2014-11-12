package ru.terra.universal.client.game;

/**
 * Date: 12.11.14
 * Time: 15:43
 */
public interface GameStateChangeNotifier {
    public void onGameStateChange(GameStateHolder.GameState oldgs, GameStateHolder.GameState newgs);
}
