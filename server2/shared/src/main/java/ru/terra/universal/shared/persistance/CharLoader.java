package ru.terra.universal.shared.persistance;

import ru.terra.universal.shared.entity.AccountInfo;
import ru.terra.universal.shared.entity.PlayerInfo;

import java.util.List;
import java.util.Optional;

/**
 * Date: 25.04.14
 * Time: 14:34
 */
public interface CharLoader {
    Optional<PlayerInfo> loadCharacter(final Long uid);

    List<PlayerInfo> loadCharacters();

    Optional<AccountInfo> findAccount(final String login, final String pass);

    List<AccountInfo> loadAccounts();
}
