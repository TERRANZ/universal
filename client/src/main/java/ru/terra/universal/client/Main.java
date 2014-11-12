package ru.terra.universal.client;

import org.apache.log4j.BasicConfigurator;
import ru.terra.universal.client.game.GameStateHolder;
import ru.terra.universal.client.test.ClientTest;

public class Main {
    public static void main(String[] args) {
        BasicConfigurator.configure();
        GameStateHolder.getInstance().setGameState(GameStateHolder.GameState.INIT);
//        GuiManager.getInstance().startLoginWindow();
        for (int i = 0; i < 50; i++)
            new ClientTest().start("46.0.69.103");
    }
}
