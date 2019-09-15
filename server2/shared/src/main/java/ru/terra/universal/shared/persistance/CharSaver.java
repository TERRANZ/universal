package ru.terra.universal.shared.persistance;

import ru.terra.universal.shared.entity.AccountInfo;
import ru.terra.universal.shared.entity.PlayerInfo;

import java.util.List;

/**
 * Date: 25.04.14
 * Time: 14:46
 */
public interface CharSaver {
    void save(PlayerInfo playerInfo);

    void savePlayerInfos(List<PlayerInfo> playerInfos);

    void save(AccountInfo accountInfo);

    void saveAccountInfos(List<AccountInfo> accountInfos);
}
