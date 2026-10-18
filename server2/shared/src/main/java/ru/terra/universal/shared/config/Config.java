package ru.terra.universal.shared.config;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 * User: Vadim Korostelev
 * Date: 09.09.13
 * Time: 15:46
 */
public class Config {

    private PropertiesConfiguration configuration;
    private static final Config instance = new Config();

    public static Config getConfig() {
        return instance;
    }

    private Config() {
        try {
            final Parameters params = new Parameters();
            final FileBasedConfigurationBuilder<PropertiesConfiguration> builder =
                new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
                    .configure(params.properties().setFileName("server.properties"));
            builder.setAutoSave(true);
            configuration = builder.getConfiguration();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }


    public String getValue(String key, String defVal) {
        if (configuration == null)
            return defVal;
        else {
            final String value = configuration.getString(key);
            return value != null ? value : defVal;
        }
    }


    public void setConfig(String key, String val) {
        if (configuration != null) {
            configuration.setProperty(key, val);
        }

    }
}
