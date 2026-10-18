package ru.terra.universal.client.network;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class GUIDHOlderTest {

    @Test
    void startsWithANonZeroGuid() {
        assertNotEquals(0L, new GUIDHOlder().getGuid());
    }

    @Test
    void storesTheLatestGuid() {
        final GUIDHOlder holder = new GUIDHOlder();

        holder.setGuid(42L);

        assertEquals(42L, holder.getGuid());
    }

    @Test
    void separateInstancesDoNotShareState() {
        final GUIDHOlder first = new GUIDHOlder();
        final GUIDHOlder second = new GUIDHOlder();

        first.setGuid(1L);
        second.setGuid(2L);

        assertEquals(1L, first.getGuid());
        assertEquals(2L, second.getGuid());
    }

    @Test
    void exposesASingleton() {
        assertSame(GUIDHOlder.getInstance(), GUIDHOlder.getInstance());
    }
}
