package ru.terra.universal.shared.packet.worldserver;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.universal.shared.annoations.Packet;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.entity.PlayerInfo;

/**
 * Created by Vadim_Korostelev on 11/15/2016.
 */
@Packet(opCode = OpCodes.WorldServer.PLAYER_IN)
public class PlayerLoggedInPacket extends AbstractPlayerLogPacket {
    private PlayerInfo playerInfo;

    public PlayerLoggedInPacket(Long sender, Long guid, PlayerInfo playerInfo) {
        super(sender, guid);
        this.sender = sender;
        this.guid = guid;
        this.playerInfo = playerInfo;
    }

    @Override
    public void get(ChannelBuffer buffer) {
        super.get(buffer);
        playerInfo.readPlayerInfo(buffer);
    }

    @Override
    public void send(ChannelBuffer buffer) {
        super.send(buffer);
        playerInfo.writePlayerInfo(buffer);
    }

    public PlayerInfo getPlayerInfo() {
        return playerInfo;
    }
}
