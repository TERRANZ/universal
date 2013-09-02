package ru.terra.universal.frontend.server;

import org.jboss.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Vadim Korostelev
 * Date: 02.09.13
 * Time: 11:39
 */
public class TempCharactersHolder {
    private static TempCharactersHolder instance = new TempCharactersHolder();
    private List<Channel> channels = new ArrayList<>();

    private TempCharactersHolder() {
    }

    public static TempCharactersHolder getInstance() {
        return instance;
    }

    public synchronized Channel getTempChannel(Integer id) {
        return channels.get(id);
    }

    public synchronized void addTempChannel(Channel channel) {
        channels.add(channel);
    }

    public long size() {
        return channels.size();
    }

    public synchronized void deleteTempChannel(int id) {
        channels.remove(id);
    }
}
