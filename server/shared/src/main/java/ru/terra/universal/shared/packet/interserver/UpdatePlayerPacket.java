package ru.terra.universal.shared.packet.interserver;

import org.jboss.netty.buffer.ChannelBuffer;
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
    public void get(ChannelBuffer buffer) {
        playerInfo.readPlayerInfo(buffer);
    }

    @Override
    public void send(ChannelBuffer buffer) {
        playerInfo.writePlayerInfo(buffer);
    }
}
