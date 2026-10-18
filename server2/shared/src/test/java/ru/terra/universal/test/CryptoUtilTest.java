package ru.terra.universal.test;

import org.junit.jupiter.api.Test;
import ru.terra.universal.shared.util.CryptoUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CryptoUtilTest {
    @Test
    void testMD5() {
        String val = "awdawd";
        String ethalon = "189342e2ed9d23bb9a02ecbf8ed06762";
        assertEquals(ethalon, CryptoUtil.encryptMD5(val));
    }
}
