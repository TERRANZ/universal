package ru.terra.universal.shared.packet.client;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.universal.shared.annoations.Packet;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.packet.AbstractPacket;

@Packet(opCode = OpCodes.Client.Login.CMSG_LOGOUT)
public class LogoutPacket extends AbstractPacket {
    public LogoutPacket() {
    }

    @Override
    public void get(ChannelBuffer buffer) {
    }

    @Override
    public void send(ChannelBuffer buffer) {
    }
}
