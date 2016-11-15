package ru.terra.universal.client.game;

import ru.terra.universal.client.gui.jmonkey.JMonkeyGameView;
import ru.terra.universal.shared.entity.AbstractEntity;
import ru.terra.universal.shared.entity.PlayerInfo;

public abstract class GameView {
    public abstract void init();

    public abstract void loadPlayer();

    public abstract void entityAdd(AbstractEntity entity);

    public abstract void entityDel(Long uid);

    public abstract void entityVectorMove(AbstractEntity entity, float x, float y, float z, float h, boolean stop);

    public abstract void updateEntityPosition(AbstractEntity entity);

    public static GameView getView() {
        return JMonkeyGameView.getInstance();
    }

    public abstract void enemyLoggedIn(PlayerInfo enemy);

    public abstract void enemyLoggedOut(Long uid);

    public abstract void serverMessage(String message);

    public abstract void playerSay(PlayerInfo player, String message);
}
