package ru.terra.universal.shared.packet.interserver;

import io.netty.buffer.ByteBuf;
import ru.terra.universal.shared.annoations.Packet;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.entity.PlayerInfo;
import ru.terra.universal.shared.packet.AbstractPacket;

/**
 * Date: 13.01.14
 * Time: 16:38
 */
@Packet(opCode = OpCodes.InterServer.ISMSG_UPDATE_CHAR)
public class UpdatePlayerPacket extends AbstractPacket {
    private PlayerInfo playerInfo;

    public UpdatePlayerPacket() {
        super();
        playerInfo = new PlayerInfo();
    }

    public UpdatePlayerPacket(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
    }

    public PlayerInfo getPlayerInfo() {
        return playerInfo;
    }

    public void setPlayerInfo(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
    }

    @Override
    public void get(ByteBuf buffer) {
        playerInfo.readPlayerInfo(buffer);
    }

    @Override
    public void send(ByteBuf buffer) {
        if (playerInfo == null)
            System.out.println("Player info is null");
        else
            playerInfo.writePlayerInfo(buffer);
    }
}
