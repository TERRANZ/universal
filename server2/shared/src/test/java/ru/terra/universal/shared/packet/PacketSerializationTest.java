package ru.terra.universal.shared.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.entity.AbstractEntity;
import ru.terra.universal.shared.entity.PlayerInfo;
import ru.terra.universal.shared.packet.client.BootMePacket;
import ru.terra.universal.shared.packet.client.LoginPacket;
import ru.terra.universal.shared.packet.client.LogoutPacket;
import ru.terra.universal.shared.packet.client.SelectServerPacket;
import ru.terra.universal.shared.packet.client.chat.ChatSayPacket;
import ru.terra.universal.shared.packet.client.chat.WispSayPacket;
import ru.terra.universal.shared.packet.interserver.BootCharPacket;
import ru.terra.universal.shared.packet.interserver.CharInWorldPacket;
import ru.terra.universal.shared.packet.interserver.CharRegPacket;
import ru.terra.universal.shared.packet.interserver.HelloPacket;
import ru.terra.universal.shared.packet.interserver.ISWorldAvaiPacket;
import ru.terra.universal.shared.packet.interserver.RegWorldPacket;
import ru.terra.universal.shared.packet.interserver.RegisterPacket;
import ru.terra.universal.shared.packet.interserver.UnregCharPacket;
import ru.terra.universal.shared.packet.interserver.UpdatePlayerPacket;
import ru.terra.universal.shared.packet.movement.MovementPacket;
import ru.terra.universal.shared.packet.movement.PlayerTeleportPacket;
import ru.terra.universal.shared.packet.server.CharBootPacket;
import ru.terra.universal.shared.packet.server.LoginFailedPacket;
import ru.terra.universal.shared.packet.server.OkPacket;
import ru.terra.universal.shared.packet.server.UpdatePacket;
import ru.terra.universal.shared.packet.server.WorldIsNotAvail;
import ru.terra.universal.shared.packet.server.WorldStatePacket;
import ru.terra.universal.shared.packet.worldserver.EntityAddPacket;
import ru.terra.universal.shared.packet.worldserver.EntityDelPacket;
import ru.terra.universal.shared.packet.worldserver.PlayerLoggedInPacket;
import ru.terra.universal.shared.packet.worldserver.PlayerLoggedOutPacket;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Round-trips each packet type through its own {@code send}/{@code get} payload
 * codec and asserts that all fields survive and that the whole payload is
 * consumed (a leftover byte would desynchronise the stream).
 */
class PacketSerializationTest {

    // --- client packets ---------------------------------------------------

    @Test
    void loginPacket() {
        final LoginPacket packet = new LoginPacket();
        packet.setLogin("hero");
        packet.setPassword("s3cret");

        final LoginPacket copy = roundTrip(packet, LoginPacket.class);
        assertEquals("hero", copy.getLogin());
        assertEquals("s3cret", copy.getPassword());
    }

    @Test
    void emptyPayloadClientPackets() {
        assertEquals(0L, roundTrip(new BootMePacket(), BootMePacket.class).getSender());
        assertEquals(0L, roundTrip(new LogoutPacket(), LogoutPacket.class).getSender());
    }

    @Test
    void selectServerPacket() {
        final SelectServerPacket packet = new SelectServerPacket();
        packet.setTargetWorld("newScene");

        assertEquals("newScene", roundTrip(packet, SelectServerPacket.class).getTargetWorld());
    }

    @Test
    void chatSayPacket() {
        final ChatSayPacket packet = new ChatSayPacket("hello world", 77L);

        final ChatSayPacket copy = roundTrip(packet, ChatSayPacket.class);
        assertEquals("hello world", copy.getMessage());
        assertEquals(77L, copy.getTo());
    }

    @Test
    void wispSayPacket() {
        final WispSayPacket packet = new WispSayPacket("psst", 88L);

        final WispSayPacket copy = roundTrip(packet, WispSayPacket.class);
        assertEquals("psst", copy.getMessage());
        assertEquals(88L, copy.getTo());
    }

    // --- movement ---------------------------------------------------------

    @Test
    void movementPacket() {
        final MovementPacket packet = new MovementPacket(42L, 3, 1.5f, 2.5f, 3.5f, 90);

        final MovementPacket copy = roundTrip(packet, MovementPacket.class);
        assertEquals(42L, copy.getMovableId().longValue());
        assertEquals(3, copy.getDirection().intValue());
        assertEquals(1.5f, copy.getX().floatValue(), 0.0001f);
        assertEquals(2.5f, copy.getY().floatValue(), 0.0001f);
        assertEquals(3.5f, copy.getZ().floatValue(), 0.0001f);
        assertEquals(90, copy.getH().intValue());
    }

    @Test
    void playerTeleportPacketReusesMovementPayload() {
        final PlayerTeleportPacket packet = new PlayerTeleportPacket(9L, new MovementPacket(42L, 1, 1f, 2f, 3f, 4));

        final PlayerTeleportPacket copy = roundTrip(packet, PlayerTeleportPacket.class);
        assertEquals(9L, copy.getSender());
        assertEquals(42L, copy.getMovableId().longValue());
        assertEquals(OpCodes.WorldServer.Movement.MSG_MOVE_TELEPORT, copy.getOpCode());
    }

    // --- server -> client -------------------------------------------------

    @Test
    void okPacket() {
        final OkPacket packet = new OkPacket(123L);
        assertEquals(123L, roundTrip(packet, OkPacket.class).getSender());
    }

    @Test
    void charBootPacket() {
        final PlayerInfo playerInfo = player(7L, "Hero", "newScene");
        final CharBootPacket packet = new CharBootPacket(1L, playerInfo, List.of("newScene", "otherScene"));

        final CharBootPacket copy = roundTrip(packet, CharBootPacket.class);
        assertEquals(1L, copy.getSender());
        assertEquals(List.of("newScene", "otherScene"), copy.getWorlds());
        assertEquals("Hero", copy.getPlayerInfo().getName());
        assertEquals(7L, copy.getPlayerInfo().getUID().longValue());
        assertEquals(100, copy.getPlayerInfo().getHp().intValue());
    }

    @Test
    void updatePacket() {
        final UpdatePacket packet = new UpdatePacket(2, "75");

        final UpdatePacket copy = roundTrip(packet, UpdatePacket.class);
        assertEquals(2, copy.getField());
        assertEquals("75", copy.getValue());
    }

    @Test
    void loginFailedPacket() {
        final LoginFailedPacket packet = new LoginFailedPacket();
        packet.setReason("bad credentials");

        assertEquals("bad credentials", roundTrip(packet, LoginFailedPacket.class).getReason());
    }

    @Test
    void worldIsNotAvailPacket() {
        final WorldIsNotAvail packet = new WorldIsNotAvail(5L, "newScene");

        final WorldIsNotAvail copy = roundTrip(packet, WorldIsNotAvail.class);
        assertEquals(5L, copy.getSender());
        assertEquals("newScene", copy.getWorld());
    }

    @Test
    void worldStatePacket() {
        final WorldStatePacket packet = new WorldStatePacket(
            List.of(entity(1L), entity(2L)),
            List.of(player(3L, "Alice", "newScene"), player(4L, "Bob", "newScene")));

        final WorldStatePacket copy = roundTrip(packet, WorldStatePacket.class);
        assertEquals(2, copy.getEntityInfos().size());
        assertEquals(2, copy.getPlayerInfos().size());
        assertEquals(1L, copy.getEntityInfos().get(0).getUID().longValue());
        assertEquals(5, copy.getEntityInfos().get(0).getMap().intValue());
        assertEquals("Alice", copy.getPlayerInfos().get(0).getName());
        assertEquals("Bob", copy.getPlayerInfos().get(1).getName());
    }

    // --- world server -----------------------------------------------------

    @Test
    void playerLoggedInPacket() {
        final PlayerInfo playerInfo = player(3L, "Alice", "newScene");
        final PlayerLoggedInPacket packet = new PlayerLoggedInPacket(99L, 3L, playerInfo);

        final PlayerLoggedInPacket copy = roundTrip(packet, PlayerLoggedInPacket.class);
        assertEquals(99L, copy.getSender());
        assertEquals(3L, copy.getGuid().longValue());
        assertEquals("Alice", copy.getPlayerInfo().getName());
    }

    @Test
    void playerLoggedOutPacket() {
        final PlayerLoggedOutPacket packet = new PlayerLoggedOutPacket(99L, 3L);

        final PlayerLoggedOutPacket copy = roundTrip(packet, PlayerLoggedOutPacket.class);
        assertEquals(99L, copy.getSender());
        assertEquals(3L, copy.getGuid().longValue());
    }

    @Test
    void entityDelPacket() {
        final EntityDelPacket packet = new EntityDelPacket(99L, 55L);

        final EntityDelPacket copy = roundTrip(packet, EntityDelPacket.class);
        assertEquals(99L, copy.getSender());
        assertEquals(55L, copy.getGuid().longValue());
    }

    @Test
    void entityAddPacket() {
        final EntityAddPacket packet = new EntityAddPacket(1L, entity(55L));

        final ByteBuf buffer = Unpooled.buffer();
        packet.send(buffer);

        final EntityAddPacket copy = new EntityAddPacket();
        copy.setEntity(new AbstractEntity());
        copy.get(buffer);

        assertEquals(0, buffer.readableBytes());
        assertEquals(55L, copy.getEntity().getUID().longValue());
        assertEquals(1.25f, copy.getEntity().getX().floatValue(), 0.0001f);
    }

    // --- interserver ------------------------------------------------------

    @Test
    void helloPacket() {
        final HelloPacket packet = new HelloPacket();
        packet.setHello("world server");

        assertEquals("world server", roundTrip(packet, HelloPacket.class).getHello());
    }

    @Test
    void registerPacket() {
        final RegisterPacket packet = new RegisterPacket();
        packet.setStartRange(OpCodes.WorldOpcodeStart);
        packet.setEndRange(OpCodes.WorldOpcodeEnd);
        packet.setWorld("newScene");

        final RegisterPacket copy = roundTrip(packet, RegisterPacket.class);
        assertEquals(OpCodes.WorldOpcodeStart, copy.getStartRange());
        assertEquals(OpCodes.WorldOpcodeEnd, copy.getEndRange());
        assertEquals("newScene", copy.getWorld());
    }

    @Test
    void regWorldPacket() {
        final RegWorldPacket packet = new RegWorldPacket();
        packet.setWorldUid("newScene");

        assertEquals("newScene", roundTrip(packet, RegWorldPacket.class).getWorldUid());
    }

    @Test
    void emptyPayloadInterserverPackets() {
        assertEquals(0L, roundTrip(new BootCharPacket(), BootCharPacket.class).getSender());
        assertEquals(0L, roundTrip(new UnregCharPacket(), UnregCharPacket.class).getSender());
    }

    @Test
    void charRegPacket() {
        final CharRegPacket packet = new CharRegPacket(5L);
        packet.setOldId(42L);

        final CharRegPacket copy = roundTrip(packet, CharRegPacket.class);
        assertEquals(5L, copy.getSender());
        assertEquals(42L, copy.getOldId().longValue());
    }

    @Test
    void charInWorldPacket() {
        final CharInWorldPacket packet = new CharInWorldPacket(5L, player(5L, "Hero", "newScene"));

        final CharInWorldPacket copy = roundTrip(packet, CharInWorldPacket.class);
        assertEquals(5L, copy.getSender());
        assertEquals("Hero", copy.getPlayerInfo().getName());
        assertEquals("newScene", copy.getPlayerInfo().getWorld());
    }

    @Test
    void updatePlayerPacket() {
        final UpdatePlayerPacket packet = new UpdatePlayerPacket(player(5L, "Hero", "newScene"));

        final UpdatePlayerPacket copy = roundTrip(packet, UpdatePlayerPacket.class);
        assertEquals("Hero", copy.getPlayerInfo().getName());
        assertEquals(5L, copy.getPlayerInfo().getUID().longValue());
    }

    @Test
    void isWorldAvailPacket() {
        final ISWorldAvaiPacket available = new ISWorldAvaiPacket(5L, "newScene");
        available.setAvail(true);
        final ISWorldAvaiPacket availableCopy = roundTrip(available, ISWorldAvaiPacket.class);
        assertEquals(5L, availableCopy.getSender());
        assertEquals("newScene", availableCopy.getWorld());
        assertTrue(availableCopy.getAvail());

        final ISWorldAvaiPacket unavailable = new ISWorldAvaiPacket(6L, "otherScene");
        unavailable.setAvail(false);
        assertFalse(roundTrip(unavailable, ISWorldAvaiPacket.class).getAvail());
    }

    // --- helpers ----------------------------------------------------------

    private static <T extends AbstractPacket> T roundTrip(T packet, Class<T> type) {
        final ByteBuf buffer = Unpooled.buffer();
        packet.send(buffer);

        final T copy = newInstance(type);
        copy.setSender(packet.getSender());
        if (copy instanceof EntityAddPacket) {
            ((EntityAddPacket) copy).setEntity(new AbstractEntity());
        }
        copy.get(buffer);

        assertEquals(0, buffer.readableBytes(),
            type.getSimpleName() + " left " + buffer.readableBytes() + " unread byte(s) in the buffer");
        return copy;
    }

    private static <T> T newInstance(Class<T> type) {
        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Cannot instantiate " + type.getName(), e);
        }
    }

    static AbstractEntity entity(long uid) {
        final AbstractEntity entity = new AbstractEntity();
        entity.setX(1.25f);
        entity.setY(2.5f);
        entity.setZ(3.75f);
        entity.setH(90);
        entity.setR(1);
        entity.setMap(5);
        entity.setUID(uid);
        return entity;
    }

    static PlayerInfo player(long uid, String name, String world) {
        final PlayerInfo player = new PlayerInfo(name, world);
        player.setX(1.0f);
        player.setY(2.0f);
        player.setZ(3.0f);
        player.setH(45);
        player.setR(2);
        player.setMap(1);
        player.setUID(uid);
        player.setHp(100);
        player.setMp(60);
        player.setXp(12);
        player.setLevel(9);
        return player;
    }
}
