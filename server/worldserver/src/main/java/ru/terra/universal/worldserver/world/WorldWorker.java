package ru.terra.universal.worldserver.world;

import ru.terra.universal.shared.entity.PlayerInfo;
import ru.terra.universal.shared.entity.WorldEntity;
import ru.terra.universal.shared.packet.client.MovementPacket;

import java.util.LinkedList;
import java.util.List;

/**
 * Date: 13.01.14
 * Time: 14:41
 */
public class WorldWorker {
    private static WorldWorker instance = new WorldWorker();
    private List<PlayerInfo> players = new LinkedList<>();
    private WorldThread worldThread;

    public synchronized List<PlayerInfo> getPlayers() {
        return players;
    }

    private WorldWorker() {
        worldThread = new WorldThread();
        new Thread(worldThread).start();
    }

    public static WorldWorker getInstance() {
        return instance;
    }

    public void playerMove(MovementPacket movementPacket) {
    }

    public List<WorldEntity> getEntities() {
        return worldThread.getEntities();
    }
}
