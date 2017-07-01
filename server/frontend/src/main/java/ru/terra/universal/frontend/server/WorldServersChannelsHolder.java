package ru.terra.universal.frontend.server;

import org.jboss.netty.channel.Channel;
import ru.terra.universal.shared.constants.OpCodes;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by terranz on 02.07.17.
 */
public class WorldServersChannelsHolder {
    private static WorldServersChannelsHolder instance = new WorldServersChannelsHolder();
    private Map<String, Map<Integer, Channel>> channels = new HashMap<>();

    private WorldServersChannelsHolder() {
    }

    public static WorldServersChannelsHolder getInstance() {
        return instance;
    }

    public synchronized Boolean isWorldAvailable(String world) {
        return channels.containsKey(world);
    }

    public synchronized Channel getChannel(String world) {
        return channels.get(world).get(OpCodes.WorldOpcodeStart);
    }

    public synchronized void addChannel(String world, int opCode, Channel channel) {
        channels.computeIfAbsent(world, k -> new HashMap<>());
        channels.get(world).put(opCode, channel);
    }

    public Map<String, Map<Integer, Channel>> getChannels() {
        return channels;
    }
}
