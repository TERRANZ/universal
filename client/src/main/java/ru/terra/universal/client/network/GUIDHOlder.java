package ru.terra.universal.client.network;

import org.apache.log4j.Logger;

import java.util.Date;

/**
 * User: Vadim Korostelev
 * Date: 02.09.13
 * Time: 12:29
 */
public class GUIDHOlder {
    private static GUIDHOlder instance = new GUIDHOlder();
    private long guid = new Date().getTime();
    private Logger logger = Logger.getLogger(this.getClass());

    private GUIDHOlder() {
    }

    public static GUIDHOlder getInstance() {
        return instance;
    }

    public long getGuid() {
        return guid;
    }

    public void setGuid(long guid) {
        logger.info("Now our guid is " + guid);
        this.guid = guid;
    }
}
