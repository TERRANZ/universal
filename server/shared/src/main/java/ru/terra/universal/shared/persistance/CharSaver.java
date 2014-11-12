package ru.terra.universal.shared.persistance;

import ru.terra.universal.shared.entity.PlayerInfo;

import java.util.List;

/**
 * Date: 25.04.14
 * Time: 14:46
 */
public interface CharSaver {
    public void save(PlayerInfo playerInfo);

    public void save(List<PlayerInfo> playerInfos);
}
