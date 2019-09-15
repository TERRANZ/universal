package ru.terra.universal.shared.persistance.impl;

import flexjson.JSONDeserializer;
import ru.terra.universal.shared.entity.AccountInfo;
import ru.terra.universal.shared.entity.PlayerInfo;
import ru.terra.universal.shared.persistance.CharLoader;
import ru.terra.universal.shared.persistance.FilePersister;
import ru.terra.universal.shared.util.CryptoUtil;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.terra.universal.shared.config.Config.getConfig;
import static ru.terra.universal.shared.config.ConfigConstants.*;

/**
 * Date: 25.04.14
 * Time: 14:39
 */
public class JsonCharLoaderImpl extends FilePersister implements CharLoader {

    private final JSONDeserializer<List<PlayerInfo>> charsDeserializer = new JSONDeserializer<>();
    private final JSONDeserializer<List<AccountInfo>> accountsDeserializer = new JSONDeserializer<>();
    private final String charactersFileName = getConfig().getValue(CHARACTERS_FILE, CHARACTERS_FILE_DEFAULT);
    private final String accountsFileName = getConfig().getValue(ACCOUNTS_FILE, ACCOUNTS_FILE_DEFAULT);

    @Override
    public Optional<PlayerInfo> loadCharacter(Long uid) {
        for (PlayerInfo playerInfo : loadCharacters())
            if (playerInfo.getUID().equals(uid))
                return Optional.of(playerInfo);
        return Optional.empty();
    }

    @Override
    public List<PlayerInfo> loadCharacters() {
        try {
            return charsDeserializer.use(null, ArrayList.class).use("values", PlayerInfo.class).deserialize(new FileReader(dir + charactersFileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public Optional<AccountInfo> findAccount(String login, String pass) {
        List<AccountInfo> accounts = loadAccounts();
        for (AccountInfo ai : accounts) {
            if (ai.getLogin().equals(login) && ai.getPass().equals(CryptoUtil.encryptMD5(pass)))
                return Optional.of(ai);
        }
        return Optional.empty();
    }

    @Override
    public List<AccountInfo> loadAccounts() {
        try {
            return accountsDeserializer.use(null, ArrayList.class).use("values", AccountInfo.class).deserialize(new FileReader(dir + accountsFileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
