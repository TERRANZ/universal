package ru.terra.universal.shared.packet.interserver;

import io.netty.buffer.ByteBuf;
import ru.terra.universal.shared.annoations.Packet;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.packet.AbstractPacket;

@Packet(opCode = OpCodes.InterServer.ISMSG_BOOT_CHAR)
public class BootCharPacket extends AbstractPacket {
    public BootCharPacket() {
    }

    public BootCharPacket(final Long sender) {
        super(sender);
    }

    @Override
    public void get(ByteBuf buffer) {

    }

    @Override
    public void send(ByteBuf buffer) {

    }
}
