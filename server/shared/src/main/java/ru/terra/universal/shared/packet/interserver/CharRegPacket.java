package ru.terra.universal.shared.packet.interserver;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.universal.shared.annoations.Packet;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.packet.AbstractPacket;

@Packet(opCode = OpCodes.InterServer.ISMSG_CHAR_REG)
public class CharRegPacket extends AbstractPacket {
    private Long oldId = 0l;

    public CharRegPacket() {
    }

    @Override
    public void get(ChannelBuffer buffer) {
        oldId = buffer.readLong();
    }

    @Override
    public void send(ChannelBuffer buffer) {
        buffer.writeLong(oldId);
    }

    public Long getOldId() {
        return oldId;
    }

    public void setOldId(Long oldId) {
        this.oldId = oldId;
    }
}
