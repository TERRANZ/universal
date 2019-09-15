package ru.terra.universal.frontend.server;

import org.jboss.netty.channel.Channel;
import ru.terra.universal.shared.constants.OpCodes;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by terranz on 02.07.17.
 */
class WorldServersChannelsHolder {
    private static final WorldServersChannelsHolder instance = new WorldServersChannelsHolder();
    private final Map<String, Map<Integer, Channel>> channels = new HashMap<>();

    private WorldServersChannelsHolder() {
    }

    static WorldServersChannelsHolder getInstance() {
        return instance;
    }

    synchronized Boolean isWorldAvailable(final String world) {
        return channels.containsKey(world);
    }

    synchronized Channel getChannel(final String world) {
        return channels.get(world).get(OpCodes.WorldOpcodeStart);
    }

    synchronized void addChannel(final String world, final int opCode, final Channel channel) {
        channels.computeIfAbsent(world, k -> new HashMap<>());
        channels.get(world).put(opCode, channel);
    }

    Map<String, Map<Integer, Channel>> getChannels() {
        return channels;
    }
}
