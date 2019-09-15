package ru.terra.universal.shared.packet.interserver;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.universal.shared.annoations.Packet;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.packet.AbstractPacket;

/**
 * Created by terranz on 02.07.17.
 */
@Packet(opCode = OpCodes.InterServer.ISMSG_IS_WORLD_AVAIL)
public class ISWorldAvaiPacket extends AbstractPacket {
    private String world;
    private boolean avail;

    public ISWorldAvaiPacket() {
        super();
    }

    public ISWorldAvaiPacket(String world) {
        super();
        this.world = world;
    }

    @Override
    public void get(ChannelBuffer buffer) {
        world = readString(buffer);
        avail = buffer.readByte() == 1;
    }

    @Override
    public void send(ChannelBuffer buffer) {
        writeString(buffer, world);
        buffer.writeByte(avail ? 1 : 0);
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public Boolean getAvail() {
        return avail;
    }

    public void setAvail(Boolean avail) {
        this.avail = avail;
    }
}
