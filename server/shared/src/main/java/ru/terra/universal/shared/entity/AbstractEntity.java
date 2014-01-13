package ru.terra.universal.shared.entity;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * Date: 13.01.14
 * Time: 14:43
 */
public abstract class AbstractEntity {
    protected Integer x = 0, y = 0, z = 0, h = 0, r = 0, map = 0;

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

    public void writeEntityInfo(ChannelBuffer buffer) {
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
        buffer.writeInt(h);
        buffer.writeInt(r);
        buffer.writeInt(map);
    }

    public void readEntityInfo(ChannelBuffer buffer) {
        x = buffer.readInt();
        y = buffer.readInt();
        z = buffer.readInt();
        h = buffer.readInt();
        r = buffer.readInt();
        map = buffer.readInt();
    }
}
