package ru.terra.universal.shared.packet.server;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.universal.shared.annoations.Packet;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.packet.AbstractPacket;

/**
 * Created by terranz on 02.07.17.
 */
@Packet(opCode = OpCodes.Server.SMSG_WORLD_NOT_AVAIL)
public class WorldIsNotAvail extends AbstractPacket {
    private String world;

    public WorldIsNotAvail() {
    }

    public WorldIsNotAvail(final Long sender, final String world) {
        super(sender);
        this.world = world;
    }

    @Override
    public void get(ChannelBuffer buffer) {
        world = readString(buffer);
    }

    @Override
    public void send(ChannelBuffer buffer) {
        writeString(buffer, world);
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }
}
