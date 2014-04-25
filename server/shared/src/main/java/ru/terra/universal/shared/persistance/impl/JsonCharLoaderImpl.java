package ru.terra.universal.shared.persistance.impl;

import ru.terra.universal.shared.entity.PlayerInfo;
import ru.terra.universal.shared.persistance.CharLoader;

import java.util.List;
import java.util.UUID;

/**
 * Date: 25.04.14
 * Time: 14:39
 */
public class JsonCharLoaderImpl implements CharLoader {
    @Override
    public PlayerInfo loadCharacter(String uid) {
        PlayerInfo playerInfo = new PlayerInfo();
        playerInfo.setUID(uid);
        playerInfo.setName("My Cool player " + playerInfo.getUID());
        String wuid = UUID.randomUUID().toString();
        playerInfo.setWorld(wuid);
        return playerInfo;
    }

    @Override
    public List<String> loadCharacters() {
        return null;
    }
}
