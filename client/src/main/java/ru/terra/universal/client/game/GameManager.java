package ru.terra.universal.client.game;

import ru.terra.universal.interserver.network.NetworkManager;
import ru.terra.universal.shared.entity.PlayerInfo;
import ru.terra.universal.shared.packet.client.MovementPacket;

/**
 * Created with IntelliJ IDEA.
 * User: terranz
 * Date: 22.09.13
 * Time: 0:47
 * To change this template use File | Settings | File Templates.
 */
public class GameManager {
    private static GameManager instance = new GameManager();
    private PlayerInfo playerInfo;
    private NetworkManager nm = NetworkManager.getInstance();

    private GameManager() {
    }

    public static GameManager getInstance() {
        return instance;
    }

    public PlayerInfo getPlayerInfo() {
        return playerInfo;
    }

    public void setPlayerInfo(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
    }

    public void sendPlayerMove(Integer direction, float x, float y, float z, int h) {
        MovementPacket packet = new MovementPacket(direction, x, y, z, h);
        packet.setSender(playerInfo.getUID());
        nm.sendPacket(packet);
    }
}
