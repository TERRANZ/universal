package ru.terra.universal.worldserver.world;

import org.apache.log4j.Logger;
import ru.terra.universal.interserver.network.NetworkManager;
import ru.terra.universal.shared.entity.AbstractEntity;
import ru.terra.universal.shared.entity.PlayerInfo;
import ru.terra.universal.shared.packet.interserver.UpdatePlayerPacket;
import ru.terra.universal.shared.packet.movement.MovementPacket;
import ru.terra.universal.shared.packet.movement.PlayerTeleportPacket;
import ru.terra.universal.shared.packet.worldserver.PlayerLoggedInPacket;
import ru.terra.universal.shared.packet.worldserver.PlayerLoggedOutPacket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date: 13.01.14
 * Time: 14:41
 */
public class WorldWorker {
    private static final WorldWorker instance = new WorldWorker();
    private final Map<Long, PlayerInfo> players = new HashMap<>();
    private WorldThread worldThread;
    private final Logger log = Logger.getLogger(this.getClass());
    private final NetworkManager networkManager = NetworkManager.getInstance();

    public synchronized Map<Long, PlayerInfo> getPlayers() {
        return players;
    }

    private WorldWorker() {
        worldThread = new WorldThread();
        new Thread(worldThread).start();
    }

    public static WorldWorker getInstance() {
        return instance;
    }

    public void playerMove(MovementPacket packet) {
        synchronized (players) {
            players.values()
                    .stream()
                    .filter(p -> !p.getUID().equals(packet.getSender()))
                    .forEach(p -> networkManager.sendPacket(new MovementPacket(p.getUID(), packet)));
        }
    }

    public List<AbstractEntity> getEntities() {
        return worldThread.getEntities();
    }

    public void updatePlayerPosition(MovementPacket packet) {
        synchronized (players) {
            final PlayerInfo playerInfo = getPlayers().get(packet.getSender());
            playerInfo.setX(packet.getX());
            playerInfo.setY(packet.getY());
            playerInfo.setZ(packet.getZ());
            playerInfo.setH(packet.getH());
            players.values()
                    .stream()
                    .filter(p -> !p.getUID().equals(packet.getSender()))
                    .forEach(p -> networkManager.sendPacket(new PlayerTeleportPacket(p.getUID(), packet)));
        }
    }

    public void removePlayer(Long sender) {
        final PlayerInfo playerInfo = getPlayers().remove(sender);
        players.values().forEach(p -> networkManager.sendPacket(new PlayerLoggedOutPacket(p.getUID(), sender)));
        networkManager.sendPacket(new UpdatePlayerPacket(playerInfo));
    }

    public void addPlayer(PlayerInfo playerInfo) {
        players.values().forEach(p -> networkManager.sendPacket(new PlayerLoggedInPacket(p.getUID(), playerInfo.getUID(), playerInfo)));
        getPlayers().put(playerInfo.getUID(), playerInfo);
    }
}
