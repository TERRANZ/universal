package ru.terra.universal.shared.config;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.net.URL;

/**
 * User: Vadim Korostelev
 * Date: 09.09.13
 * Time: 15:46
 */
public class Config {

    private static final String CONFIG_FILE = "server.properties";

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
                    .configure(params.properties().setURL(locateConfigFile()));
            builder.setAutoSave(true);
            configuration = builder.getConfiguration();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Commons Configuration 2 dropped the automatic classpath fallback that 1.x
     * had, so locate the file explicitly: a file in the working directory wins,
     * otherwise fall back to the classpath (e.g. a resource inside a fat jar).
     */
    private static URL locateConfigFile() {
        final File external = new File(CONFIG_FILE);
        if (external.isFile()) {
            try {
                return external.toURI().toURL();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Config.class.getClassLoader().getResource(CONFIG_FILE);
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
