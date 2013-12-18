package ru.terra.universal.frontend.server;

import org.jboss.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

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

    public synchronized Channel getTempChannel(Long id) {
        return channels.get(id);
    }

    public synchronized void addTempChannel(Long id, Channel channel) {
        channels.put(id, channel);
    }

    public synchronized long size() {
        return channels.size();
    }

    public synchronized void deleteTempChannel(Long id) {
        channels.remove(id);
    }
}
