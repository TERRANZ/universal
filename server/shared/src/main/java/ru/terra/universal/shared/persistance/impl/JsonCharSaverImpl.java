package ru.terra.universal.shared.persistance.impl;

import flexjson.JSONSerializer;
import ru.terra.universal.shared.config.Config;
import ru.terra.universal.shared.config.ConfigConstants;
import ru.terra.universal.shared.entity.PlayerInfo;
import ru.terra.universal.shared.persistance.CharLoader;
import ru.terra.universal.shared.persistance.CharSaver;
import ru.terra.universal.shared.persistance.FilePersister;

import java.io.*;
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
        CharLoader charLoader = new JsonCharLoaderImpl();
        List<PlayerInfo> playerInfos = charLoader.loadCharacters();
        PlayerInfo playerInfoToRemove = null;
        for (PlayerInfo pi : playerInfos)
            if (pi.getUID().equals(playerInfo))
                playerInfoToRemove = pi;
        if (playerInfoToRemove != null)
            playerInfos.remove(playerInfoToRemove);

        playerInfos.add(playerInfo);
        save(playerInfos);
    }

    @Override
    public void save(List<PlayerInfo> playerInfos) {
        try {
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dir + fileName), "UTF-8"));
            jsonSerializer.serialize(playerInfos, out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
