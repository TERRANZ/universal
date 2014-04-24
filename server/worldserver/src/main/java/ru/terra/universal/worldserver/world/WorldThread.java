package ru.terra.universal.worldserver.world;

import ru.terra.universal.shared.entity.WorldEntity;
import ru.terra.universal.shared.world.World;

import java.util.List;

/**
 * Date: 24.04.14
 * Time: 20:00
 */
public class WorldThread implements Runnable {

    private World world;

    @Override
    public void run() {
        world.load();
    }

    public synchronized List<WorldEntity> getEntities() {
        return world.getEntities();
    }
}
