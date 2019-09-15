package ru.terra.universal.shared.persistance;

import ru.terra.universal.shared.entity.AccountInfo;
import ru.terra.universal.shared.entity.PlayerInfo;

import java.util.List;

/**
 * Date: 25.04.14
 * Time: 14:46
 */
public interface CharSaver {
    void save(final PlayerInfo playerInfo);

    void savePlayerInfos(final List<PlayerInfo> playerInfos);

    void save(final AccountInfo accountInfo);

    void saveAccountInfos(final List<AccountInfo> accountInfos);
}
