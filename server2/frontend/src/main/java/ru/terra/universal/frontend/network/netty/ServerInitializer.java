package ru.terra.universal.frontend.network.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

public class ServerInitializer extends ChannelInitializer<Channel> {

    private final Class<? extends ServerWorker> serverWorker;

    public ServerInitializer(Class<? extends ServerWorker> serverWorker) {
        this.serverWorker = serverWorker;
    }

    @Override
    protected void initChannel(Channel ch) {
        final PacketFrameDecoder decoder = new PacketFrameDecoder();
        final PacketFrameEncoder encoder = new PacketFrameEncoder();
        ch.pipeline().addLast(decoder, encoder, new ServerHandler(decoder, encoder, serverWorker));
    }
}
