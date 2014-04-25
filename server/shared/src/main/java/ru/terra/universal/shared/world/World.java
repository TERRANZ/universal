package ru.terra.universal.shared.world;

import org.apache.log4j.Logger;
import ru.terra.universal.shared.annoations.Module;
import ru.terra.universal.shared.config.Config;
import ru.terra.universal.shared.core.ClassSearcher;
import ru.terra.universal.shared.entity.WorldEntity;
import ru.terra.universal.shared.persistance.WorldLoader;
import ru.terra.universal.shared.persistance.WorldSaver;

import java.util.List;

/**
 * Date: 24.04.14
 * Time: 19:56
 */
public class World {
    private WorldLoader worldLoader;
    private WorldSaver worldSaver;
    private List<WorldEntity> entities;
    private String worldUID;
    private List<WorldModule> modules;
    private Config config = Config.getConfig();
    private Logger logger = Logger.getLogger(this.getClass());

    public World(WorldLoader worldLoader, WorldSaver worldSaver, String worldUID) {
        this.worldLoader = worldLoader;
        this.worldSaver = worldSaver;
        this.worldUID = worldUID;
        modules = new ClassSearcher<WorldModule>().load("ru.terra.universal.worldserver.module", Module.class);
        for (WorldModule module : modules)
            logger.info("Loaded module " + module.getName());
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
