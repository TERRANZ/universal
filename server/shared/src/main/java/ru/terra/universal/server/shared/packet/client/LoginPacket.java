package ru.terra.universal.server.shared.packet.client;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.universal.server.shared.constants.OpCodes;
import ru.terra.universal.server.shared.packet.Packet;

/**
 * User: Vadim Korostelev
 * Date: 30.08.13
 * Time: 16:20
 */
public class LoginPacket extends Packet {
    public LoginPacket(long sender) {
        super(OpCodes.Client.Login.CMSG_LOGIN, sender);
    }

    @Override
    public void get(ChannelBuffer buffer) {
    }

    @Override
    public void send(ChannelBuffer buffer) {
    }
}
