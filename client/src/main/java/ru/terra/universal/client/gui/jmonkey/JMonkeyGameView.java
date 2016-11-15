package ru.terra.universal.client.gui.jmonkey;

import ru.terra.universal.client.game.GameView;
import ru.terra.universal.shared.entity.AbstractEntity;
import ru.terra.universal.shared.entity.PlayerInfo;

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
    public void entityAdd(AbstractEntity entity) {
        jmeGameViewImpl.entityAdd(entity);
    }

    @Override
    public void entityDel(Long uid) {
        jmeGameViewImpl.entityDel(uid);
    }

    @Override
    public void enemyLoggedIn(PlayerInfo enemy) {
        jmeGameViewImpl.enemyLoggedIn(enemy);
    }

    @Override
    public void enemyLoggedOut(Long uid) {
        jmeGameViewImpl.enemyLoggedOut(uid);
    }

    @Override
    public void updateEntityPosition(AbstractEntity entity) {
        jmeGameViewImpl.updateEntityPosition(entity);
    }

    @Override
    public void entityVectorMove(AbstractEntity entity, float x, float y, float z, float h, boolean stop) {
        jmeGameViewImpl.entityVectorMove(entity.getUID(), x, y, z, h, stop);
    }

    @Override
    public void serverMessage(String message) {
        jmeGameViewImpl.serverMessage(message);

    }

    @Override
    public void playerSay(PlayerInfo player, String message) {
        jmeGameViewImpl.playerSay(player, message);
    }
}
