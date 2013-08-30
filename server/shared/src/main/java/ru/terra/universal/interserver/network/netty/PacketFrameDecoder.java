package ru.terra.universal.interserver.network.netty;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.handler.codec.replay.ReplayingDecoder;
import org.jboss.netty.handler.codec.replay.VoidEnum;
import ru.terra.universal.server.shared.packet.Packet;

public class PacketFrameDecoder extends ReplayingDecoder<VoidEnum> {
    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        ctx.sendUpstream(e);
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        ctx.sendUpstream(e);
    }

    @Override
    protected Object decode(ChannelHandlerContext arg0, Channel arg1, ChannelBuffer buffer, VoidEnum e) throws Exception {
        return Packet.read(buffer);
    }
}