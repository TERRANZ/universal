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
class TempCharactersHolder {
    private static final TempCharactersHolder instance = new TempCharactersHolder();
    private final Map<Long, Channel> channels = new HashMap<>();

    private TempCharactersHolder() {
    }

    static TempCharactersHolder getInstance() {
        return instance;
    }

    Channel getTempChannel(final Long id) {
        synchronized (channels) {
            return channels.get(id);
        }
    }

    void addTempChannel(final Long id, final Channel channel) {
        synchronized (channels) {
            channels.put(id, channel);
        }
    }

    long getUnusedId() {
        return UUID.randomUUID().getLeastSignificantBits();
    }

    void deleteTempChannel(final Long id) {
        channels.remove(id);
    }
}
