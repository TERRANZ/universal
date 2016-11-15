package ru.terra.universal.shared.packet.client;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.universal.shared.annoations.Packet;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.packet.AbstractPacket;

@Packet(opCode = OpCodes.Client.Char.CMSG_SELECT_SERVER)
public class SelectServerPacket extends AbstractPacket {
    private String targetWorld = "";

    public SelectServerPacket() {
    }

    @Override
    public void get(ChannelBuffer buffer) {
        targetWorld = readString(buffer);
    }

    @Override
    public void send(ChannelBuffer buffer) {
        writeString(buffer, targetWorld);
    }

    public String getTargetWorld() {
        return targetWorld;
    }

    public void setTargetWorld(String targetWorld) {
        this.targetWorld = targetWorld;
    }
}
