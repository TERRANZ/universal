package ru.terra.universal.server.shared.packet.interserver;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.universal.server.shared.constants.OpCodes.InterServer;
import ru.terra.universal.server.shared.packet.Packet;

public class RegisterPacket extends Packet {

    private int startRange = 0;
    private int endRange = 0;

    public RegisterPacket(long sender) {
        super(InterServer.ISMSG_REG, sender);
    }

    @Override
    public void get(ChannelBuffer buffer) {
        startRange = buffer.readInt();
        endRange = buffer.readInt();
    }

    @Override
    public void send(ChannelBuffer buffer) {
        buffer.writeInt(startRange);
        buffer.writeInt(endRange);
    }

    public int getStartRange() {
        return startRange;
    }

    public void setStartRange(int startRange) {
        this.startRange = startRange;
    }

    public int getEndRange() {
        return endRange;
    }

    public void setEndRange(int endRange) {
        this.endRange = endRange;
    }

}
