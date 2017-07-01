package ru.terra.universal.shared.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by terranz on 01.07.17.
 */
public class CryptoUtil {
    public static String encryptMD5(String val) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(val.getBytes());
            byte byteData[] = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
