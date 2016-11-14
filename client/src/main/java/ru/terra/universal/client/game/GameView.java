package ru.terra.universal.client.game;

import ru.terra.universal.client.game.entity.Entity;
import ru.terra.universal.client.game.entity.MapObject;
import ru.terra.universal.client.game.entity.Player;
import ru.terra.universal.client.gui.jmonkey.JMonkeyGameView;

public abstract class GameView {
    public abstract void init();

    public abstract void loadPlayer();

    public abstract void addMapObject(MapObject entity);

    public static GameView getView() {
        return JMonkeyGameView.getInstance();
    }

    public abstract void enemyLoggedIn(Player enemy);

    public abstract void updateEntityPosition(Entity entity);

    public abstract void entityVectorMove(Entity entity, float x, float y, float z, float h, boolean stop);

    public abstract void serverMessage(String message);

    public abstract void playerSay(Player player, String message);
}
