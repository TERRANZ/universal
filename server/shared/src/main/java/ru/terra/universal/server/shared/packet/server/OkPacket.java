package ru.terra.universal.server.shared.packet.server;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.universal.server.shared.constants.OpCodes;
import ru.terra.universal.server.shared.packet.Packet;

/**
 * User: Vadim Korostelev
 * Date: 30.08.13
 * Time: 16:20
 */
public class OkPacket extends Packet {
    public OkPacket(long sender) {
        super(OpCodes.Server.SMSG_OK, sender);
    }

    @Override
    public void get(ChannelBuffer buffer) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void send(ChannelBuffer buffer) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
