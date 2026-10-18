package ru.terra.universal.shared.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CryptoUtilTest {

    @ParameterizedTest
    @CsvSource({
        "awdawd,  189342e2ed9d23bb9a02ecbf8ed06762",
        "password,5f4dcc3b5aa765d61d8327deb882cf99",
        "hello,   5d41402abc4b2a76b9719d911017c592",
        "'',      d41d8cd98f00b204e9800998ecf8427e",
        "Привет,  8a669e9418750c81ab90ae159a8ec410"
    })
    void hashesKnownVectors(String input, String expected) {
        assertEquals(expected, CryptoUtil.encryptMD5(input.trim()));
    }

    @Test
    void isDeterministic() {
        assertEquals(CryptoUtil.encryptMD5("same-input"), CryptoUtil.encryptMD5("same-input"));
    }

    @Test
    void producesLowercase32CharHex() {
        final String hash = CryptoUtil.encryptMD5("some value");
        assertEquals(32, hash.length());
        assertTrue(hash.matches("[0-9a-f]{32}"), "expected lowercase hex, got " + hash);
    }
}
