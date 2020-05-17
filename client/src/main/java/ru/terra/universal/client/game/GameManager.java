package ru.terra.universal.client.game;

import ru.terra.universal.interserver.network.NetworkManager;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.entity.PlayerInfo;
import ru.terra.universal.shared.packet.movement.MovementPacket;
import ru.terra.universal.shared.packet.movement.PlayerTeleportPacket;

/**
 * User: terranz Date: 22.09.13 Time: 0:47
 */
public class GameManager {

    private static GameManager instance = new GameManager();
    private PlayerInfo playerInfo;
    private NetworkManager nm = NetworkManager.getInstance();

    public GameManager() {
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
        MovementPacket packet = new MovementPacket(playerInfo.getUID(), direction, x, y, z, h);
        packet.setSender(playerInfo.getUID());
        nm.sendPacket(packet);
    }

    public void sendPlayerTeleport(float x, float y, float z, int h) {
        MovementPacket packet = new MovementPacket(playerInfo.getUID(),
            OpCodes.WorldServer.Movement.DIRECTION.NO_MOVE.ordinal(), x, y, z, h);
        PlayerTeleportPacket playerTeleportPacket = new PlayerTeleportPacket(playerInfo.getUID(), packet);

        nm.sendPacket(playerTeleportPacket);
    }
}
