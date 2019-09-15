package ru.terra.universal.worldserver.world;

import ru.terra.universal.shared.entity.AbstractEntity;
import ru.terra.universal.shared.persistance.impl.JsonWorldLoaderImpl;
import ru.terra.universal.shared.persistance.impl.JsonWorldSaverImpl;
import ru.terra.universal.shared.world.World;
import ru.terra.universal.shared.world.WorldControlInterface;

import java.util.List;

/**
 * Date: 24.04.14
 * Time: 20:00
 */
public class WorldThread implements Runnable, WorldControlInterface {

    private World world;
    private boolean stop = false;

    WorldThread() {
        world = new World(new JsonWorldLoaderImpl(), new JsonWorldSaverImpl(), "uid");
    }

    @Override
    public void run() {
        world.load();
        while (!stop) {
            world.tick();
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    synchronized List<AbstractEntity> getEntities() {
        return world.getEntities();
    }

    @Override
    public void stop() {
        stop = true;
    }
}
