package ru.terra.universal.shared.packet.client;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.universal.shared.annoations.Packet;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.packet.AbstractPacket;

/**
 * Date: 13.01.14
 * Time: 13:45
 */
@Packet(opCode = OpCodes.Movement.MSG_MOVE)
public class MovementPacket extends AbstractPacket {

    private Float x, y, z;
    private Integer h;
    private Integer direction;

    public MovementPacket() {
    }

    public MovementPacket(Integer direction, Float x, Float y, Float z, Integer h) {
        this.direction = direction;
        this.x = x;
        this.y = y;
        this.z = z;
        this.h = h;
    }

    public MovementPacket(Long uid, MovementPacket packet) {
        setSender(uid);
        this.direction = packet.getDirection();
        this.x = packet.getX();
        this.y = packet.getY();
        this.z = packet.getZ();
        this.h = packet.getH();
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public Float getZ() {
        return z;
    }

    public void setZ(Float z) {
        this.z = z;
    }

    public Integer getH() {
        return h;
    }

    public void setH(Integer h) {
        this.h = h;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    @Override
    public void get(ChannelBuffer buffer) {
        direction = buffer.readInt();
        x = buffer.readFloat();
        y = buffer.readFloat();
        z = buffer.readFloat();
        h = buffer.readInt();
    }

    @Override
    public void send(ChannelBuffer buffer) {
        buffer.writeInt(direction);
        buffer.writeFloat(x);
        buffer.writeFloat(y);
        buffer.writeFloat(z);
        buffer.writeInt(h);
    }

    @Override
    public String toString() {
        return "MovementPacket{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", h=" + h +
                ", direction=" + direction +
                '}';
    }
}
