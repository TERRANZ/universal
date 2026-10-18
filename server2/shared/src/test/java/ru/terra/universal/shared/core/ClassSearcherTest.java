package ru.terra.universal.shared.core;

import org.junit.jupiter.api.Test;
import ru.terra.universal.shared.annoations.Packet;
import ru.terra.universal.shared.packet.AbstractPacket;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClassSearcherTest {

    @Test
    void findsEveryAnnotatedPacket() {
        final List<AbstractPacket> packets = new ClassSearcher<AbstractPacket>()
            .load("ru.terra.universal.shared.packet", Packet.class);

        assertFalse(packets.isEmpty(), "expected the packet classes to be discovered");
        assertTrue(packets.size() >= 20, "expected at least 20 packets, found " + packets.size());
        assertTrue(packets.stream().allMatch(p -> p.getOpCode() != 0),
            "every discovered packet must carry its @Packet opcode");
    }

    @Test
    void returnsEmptyForPackageWithoutAnnotatedTypes() {
        final List<AbstractPacket> packets = new ClassSearcher<AbstractPacket>()
            .load("ru.terra.universal.shared.constants", Packet.class);

        assertEquals(List.of(), packets);
    }
}
