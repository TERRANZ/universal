package ru.terra.universal.shared.packet.interserver;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.universal.shared.annoations.Packet;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.packet.AbstractPacket;

/**
 * User: Vadim Korostelev
 * Date: 17.09.13
 * Time: 17:07
 */
@Packet(opCode = OpCodes.InterServer.ISMSG_UNREG_CHAR)
public class UnregCharPacket extends AbstractPacket {
    public UnregCharPacket() {
    }

    public UnregCharPacket(final Long sender) {
        super(sender);
    }

    @Override
    public void get(ChannelBuffer buffer) {

    }

    @Override
    public void send(ChannelBuffer buffer) {

    }
}
