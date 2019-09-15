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
    private static final CharactersHolder instance = new CharactersHolder();
    private final HashMap<Long, Channel> characters = new HashMap<>();
    private final Logger logger = Logger.getLogger(this.getClass());

    private CharactersHolder() {
    }

    static CharactersHolder getInstance() {
        return instance;
    }

    synchronized Channel getCharChannel(final Long charUID) {
        return characters.get(charUID);
    }

    synchronized void addCharChannel(final Long charUID, final Channel channel) {
        if (channel == null)
            logger.error("Channel is null");
        if (charUID == null)
            logger.error("charUID is null");
        logger.info("Adding channel " + channel.getId() + " for player " + charUID);
        characters.put(charUID, channel);
    }

    synchronized List<Long> getChannels() {
        return new ArrayList<>(characters.keySet());
    }

    public synchronized int size() {
        return characters.size();
    }

    synchronized Long removeChar(final Channel channel) {
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
