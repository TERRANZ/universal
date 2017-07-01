package ru.terra.universal.shared.persistance.impl;

import flexjson.JSONDeserializer;
import ru.terra.universal.shared.config.Config;
import ru.terra.universal.shared.config.ConfigConstants;
import ru.terra.universal.shared.entity.AccountInfo;
import ru.terra.universal.shared.entity.PlayerInfo;
import ru.terra.universal.shared.persistance.CharLoader;
import ru.terra.universal.shared.persistance.FilePersister;
import ru.terra.universal.shared.util.CryptoUtil;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 25.04.14
 * Time: 14:39
 */
public class JsonCharLoaderImpl extends FilePersister implements CharLoader {

    private JSONDeserializer<List<PlayerInfo>> charsDeserializer = new JSONDeserializer<>();
    private JSONDeserializer<List<AccountInfo>> accountsDeserializer = new JSONDeserializer<>();
    private String charactersFileName = Config.getConfig().getValue(ConfigConstants.CHARACTERS_FILE, ConfigConstants.CHARACTERS_FILE_DEFAULT);
    private String accountsFileName = Config.getConfig().getValue(ConfigConstants.ACCOUNTS_FILE, ConfigConstants.ACCOUNTS_FILE_DEFAULT);

    @Override
    public PlayerInfo loadCharacter(Long uid) {
        for (PlayerInfo playerInfo : loadCharacters())
            if (playerInfo.getUID().equals(uid))
                return playerInfo;
        return null;
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
    public AccountInfo findAccount(String login, String pass) {
        List<AccountInfo> accounts = loadAccounts();
        for (AccountInfo ai : accounts) {
            if (ai.getLogin().equals(login) && ai.getPass().equals(CryptoUtil.encryptMD5(pass)))
                return ai;
        }
        return null;
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
