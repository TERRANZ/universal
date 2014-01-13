package ru.terra.universal.worldserver.world;

import ru.terra.universal.shared.entity.PlayerInfo;
import ru.terra.universal.shared.entity.WorldEntityInfo;
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
    private List<WorldEntityInfo> entities = new LinkedList<>();

    private WorldWorker() {
    }

    public static WorldWorker getInstance() {
        return instance;
    }


    public synchronized List<PlayerInfo> getPlayers() {
        return players;
    }

    public synchronized List<WorldEntityInfo> getEntities() {
        return entities;
    }

    public void playerMove(MovementPacket movementPacket) {
    }


}
