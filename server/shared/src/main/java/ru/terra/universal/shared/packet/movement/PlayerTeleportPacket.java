package ru.terra.universal.shared.packet.movement;

import ru.terra.universal.shared.annoations.Packet;
import ru.terra.universal.shared.constants.OpCodes;

/**
 * Date: 15.11.16
 * Time: 13:45
 */
@Packet(opCode = OpCodes.WorldServer.Movement.MSG_MOVE_TELEPORT)
public class PlayerTeleportPacket extends MovementPacket {
    public PlayerTeleportPacket() {
    }

    public PlayerTeleportPacket(Long uid, MovementPacket packet) {
        super(uid, packet);
    }
}