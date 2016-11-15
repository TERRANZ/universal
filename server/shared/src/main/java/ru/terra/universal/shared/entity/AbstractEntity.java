package ru.terra.universal.shared.entity;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.universal.shared.packet.AbstractPacket;

/**
 * Date: 13.01.14
 * Time: 14:43
 */
public class AbstractEntity {
    protected Float x = 0f, y = 0f, z = 0f;
    protected Integer h = 0, r = 0, map = 0;
    protected Long UID = 0L;
    protected String model = "";
    protected Integer state = 0;

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

    public Integer getR() {
        return r;
    }

    public void setR(Integer r) {
        this.r = r;
    }

    public Integer getMap() {
        return map;
    }

    public void setMap(Integer map) {
        this.map = map;
    }

    public Long getUID() {
        return UID;
    }

    public void setUID(Long UID) {
        this.UID = UID;
    }

    public void writeEntityInfo(ChannelBuffer buffer) {
        buffer.writeFloat(x);
        buffer.writeFloat(y);
        buffer.writeFloat(z);
        buffer.writeInt(h);
        buffer.writeInt(r);
        AbstractPacket.writeString(buffer, model);
        buffer.writeInt(state);
        buffer.writeInt(map);
        buffer.writeLong(UID);
    }

    public void readEntityInfo(ChannelBuffer buffer) {
        x = buffer.readFloat();
        y = buffer.readFloat();
        z = buffer.readFloat();
        h = buffer.readInt();
        r = buffer.readInt();
        model = AbstractPacket.readString(buffer);
        state = buffer.readInt();
        map = buffer.readInt();
        UID = buffer.readLong();
    }

    @Override
    public String toString() {
        return "AbstractEntity{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", h=" + h +
                ", r=" + r +
                ", map=" + map +
                ", UID='" + UID + '\'' +
                '}';
    }
}
