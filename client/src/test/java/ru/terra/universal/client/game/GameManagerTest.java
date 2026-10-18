package ru.terra.universal.client.game;

import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.terra.universal.interserver.network.NetworkManager;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.entity.PlayerInfo;
import ru.terra.universal.shared.packet.movement.MovementPacket;
import ru.terra.universal.shared.packet.movement.PlayerTeleportPacket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class GameManagerTest {

    private static final long UID = 12345L;

    private EmbeddedChannel channel;

    @BeforeEach
    void setUp() {
        channel = new EmbeddedChannel();
        NetworkManager.getInstance().setChannel(channel);

        final PlayerInfo player = new PlayerInfo("hero", "map_0");
        player.setUID(UID);
        GameManager.getInstance().setPlayerInfo(player);
    }

    @Test
    void sendsAMovePacketForTheLocalPlayer() {
        final int direction = OpCodes.WorldServer.Movement.DIRECTION.MOVE_FORWARD.ordinal();

        GameManager.getInstance().sendPlayerMove(direction, 1.5f, 2.5f, 3.5f, 45);

        final MovementPacket packet = channel.readOutbound();
        assertNotNull(packet);
        assertEquals(OpCodes.WorldServer.Movement.MSG_MOVE, packet.getOpCode());
        assertEquals(UID, packet.getSender());
        assertEquals(UID, packet.getMovableId());
        assertEquals(direction, packet.getDirection());
        assertEquals(1.5f, packet.getX(), 0.0001f);
        assertEquals(2.5f, packet.getY(), 0.0001f);
        assertEquals(3.5f, packet.getZ(), 0.0001f);
        assertEquals(45, packet.getH());
        assertNull(channel.readOutbound(), "a move must produce exactly one packet");
    }

    @Test
    void sendsATeleportPacketWithoutDirection() {
        GameManager.getInstance().sendPlayerTeleport(4f, 5f, 6f, 90);

        final PlayerTeleportPacket packet = channel.readOutbound();
        assertNotNull(packet);
        assertEquals(OpCodes.WorldServer.Movement.MSG_MOVE_TELEPORT, packet.getOpCode());
        assertEquals(UID, packet.getSender());
        assertEquals(UID, packet.getMovableId());
        assertEquals(OpCodes.WorldServer.Movement.DIRECTION.NO_MOVE.ordinal(), packet.getDirection());
        assertEquals(4f, packet.getX(), 0.0001f);
        assertEquals(5f, packet.getY(), 0.0001f);
        assertEquals(6f, packet.getZ(), 0.0001f);
        assertEquals(90, packet.getH());
        assertNull(channel.readOutbound(), "a teleport must produce exactly one packet");
    }
}
