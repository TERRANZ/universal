package ru.terra.universal.shared.world;

import ru.terra.universal.shared.config.Config;
import ru.terra.universal.shared.entity.WorldEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 24.04.14
 * Time: 19:56
 */
public abstract class World {
    protected WorldLoader worldLoader;
    protected WorldSaver worldSaver;
    protected List<WorldEntity> entities;
    protected String worldUID;
    protected List<WorldModule> modules = new ArrayList<>();
    protected Config config = Config.getConfig();

    protected World(WorldLoader worldLoader, WorldSaver worldSaver, String worldUID) {
        this.worldLoader = worldLoader;
        this.worldSaver = worldSaver;
        this.worldUID = worldUID;
        //load modules somehow
    }

    public synchronized void load() {
        entities = worldLoader.load(worldUID);
    }

    public synchronized void store() {
        worldSaver.save(worldUID, entities);
    }

    public synchronized void tick() {
        for (WorldModule worldModule : modules)
            worldModule.tick(this);
    }

    public List<WorldEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<WorldEntity> entities) {
        this.entities = entities;
    }

    public String getWorldUID() {
        return worldUID;
    }

    public void setWorldUID(String worldUID) {
        this.worldUID = worldUID;
    }
}
