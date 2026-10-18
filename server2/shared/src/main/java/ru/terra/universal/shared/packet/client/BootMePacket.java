package ru.terra.universal.shared.packet.client;

import io.netty.buffer.ByteBuf;
import ru.terra.universal.shared.annoations.Packet;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.packet.AbstractPacket;

@Packet(opCode = OpCodes.Client.Login.CSMG_BOOT_ME)
public class BootMePacket extends AbstractPacket {
    @Override
    public void get(ByteBuf buffer) {

    }

    @Override
    public void send(ByteBuf buffer) {

    }
}
