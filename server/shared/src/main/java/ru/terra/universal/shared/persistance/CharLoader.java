package ru.terra.universal.shared.persistance;

import ru.terra.universal.shared.entity.PlayerInfo;

import java.util.List;

/**
 * Date: 25.04.14
 * Time: 14:34
 */
public interface CharLoader {
    public PlayerInfo loadCharacter(String uid);

    public List<String> loadCharacters();
}
