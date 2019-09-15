package ru.terra.universal.shared.packet.server;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.universal.shared.annoations.Packet;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.packet.AbstractPacket;

/**
 * Date: 13.01.14
 * Time: 13:51
 */
@Packet(opCode = OpCodes.Server.SMSG_UPDATE)
public class UpdatePacket extends AbstractPacket {
    private int field = 0;
    private String value = "";

    public UpdatePacket() {
        super();
    }

    public UpdatePacket(int field, String value) {
        this.field = field;
        this.value = value;
    }

    public int getField() {
        return field;
    }

    public void setField(int field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void get(ChannelBuffer buffer) {
        field = buffer.readInt();
        value = readString(buffer);
    }

    @Override
    public void send(ChannelBuffer buffer) {
        buffer.writeInt(field);
        writeString(buffer, value);
    }
}
