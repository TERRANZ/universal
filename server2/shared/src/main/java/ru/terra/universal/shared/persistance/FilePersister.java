package ru.terra.universal.shared.persistance;

import ru.terra.universal.shared.config.Config;
import ru.terra.universal.shared.config.ConfigConstants;

import java.io.File;

import static ru.terra.universal.shared.config.ConfigConstants.PERSISTENCE_DIR;
import static ru.terra.universal.shared.config.ConfigConstants.PERSISTENCE_DIR_DEFAULT;

/**
 * Date: 25.04.14
 * Time: 15:05
 */
public abstract class FilePersister {
    protected String dir = Config.getConfig().getValue(PERSISTENCE_DIR, PERSISTENCE_DIR_DEFAULT);

    public FilePersister() {
        final File storageDir = new File(dir);
        if (!storageDir.exists())
            storageDir.mkdirs();
    }
}
