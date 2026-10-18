package ru.terra.universal.interserver.network.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import ru.terra.universal.shared.packet.AbstractPacket;

public class PacketFrameEncoder extends MessageToByteEncoder<AbstractPacket> {
    @Override
    protected void encode(ChannelHandlerContext ctx, AbstractPacket msg, ByteBuf out) throws Exception {
        AbstractPacket.write(msg, out);
    }
}
