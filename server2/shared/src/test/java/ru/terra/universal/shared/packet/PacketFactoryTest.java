package ru.terra.universal.shared.packet;

import org.junit.jupiter.api.Test;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.packet.client.BootMePacket;
import ru.terra.universal.shared.packet.client.LoginPacket;
import ru.terra.universal.shared.packet.client.LogoutPacket;
import ru.terra.universal.shared.packet.client.SelectServerPacket;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;

class PacketFactoryTest {

    @Test
    void resolvesClientPackets() {
        assertInstanceOf(LoginPacket.class, packet(OpCodes.Client.Login.CMSG_LOGIN));
        assertInstanceOf(LogoutPacket.class, packet(OpCodes.Client.Login.CMSG_LOGOUT));
        assertInstanceOf(BootMePacket.class, packet(OpCodes.Client.Login.CSMG_BOOT_ME));
        assertInstanceOf(SelectServerPacket.class, packet(OpCodes.Client.Char.CMSG_SELECT_SERVER));
    }

    @Test
    void resolvesServerPackets() {
        assertInstanceOf(OkPacket.class, packet(OpCodes.Server.SMSG_OK));
        assertInstanceOf(CharBootPacket.class, packet(OpCodes.Server.SMSG_CHAR_BOOT));
        assertInstanceOf(UpdatePacket.class, packet(OpCodes.Server.SMSG_UPDATE));
        assertInstanceOf(WorldStatePacket.class, packet(OpCodes.Server.SMSG_WORLD_STATE));
        assertInstanceOf(LoginFailedPacket.class, packet(OpCodes.Server.Login.SMSG_LOGIN_FAILED));
        assertInstanceOf(WorldIsNotAvail.class, packet(OpCodes.Server.SMSG_WORLD_NOT_AVAIL));
    }

    @Test
    void resolvesWorldPackets() {
        assertInstanceOf(PlayerLoggedInPacket.class, packet(OpCodes.WorldServer.PLAYER_IN));
        assertInstanceOf(PlayerLoggedOutPacket.class, packet(OpCodes.WorldServer.PLAYER_OUT));
        assertInstanceOf(EntityAddPacket.class, packet(OpCodes.WorldServer.ENTITY_ADD));
        assertInstanceOf(EntityDelPacket.class, packet(OpCodes.WorldServer.ENTITY_DEL));
        assertInstanceOf(MovementPacket.class, packet(OpCodes.WorldServer.Movement.MSG_MOVE));
        assertInstanceOf(PlayerTeleportPacket.class, packet(OpCodes.WorldServer.Movement.MSG_MOVE_TELEPORT));
    }

    @Test
    void resolvesInterserverPackets() {
        assertInstanceOf(HelloPacket.class, packet(OpCodes.InterServer.ISMSG_HELLO));
        assertInstanceOf(RegisterPacket.class, packet(OpCodes.InterServer.ISMSG_REG));
        assertInstanceOf(BootCharPacket.class, packet(OpCodes.InterServer.ISMSG_BOOT_CHAR));
        assertInstanceOf(CharRegPacket.class, packet(OpCodes.InterServer.ISMSG_CHAR_REG));
        assertInstanceOf(CharInWorldPacket.class, packet(OpCodes.InterServer.ISMSG_CHAR_IN_WORLD));
        assertInstanceOf(RegWorldPacket.class, packet(OpCodes.InterServer.ISMSG_REG_WORLD));
        assertInstanceOf(UnregCharPacket.class, packet(OpCodes.InterServer.ISMSG_UNREG_CHAR));
        assertInstanceOf(UpdatePlayerPacket.class, packet(OpCodes.InterServer.ISMSG_UPDATE_CHAR));
        assertInstanceOf(ISWorldAvaiPacket.class, packet(OpCodes.InterServer.ISMSG_IS_WORLD_AVAIL));
    }

    @Test
    void appliesSenderAndReturnsFreshInstance() {
        final AbstractPacket first = PacketFactory.getInstance().getPacket(OpCodes.Client.Login.CMSG_LOGIN, 123L);
        final AbstractPacket second = PacketFactory.getInstance().getPacket(OpCodes.Client.Login.CMSG_LOGIN, 456L);
        assertEquals(123L, first.getSender());
        assertEquals(456L, second.getSender());
        assertNotSame(first, second);
    }

    @Test
    void unknownOpcodeReturnsNull() {
        assertNull(PacketFactory.getInstance().getPacket(4242, 1L));
    }

    private static AbstractPacket packet(int opCode) {
        return PacketFactory.getInstance().getPacket(opCode, 1L);
    }
}
