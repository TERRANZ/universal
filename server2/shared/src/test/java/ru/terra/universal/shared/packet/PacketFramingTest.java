package ru.terra.universal.shared.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.packet.movement.MovementPacket;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Covers the wire framing produced by {@link AbstractPacket#write} and consumed
 * by {@link AbstractPacket#read}: a 2-byte opcode, an 8-byte sender GUID and the
 * packet specific payload.
 */
class PacketFramingTest {

    @Test
    void writeThenReadRestoresOpcodeSenderAndPayload() throws IOException {
        final MovementPacket packet = new MovementPacket(42L, 3, 1.5f, 2.5f, 3.5f, 90);
        packet.setSender(7L);

        final ByteBuf buffer = Unpooled.buffer();
        AbstractPacket.write(packet, buffer);

        // framing is readable without touching the reader index
        assertEquals(OpCodes.WorldServer.Movement.MSG_MOVE, buffer.getUnsignedShort(0));
        assertEquals(7L, buffer.getLong(2));

        final AbstractPacket decoded = assertInstanceOf(MovementPacket.class, AbstractPacket.read(buffer));
        assertEquals(7L, decoded.getSender());
        assertEquals(OpCodes.WorldServer.Movement.MSG_MOVE, decoded.getOpCode());

        final MovementPacket movement = (MovementPacket) decoded;
        assertEquals(42L, movement.getMovableId());
        assertEquals(3, movement.getDirection());
        assertEquals(1.5f, movement.getX());
        assertEquals(2.5f, movement.getY());
        assertEquals(3.5f, movement.getZ());
        assertEquals(90, movement.getH());
        assertEquals(0, buffer.readableBytes(), "framing consumed exactly one packet");
    }

    @Test
    void readRejectsUnknownOpcode() {
        final ByteBuf buffer = Unpooled.buffer();
        buffer.writeShort(9999);
        buffer.writeLong(1L);
        assertThrows(IOException.class, () -> AbstractPacket.read(buffer));
    }

    @Test
    void stringHelperRoundTrips() {
        final ByteBuf buffer = Unpooled.buffer();
        AbstractPacket.writeString(buffer, "hello");
        assertEquals("hello", AbstractPacket.readString(buffer));
        assertEquals(0, buffer.readableBytes());
    }

    @Test
    void stringHelperEncodesLengthAsShortPrefix() {
        final ByteBuf buffer = Unpooled.buffer();
        AbstractPacket.writeString(buffer, "abc");
        assertEquals(3, buffer.getShort(0));
        assertEquals(2 + 3 * 2, buffer.readableBytes());
    }

    /**
     * Documents the current behaviour that empty strings are not written at all
     * (see the "Known issues" section of the README). A reader that expects a
     * length prefix would desynchronise, so callers must not rely on empty
     * strings crossing the wire.
     */
    @Test
    void writeStringSkipsEmptyStrings() {
        final ByteBuf buffer = Unpooled.buffer();
        AbstractPacket.writeString(buffer, "");
        assertEquals(0, buffer.readableBytes());
    }
}
