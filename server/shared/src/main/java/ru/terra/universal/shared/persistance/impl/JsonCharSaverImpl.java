package ru.terra.universal.shared.persistance.impl;

import flexjson.JSONSerializer;
import ru.terra.universal.shared.config.Config;
import ru.terra.universal.shared.config.ConfigConstants;
import ru.terra.universal.shared.entity.AccountInfo;
import ru.terra.universal.shared.entity.PlayerInfo;
import ru.terra.universal.shared.persistance.CharLoader;
import ru.terra.universal.shared.persistance.CharSaver;
import ru.terra.universal.shared.persistance.FilePersister;

import java.io.*;
import java.util.List;

/**
 * Date: 25.04.14
 * Time: 14:46
 */
public class JsonCharSaverImpl extends FilePersister implements CharSaver {
    private String charactersFileName = Config.getConfig().getValue(ConfigConstants.CHARACTERS_FILE, ConfigConstants.CHARACTERS_FILE_DEFAULT);
    private String accountsFileName = Config.getConfig().getValue(ConfigConstants.ACCOUNTS_FILE, ConfigConstants.ACCOUNTS_FILE_DEFAULT);

    @Override
    public void save(PlayerInfo playerInfo) {
        CharLoader charLoader = new JsonCharLoaderImpl();
        List<PlayerInfo> playerInfos = charLoader.loadCharacters();
        PlayerInfo playerInfoToRemove = null;
        for (PlayerInfo pi : playerInfos)
            if (pi.getUID().equals(playerInfo.getUID()))
                playerInfoToRemove = pi;
        if (playerInfoToRemove != null)
            playerInfos.remove(playerInfoToRemove);

        playerInfos.add(playerInfo);
        savePlayerInfos(playerInfos);
    }

    @Override
    public void savePlayerInfos(List<PlayerInfo> playerInfos) {
        try {
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dir + charactersFileName), "UTF-8"));
            new JSONSerializer().serialize(playerInfos, out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(AccountInfo accountInfo) {
        CharLoader charLoader = new JsonCharLoaderImpl();
        List<AccountInfo> accountInfos = charLoader.loadAccounts();
        AccountInfo aiToRemove = null;
        for (AccountInfo ai : accountInfos)
            if (ai.getLogin().equals(accountInfo.getLogin()))
                aiToRemove = ai;
        if (aiToRemove != null)
            accountInfos.remove(aiToRemove);

        accountInfos.add(accountInfo);
        saveAccountInfos(accountInfos);
    }

    @Override
    public void saveAccountInfos(List<AccountInfo> accountInfos) {
        try {
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dir + accountsFileName), "UTF-8"));
            new JSONSerializer().serialize(accountInfos, out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
