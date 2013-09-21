package ru.terra.universal.client.game;

import ru.terra.universal.client.gui.jmonkey.JMonkeyGameView;

public abstract class GameView {
    public abstract void init();

    public abstract void loadPlayer();

    public static GameView getView() {
        return JMonkeyGameView.getInstance();
    }

    public abstract void serverMessage(String message);
}
