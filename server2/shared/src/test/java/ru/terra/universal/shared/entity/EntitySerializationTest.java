package ru.terra.universal.shared.entity;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EntitySerializationTest {

    @Test
    void abstractEntityRoundTrip() {
        final AbstractEntity entity = new AbstractEntity();
        entity.setX(1.25f);
        entity.setY(2.5f);
        entity.setZ(3.75f);
        entity.setH(90);
        entity.setR(1);
        entity.setMap(7);
        entity.setUID(42L);

        final ByteBuf buffer = Unpooled.buffer();
        entity.writeEntityInfo(buffer);

        final AbstractEntity copy = new AbstractEntity();
        copy.readEntityInfo(buffer);

        assertEquals(0, buffer.readableBytes());
        assertEquals(1.25f, copy.getX().floatValue(), 0.0001f);
        assertEquals(2.5f, copy.getY().floatValue(), 0.0001f);
        assertEquals(3.75f, copy.getZ().floatValue(), 0.0001f);
        assertEquals(90, copy.getH().intValue());
        assertEquals(1, copy.getR().intValue());
        assertEquals(7, copy.getMap().intValue());
        assertEquals(42L, copy.getUID().longValue());
    }

    @Test
    void playerInfoRoundTrip() {
        final PlayerInfo player = new PlayerInfo("Hero", "newScene");
        player.setX(1.0f);
        player.setY(2.0f);
        player.setZ(3.0f);
        player.setH(45);
        player.setR(2);
        player.setMap(1);
        player.setUID(7L);
        player.setHp(100);
        player.setMp(60);
        player.setXp(12);
        player.setLevel(9);

        final ByteBuf buffer = Unpooled.buffer();
        player.writePlayerInfo(buffer);

        final PlayerInfo copy = new PlayerInfo();
        copy.readPlayerInfo(buffer);

        assertEquals(0, buffer.readableBytes());
        assertEquals("Hero", copy.getName());
        assertEquals("newScene", copy.getWorld());
        assertEquals(100, copy.getHp().intValue());
        assertEquals(60, copy.getMp().intValue());
        assertEquals(12, copy.getXp().intValue());
        assertEquals(9, copy.getLevel().intValue());
        assertEquals(7L, copy.getUID().longValue());
        assertEquals(1.0f, copy.getX().floatValue(), 0.0001f);
    }

    @Test
    void playerInfoDefaults() {
        final PlayerInfo player = new PlayerInfo();
        assertEquals(0, player.getHp().intValue());
        assertEquals(0, player.getMp().intValue());
        assertEquals(0, player.getXp().intValue());
        assertEquals(1, player.getLevel().intValue());
        assertEquals("", player.getName());
        assertEquals("", player.getWorld());
    }
}
