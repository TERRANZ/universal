package ru.terra.universal.shared.persistance.impl;

import flexjson.JSONSerializer;
import ru.terra.universal.shared.config.Config;
import ru.terra.universal.shared.config.ConfigConstants;
import ru.terra.universal.shared.entity.PlayerInfo;
import ru.terra.universal.shared.persistance.CharSaver;
import ru.terra.universal.shared.persistance.FilePersister;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Date: 25.04.14
 * Time: 14:46
 */
public class JsonCharSaverImpl extends FilePersister implements CharSaver {
    private JSONSerializer jsonSerializer = new JSONSerializer();
    private String fileName = Config.getConfig().getValue(ConfigConstants.CHARACTERS_FILE, ConfigConstants.CHARACTERS_FILE_DEFAULT);

    @Override
    public void save(PlayerInfo playerInfo) {
    }

    @Override
    public void save(List<PlayerInfo> playerInfos) {
        try {
            jsonSerializer.serialize(playerInfos, new FileWriter(new File(dir + fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
