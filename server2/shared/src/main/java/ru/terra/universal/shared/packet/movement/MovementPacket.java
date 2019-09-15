package ru.terra.universal.shared.packet.movement;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.universal.shared.annoations.Packet;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.packet.AbstractPacket;

/**
 * Date: 13.01.14
 * Time: 13:45
 */
@Packet(opCode = OpCodes.WorldServer.Movement.MSG_MOVE)
public class MovementPacket extends AbstractPacket {

    private Float x, y, z;
    private Integer h;
    private Integer direction;
    private Long movableId;

    public MovementPacket() {
        super();
    }

    public MovementPacket(Long movableId, Integer direction, Float x, Float y, Float z, Integer h) {
        super();
        this.movableId = movableId;
        this.direction = direction;
        this.x = x;
        this.y = y;
        this.z = z;
        this.h = h;
    }

    public MovementPacket(Long uid, MovementPacket packet) {
        super();
        setSender(uid);
        this.movableId = packet.getMovableId();
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

    public Long getMovableId() {
        return movableId;
    }

    public void setMovableId(Long movableId) {
        this.movableId = movableId;
    }

    @Override
    public void get(ChannelBuffer buffer) {
        movableId = buffer.readLong();
        direction = buffer.readInt();
        x = buffer.readFloat();
        y = buffer.readFloat();
        z = buffer.readFloat();
        h = buffer.readInt();
    }

    @Override
    public void send(ChannelBuffer buffer) {
        buffer.writeLong(movableId);
        buffer.writeInt(direction);
        buffer.writeFloat(x);
        buffer.writeFloat(y);
        buffer.writeFloat(z);
        buffer.writeInt(h);
    }
}
