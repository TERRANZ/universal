package ru.terra.universal.shared.config;

import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * User: Vadim Korostelev
 * Date: 09.09.13
 * Time: 15:46
 */
public class Config {

    private PropertiesConfiguration configuration;
    private static Config instance = new Config();

    public static Config getConfig() {
        return instance;
    }

    private Config() {
        try {
            configuration = new PropertiesConfiguration("server.properties");
            configuration.setAutoSave(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getValue(String key, String defVal) {
        if (configuration == null)
            return defVal;
        else {
            return configuration.getString(key, defVal);
        }
    }


    public void setConfig(String key, String val) {
        if (configuration != null) {
            configuration.setProperty(key, val);
        }

    }
}
