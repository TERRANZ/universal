package ru.terra.universal.frontend.server;

import org.jboss.netty.channel.Channel;

import java.util.HashMap;

public class ChannelsHolder {
    private static ChannelsHolder instance = new ChannelsHolder();
    private HashMap<Integer, Channel> channels = new HashMap<>();

    private ChannelsHolder() {
    }

    public static ChannelsHolder getInstance() {
        return instance;
    }

    public synchronized Channel getChannel(int opCode) {
        return channels.get(opCode);
    }

    public synchronized void addChannel(int opCode, Channel channel) {
        channels.put(opCode, channel);
    }
}
