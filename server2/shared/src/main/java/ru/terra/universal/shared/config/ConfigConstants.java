package ru.terra.universal.shared.config;

public interface ConfigConstants {
    public static final String FRONTEND_IP = "frontend.ip";
    public static final String FRONTEND_IP_DEFAULT = "127.0.0.1";
    public static final String PERSISTENCE_DIR = "persistence.dir";
    public static final String PERSISTENCE_DIR_DEFAULT = "saves/";
    public static final String CHARACTERS_FILE = "persistence.characters.filename";
    public static final String CHARACTERS_FILE_DEFAULT = "characters.json";
    public static final String ACCOUNTS_FILE = "persistence.accounts.filename";
    public static final String ACCOUNTS_FILE_DEFAULT = "accounts.json";
    public static final Integer FRONTEND_PORT = 12346;
}
