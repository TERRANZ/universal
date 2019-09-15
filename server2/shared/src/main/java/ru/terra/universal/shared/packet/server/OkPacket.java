package ru.terra.universal.shared.packet.server;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.universal.shared.annoations.Packet;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.packet.AbstractPacket;

/**
 * User: Vadim Korostelev
 * Date: 30.08.13
 * Time: 16:20
 */
@Packet(opCode = OpCodes.Server.SMSG_OK)
public class OkPacket extends AbstractPacket {

    public OkPacket() {
    }

    public OkPacket(final Long sender) {
        super(sender);
    }

    @Override
    public void get(ChannelBuffer buffer) {
    }

    @Override
    public void send(ChannelBuffer buffer) {
    }
}
