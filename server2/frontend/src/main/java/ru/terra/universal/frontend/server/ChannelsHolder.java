package ru.terra.universal.frontend.server;

import org.jboss.netty.channel.Channel;

import java.util.HashMap;

class ChannelsHolder {
    private static final ChannelsHolder instance = new ChannelsHolder();
    private final HashMap<Integer, Channel> channels = new HashMap<>();

    private ChannelsHolder() {
    }

    static ChannelsHolder getInstance() {
        return instance;
    }

    synchronized Channel getChannel(final int opCode) {
        return channels.get(opCode);
    }

    synchronized void addChannel(final int opCode, final Channel channel) {
        channels.put(opCode, channel);
    }
}
