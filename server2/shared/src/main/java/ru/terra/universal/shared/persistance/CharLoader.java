package ru.terra.universal.shared.persistance;

import ru.terra.universal.shared.entity.AccountInfo;
import ru.terra.universal.shared.entity.PlayerInfo;

import java.util.List;

/**
 * Date: 25.04.14
 * Time: 14:34
 */
public interface CharLoader {
    PlayerInfo loadCharacter(Long uid);

    List<PlayerInfo> loadCharacters();

    AccountInfo findAccount(String login, String pass);

    List<AccountInfo> loadAccounts();
}
