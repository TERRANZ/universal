package ru.terra.universal.frontend.network.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import ru.terra.universal.shared.packet.AbstractPacket;

import java.util.List;

public class PacketFrameDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
        out.add(AbstractPacket.read(buffer));
    }
}
