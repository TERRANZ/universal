package ru.terra.universal.shared.packet.interserver;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.universal.shared.annoations.Packet;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.packet.AbstractPacket;

@Packet(opCode = OpCodes.InterServer.ISMSG_REG_WORLD)
public class RegWorldPacket extends AbstractPacket {

    private String worldUid = "";

    public RegWorldPacket() {
    }

    @Override
    public void get(ChannelBuffer buffer) {
        worldUid = readString(buffer);
    }

    @Override
    public void send(ChannelBuffer buffer) {
        writeString(buffer, worldUid);
    }

    public String getWorldUid() {
        return worldUid;
    }

    public void setWorldUid(String worldUid) {
        this.worldUid = worldUid;
    }
}
