package ru.terra.universal.interserver.network.netty;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

public class ClientPipelineFactory implements ChannelPipelineFactory {

    private Class<? extends InterserverWorker> workerClass;

    public ClientPipelineFactory(Class<? extends InterserverWorker> workerClass) {
        super();
        this.workerClass = workerClass;
    }

    @Override
    public ChannelPipeline getPipeline() throws Exception {
        PacketFrameDecoder decoder = new PacketFrameDecoder();
        PacketFrameEncoder encoder = new PacketFrameEncoder();
        return Channels.pipeline(decoder, encoder, new InterserverHandler(decoder, encoder, workerClass));
    }

}
