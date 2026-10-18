package ru.terra.universal.shared.persistance;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.terra.universal.shared.config.Config;
import ru.terra.universal.shared.entity.AccountInfo;
import ru.terra.universal.shared.entity.PlayerInfo;
import ru.terra.universal.shared.persistance.impl.JsonCharLoaderImpl;
import ru.terra.universal.shared.persistance.impl.JsonCharSaverImpl;
import ru.terra.universal.shared.util.CryptoUtil;

import java.io.File;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.terra.universal.shared.config.ConfigConstants.PERSISTENCE_DIR;
import static ru.terra.universal.shared.config.ConfigConstants.PERSISTENCE_DIR_DEFAULT;

class JsonPersistenceTest {

    private final String dir = Config.getConfig().getValue(PERSISTENCE_DIR, PERSISTENCE_DIR_DEFAULT);
    private final CharSaver saver = new JsonCharSaverImpl();
    private final CharLoader loader = new JsonCharLoaderImpl();

    @BeforeEach
    @AfterEach
    void cleanStorage() {
        deleteRecursively(new File(dir));
    }

    @Test
    void savesAndLoadsCharacter() {
        final PlayerInfo player = new PlayerInfo("Hero", "newScene");
        player.setUID(100L);
        player.setHp(50);
        player.setMp(10);
        player.setXp(5);
        player.setLevel(3);
        player.setX(1.5f);
        player.setY(2.5f);
        player.setZ(3.5f);

        saver.save(player);

        final Optional<PlayerInfo> loaded = loader.loadCharacter(100L);
        assertTrue(loaded.isPresent());
        assertEquals("Hero", loaded.get().getName());
        assertEquals("newScene", loaded.get().getWorld());
        assertEquals(50, loaded.get().getHp().intValue());
        assertEquals(3, loaded.get().getLevel().intValue());
        assertEquals(1.5f, loaded.get().getX().floatValue(), 0.0001f);
        assertEquals(1, loader.loadCharacters().size());
    }

    @Test
    void resavingACharacterReplacesTheStoredEntry() {
        final PlayerInfo player = new PlayerInfo("Hero", "newScene");
        player.setUID(100L);
        player.setHp(50);
        saver.save(player);

        player.setHp(999);
        saver.save(player);

        assertEquals(1, loader.loadCharacters().size());
        assertEquals(999, loader.loadCharacter(100L).orElseThrow().getHp().intValue());
    }

    @Test
    void storesSeveralCharacters() {
        final PlayerInfo first = new PlayerInfo("Hero", "newScene");
        first.setUID(1L);
        final PlayerInfo second = new PlayerInfo("Villain", "newScene");
        second.setUID(2L);

        saver.save(first);
        saver.save(second);

        assertEquals(2, loader.loadCharacters().size());
        assertTrue(loader.loadCharacter(1L).isPresent());
        assertTrue(loader.loadCharacter(2L).isPresent());
    }

    @Test
    void savesAndFindsAccount() {
        final AccountInfo account = new AccountInfo(
            "bob", CryptoUtil.encryptMD5("secret"), false, new Date(), 0L, 0, 55L);
        saver.save(account);

        final Optional<AccountInfo> found = loader.findAccount("bob", "secret");
        assertTrue(found.isPresent());
        assertEquals(55L, found.get().getUid().longValue());
        assertEquals("bob", found.get().getLogin());

        assertFalse(loader.findAccount("bob", "wrong-password").isPresent());
        assertEquals(1, loader.loadAccounts().size());
    }

    @Test
    void unknownCharacterIsEmpty() {
        assertTrue(loader.loadCharacter(404L).isEmpty());
    }

    private static void deleteRecursively(File file) {
        if (file == null || !file.exists()) {
            return;
        }
        final File[] children = file.listFiles();
        if (children != null) {
            for (File child : children) {
                deleteRecursively(child);
            }
        }
        file.delete();
    }
}
