package ru.terra.universal.shared.packet.interserver;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.universal.shared.annoations.Packet;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.packet.AbstractPacket;

@Packet(opCode = OpCodes.InterServer.ISMSG_LOGIN_TO_WORLD)
public class LoginToWorldPacket extends AbstractPacket {
    @Override
    public void get(ChannelBuffer buffer) {
    }

    @Override
    public void send(ChannelBuffer buffer) {
    }
}
