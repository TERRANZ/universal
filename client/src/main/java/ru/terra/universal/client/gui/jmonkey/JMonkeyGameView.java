package ru.terra.universal.client.gui.jmonkey;

import ru.terra.universal.client.game.GameView;

public class JMonkeyGameView extends GameView {
    private JMEGameViewImpl jmeGameViewImpl;

    private static JMonkeyGameView instance = new JMonkeyGameView();

    private JMonkeyGameView() {
        jmeGameViewImpl = new JMEGameViewImpl();
        jmeGameViewImpl.start();
    }

    public static GameView getInstance() {
        return instance;
    }

    @Override
    public void init() {
    }

    @Override
    public void loadPlayer() {
        jmeGameViewImpl.loadPlayer();
    }

    @Override
    public void serverMessage(String message) {
        jmeGameViewImpl.serverMessage(message);

    }

}
