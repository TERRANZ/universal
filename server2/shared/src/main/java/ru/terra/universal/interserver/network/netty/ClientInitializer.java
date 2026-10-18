package ru.terra.universal.interserver.network.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

public class ClientInitializer extends ChannelInitializer<Channel> {

    private final Class<? extends InterserverWorker> workerClass;

    public ClientInitializer(Class<? extends InterserverWorker> workerClass) {
        this.workerClass = workerClass;
    }

    @Override
    protected void initChannel(Channel ch) {
        final PacketFrameDecoder decoder = new PacketFrameDecoder();
        final PacketFrameEncoder encoder = new PacketFrameEncoder();
        ch.pipeline().addLast(decoder, encoder, new InterserverHandler(decoder, encoder, workerClass));
    }
}
