package ru.terra.universal.shared.packet.interserver;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.universal.shared.annoations.Packet;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.packet.AbstractPacket;

@Packet(opCode = OpCodes.InterServer.ISMSG_CHAT_TO_WORLD)
public class ChatToWorldPacket extends AbstractPacket {
    @Override
    public void get(ChannelBuffer buffer) {
    }

    @Override
    public void send(ChannelBuffer buffer) {
    }
}
