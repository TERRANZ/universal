package ru.terra.universal.shared.packet.worldserver;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.universal.shared.annoations.Packet;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.packet.AbstractPacket;

/**
 * Created by Vadim_Korostelev on 11/15/2016.
 */
@Packet(opCode = OpCodes.WorldServer.ENTITY_DEL)
public class EntityDelPacket extends AbstractPacket {
    private Long guid;

    public EntityDelPacket() {
        super();
    }

    public EntityDelPacket(Long sender, Long guid) {
        this.sender = sender;
        this.guid = guid;
    }

    @Override
    public void get(ChannelBuffer buffer) {
        guid = buffer.readLong();
    }

    @Override
    public void send(ChannelBuffer buffer) {
        buffer.writeLong(guid);
    }

    public Long getGuid() {
        return guid;
    }

    public void setGuid(Long guid) {
        this.guid = guid;
    }
}
