package ru.terra.universal.frontend.server;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * User: Vadim Korostelev
 * Date: 02.09.13
 * Time: 11:03
 */
public class CharactersHolder {
    private static CharactersHolder instance = new CharactersHolder();
    private HashMap<Long, Channel> characters = new HashMap<>();
    private Logger logger = Logger.getLogger(this.getClass());

    protected CharactersHolder() {
    }

    public static CharactersHolder getInstance() {
        return instance;
    }

    public synchronized Channel getCharChannel(Long charUID) {
        return characters.get(charUID);
    }

    public synchronized void addCharChannel(Long charUID, Channel channel) {
        if (channel == null)
            logger.error("Channel is null");
        if (charUID == null)
            logger.error("charUID is null");
        logger.info("Adding channel " + channel.getId() + " for player " + charUID);
        characters.put(charUID, channel);
    }

    public synchronized List<Long> getChannels() {
        return new ArrayList<>(characters.keySet());
    }

    public synchronized int size() {
        return characters.size();
    }

    public synchronized Long removeChar(Channel channel) {
        Long removedPlayer = null;
        for (Long guid : characters.keySet()) {
            if (characters.get(guid).equals(channel))
                removedPlayer = guid;
        }
        if (removedPlayer != null) {
            logger.info("Removing player " + removedPlayer);
            logger.info("Removed channel " + characters.remove(removedPlayer).getId());
            return removedPlayer;
        }
        return null;
    }
}
