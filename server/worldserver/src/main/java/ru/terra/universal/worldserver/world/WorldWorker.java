package ru.terra.universal.worldserver.world;

import org.apache.log4j.Logger;
import ru.terra.universal.interserver.network.NetworkManager;
import ru.terra.universal.shared.entity.PlayerInfo;
import ru.terra.universal.shared.entity.WorldEntity;
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
    private static WorldWorker instance = new WorldWorker();
    private final Map<Long, PlayerInfo> players = new HashMap<>();
    private WorldThread worldThread;
    private Logger log = Logger.getLogger(this.getClass());
    private NetworkManager networkManager = NetworkManager.getInstance();

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
        log.info("Received movement packet: " + packet.toString());
        synchronized (players) {
            players.values()
                    .stream()
                    .filter(p -> !p.getUID().equals(packet.getSender()))
                    .forEach(p -> networkManager.sendPacket(new MovementPacket(p.getUID(), packet)));
        }
    }

    public List<WorldEntity> getEntities() {
        return worldThread.getEntities();
    }

    public void updatePlayerPosition(MovementPacket packet) {
        log.info("Received movement packet: " + packet.toString());
        synchronized (players) {
            PlayerInfo playerInfo = getPlayers().get(packet.getSender());
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
        getPlayers().remove(sender);
        players.values()
                .stream()
                .filter(p -> !p.getUID().equals(sender))
                .forEach(p -> networkManager.sendPacket(new PlayerLoggedOutPacket(p.getUID(), sender)));
    }

    public void addPlayer(PlayerInfo playerInfo) {
        getPlayers().put(playerInfo.getUID(), playerInfo);
        players.values()
                .stream()
                .filter(p -> !p.getUID().equals(playerInfo.getUID()))
                .forEach(p -> networkManager.sendPacket(new PlayerLoggedInPacket(p.getUID(), playerInfo.getUID())));
    }
}
