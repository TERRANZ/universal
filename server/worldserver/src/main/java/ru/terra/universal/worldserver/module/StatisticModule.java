package ru.terra.universal.worldserver.module;

import ru.terra.universal.shared.annoations.Module;
import ru.terra.universal.shared.world.World;
import ru.terra.universal.shared.world.WorldModule;

import java.util.Date;

/**
 * Date: 25.04.14
 * Time: 14:09
 */
@Module("statistic")
public class StatisticModule extends WorldModule {
    private Date lastTickDate = new Date();

    public StatisticModule() {
    }

    @Override
    public void tick(World world) {
        Date newDate = new Date();
        //logger.info("tick time: " + (newDate.getTime() - lastTickDate.getTime()));
        lastTickDate = newDate;
    }
}
