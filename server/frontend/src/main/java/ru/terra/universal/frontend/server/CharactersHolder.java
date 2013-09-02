package ru.terra.universal.frontend.server;

import org.jboss.netty.channel.Channel;

import java.util.HashMap;

/**
 * User: Vadim Korostelev
 * Date: 02.09.13
 * Time: 11:03
 */
public class CharactersHolder {
    private static CharactersHolder instance = new CharactersHolder();
    private HashMap<Long, Channel> characters = new HashMap<>();

    protected CharactersHolder() {
    }

    public static CharactersHolder getInstance() {
        return instance;
    }

    public synchronized Channel getCharChannel(Long charUID) {
        return characters.get(charUID);
    }

    public synchronized void addCharChannel(Long charUID, Channel channel) {
        characters.put(charUID, channel);
    }

    public synchronized int size() {
        return characters.size();
    }
}
