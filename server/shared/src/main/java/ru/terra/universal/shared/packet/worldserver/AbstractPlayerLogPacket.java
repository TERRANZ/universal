package ru.terra.universal.shared.packet.worldserver;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.universal.shared.packet.AbstractPacket;

/**
 * Created by Vadim_Korostelev on 11/15/2016.
 */
public abstract class AbstractPlayerLogPacket extends AbstractPacket {
    Long guid;

    public AbstractPlayerLogPacket() {
    }

    public AbstractPlayerLogPacket(Long sender, Long guid) {
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
