package ru.terra.universal.frontend.server;

import org.jboss.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * User: Vadim Korostelev
 * Date: 02.09.13
 * Time: 11:39
 */
public class TempCharactersHolder {
    private static TempCharactersHolder instance = new TempCharactersHolder();
    private Map<Long, Channel> channels = new HashMap<>();

    private TempCharactersHolder() {
    }

    public static TempCharactersHolder getInstance() {
        return instance;
    }

    public Channel getTempChannel(Long id) {
        synchronized (channels) {
            return channels.get(id);
        }
    }

    public void addTempChannel(Long id, Channel channel) {
        synchronized (channels) {
            channels.put(id, channel);
        }
    }

    public long getUnusedId() {
        return UUID.randomUUID().getLeastSignificantBits();
    }

    public void deleteTempChannel(Long id) {
        {
            channels.remove(id);
        }
    }
}
