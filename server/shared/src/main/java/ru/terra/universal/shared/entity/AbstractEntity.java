package ru.terra.universal.shared.entity;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.universal.shared.packet.AbstractPacket;

import java.util.UUID;

/**
 * Date: 13.01.14
 * Time: 14:43
 */
public abstract class AbstractEntity {
    protected Integer x = 0, y = 0, z = 0, h = 0, r = 0, map = 0;
    protected String UID = "0";

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getZ() {
        return z;
    }

    public void setZ(Integer z) {
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

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public void writeEntityInfo(ChannelBuffer buffer) {
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
        buffer.writeInt(h);
        buffer.writeInt(r);
        buffer.writeInt(map);
        AbstractPacket.writeString(buffer, UID);
    }

    public void readEntityInfo(ChannelBuffer buffer) {
        x = buffer.readInt();
        y = buffer.readInt();
        z = buffer.readInt();
        h = buffer.readInt();
        r = buffer.readInt();
        map = buffer.readInt();
        UID = AbstractPacket.readString(buffer);
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
