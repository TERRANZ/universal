package ru.terra.universal.shared.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by terranz on 01.07.17.
 */
public class CryptoUtil {
    public static String encryptMD5(final String val) {
        try {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(val.getBytes());
            final byte[] byteData = md.digest();
            final StringBuilder sb = new StringBuilder();
            for (final byte byteDatum : byteData) {
                sb.append(Integer.toString((byteDatum & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
