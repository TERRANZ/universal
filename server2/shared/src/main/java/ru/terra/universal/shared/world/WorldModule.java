package ru.terra.universal.shared.world;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.terra.universal.shared.annoations.Module;

/**
 * Date: 24.04.14
 * Time: 19:53
 */
public abstract class WorldModule {
    protected Logger logger = LogManager.getLogger(this.getClass());

    public abstract void tick(World world);

    String getName() {
        return this.getClass().getAnnotation(Module.class).value();
    }
}
