package ru.terra.universal.shared.persistance.impl;

import flexjson.JSONDeserializer;
import ru.terra.universal.shared.config.Config;
import ru.terra.universal.shared.config.ConfigConstants;
import ru.terra.universal.shared.entity.PlayerInfo;
import ru.terra.universal.shared.persistance.CharLoader;
import ru.terra.universal.shared.persistance.FilePersister;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Date: 25.04.14
 * Time: 14:39
 */
public class JsonCharLoaderImpl extends FilePersister implements CharLoader {

    private JSONDeserializer<List<PlayerInfo>> deserializer = new JSONDeserializer<>();
    private String fileName = Config.getConfig().getValue(ConfigConstants.CHARACTERS_FILE, ConfigConstants.CHARACTERS_FILE_DEFAULT);

    @Override
    public PlayerInfo loadCharacter(Long uid) {
        for (PlayerInfo playerInfo : loadCharacters())
            if (playerInfo.getUID().equals(uid))
                return playerInfo;
        PlayerInfo playerInfo = new PlayerInfo();
        playerInfo.setUID(uid);
        playerInfo.setName("My Cool player " + playerInfo.getUID());
        playerInfo.setWorld("newScene");
        return playerInfo;
    }

    @Override
    public List<PlayerInfo> loadCharacters() {
        try {
            return deserializer.use(null, ArrayList.class).use("values", PlayerInfo.class).deserialize(new FileReader(dir + fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public Long findCharacter(String login, String pass) {
        //TODO:
//        List<PlayerInfo> playerInfos = loadCharacters();
//        if (playerInfos.size() > 0)
//            return playerInfos.get(0).getUID();
        Long uid = UUID.randomUUID().getLeastSignificantBits();
        return uid;
    }
}
