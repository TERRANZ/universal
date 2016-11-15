package ru.terra.universal.shared.packet.interserver;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.universal.shared.annoations.Packet;
import ru.terra.universal.shared.constants.OpCodes.InterServer;
import ru.terra.universal.shared.packet.AbstractPacket;

@Packet(opCode = InterServer.ISMSG_REG)
public class RegisterPacket extends AbstractPacket {

    private int startRange = 0;
    private int endRange = 0;

    public RegisterPacket() {
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
