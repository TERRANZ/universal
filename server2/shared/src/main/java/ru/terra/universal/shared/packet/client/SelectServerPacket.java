package ru.terra.universal.shared.packet.client;

import io.netty.buffer.ByteBuf;
import ru.terra.universal.shared.annoations.Packet;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.packet.AbstractPacket;

@Packet(opCode = OpCodes.Client.Char.CMSG_SELECT_SERVER)
public class SelectServerPacket extends AbstractPacket {

    private String targetWorld = "";

    @Override
    public void get(ByteBuf buffer) {
        targetWorld = readString(buffer);
    }

    @Override
    public void send(ByteBuf buffer) {
        writeString(buffer, targetWorld);
    }

    public String getTargetWorld() {
        return targetWorld;
    }

    public void setTargetWorld(String targetWorld) {
        this.targetWorld = targetWorld;
    }
}
