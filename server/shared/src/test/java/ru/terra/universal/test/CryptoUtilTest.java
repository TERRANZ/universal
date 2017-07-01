package ru.terra.universal.test;

import org.junit.Assert;
import org.junit.Test;
import ru.terra.universal.shared.util.CryptoUtil;

public class CryptoUtilTest {
    @Test
    public void testMD5() {
        String val = "awdawd";
        String ethalon = "189342e2ed9d23bb9a02ecbf8ed06762";
        Assert.assertEquals(ethalon, CryptoUtil.encryptMD5(val));
    }
}
