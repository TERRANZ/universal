package ru.terra.universal.shared.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.terra.universal.shared.config.ConfigConstants.FRONTEND_IP;
import static ru.terra.universal.shared.config.ConfigConstants.FRONTEND_IP_DEFAULT;

class ConfigTest {

    @Test
    void readsValueFromClasspathConfig() {
        // src/test/resources/server.properties ships frontend.ip=test-frontend
        assertEquals("test-frontend", Config.getConfig().getValue(FRONTEND_IP, FRONTEND_IP_DEFAULT));
    }

    @Test
    void returnsProvidedDefaultForUnknownKey() {
        assertEquals("fallback", Config.getConfig().getValue("definitely.not.present", "fallback"));
    }

    @Test
    void setConfigOverridesValue() {
        Config.getConfig().setConfig("test.key", "value");
        assertEquals("value", Config.getConfig().getValue("test.key", "default"));
    }
}
