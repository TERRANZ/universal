package ru.terra.universal.shared.packet.client;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.universal.shared.annoations.Packet;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.packet.AbstractPacket;

/**
 * Date: 13.01.14
 * Time: 13:45
 */
@Packet(opCode = OpCodes.Client.Movement.CMSG_MOVE)
public class MovementPacket extends AbstractPacket {

    private Long x, y, z, h;

    public MovementPacket() {
    }

    public MovementPacket(Long x, Long y, Long z, Long h) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.h = h;
    }

    public Long getX() {
        return x;
    }

    public void setX(Long x) {
        this.x = x;
    }

    public Long getY() {
        return y;
    }

    public void setY(Long y) {
        this.y = y;
    }

    public Long getZ() {
        return z;
    }

    public void setZ(Long z) {
        this.z = z;
    }

    public Long getH() {
        return h;
    }

    public void setH(Long h) {
        this.h = h;
    }

    @Override
    public void get(ChannelBuffer buffer) {
        x = buffer.readLong();
        y = buffer.readLong();
        z = buffer.readLong();
        h = buffer.readLong();
    }

    @Override
    public void send(ChannelBuffer buffer) {
        buffer.writeLong(x);
        buffer.writeLong(y);
        buffer.writeLong(z);
        buffer.writeLong(h);

    }
}
