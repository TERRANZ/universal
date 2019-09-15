package ru.terra.universal.shared.world;

import org.apache.log4j.Logger;
import ru.terra.universal.shared.annoations.Module;

/**
 * Date: 24.04.14
 * Time: 19:53
 */
public abstract class WorldModule {
    protected Logger logger = Logger.getLogger(this.getClass());

    public abstract void tick(World world);

    String getName() {
        return this.getClass().getAnnotation(Module.class).value();
    }
}
