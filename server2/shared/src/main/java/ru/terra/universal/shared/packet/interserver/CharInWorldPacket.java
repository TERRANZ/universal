package ru.terra.universal.shared.packet.interserver;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.universal.shared.annoations.Packet;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.entity.PlayerInfo;
import ru.terra.universal.shared.packet.AbstractPacket;

@Packet(opCode = OpCodes.InterServer.ISMSG_CHAR_IN_WORLD)
public class CharInWorldPacket extends AbstractPacket {
    private PlayerInfo playerInfo;

    public CharInWorldPacket() {
        super();
    }

    public CharInWorldPacket(final Long sender, final PlayerInfo playerInfo) {
        super(sender);
        this.playerInfo = playerInfo;
    }

    @Override
    public void get(ChannelBuffer buffer) {
        playerInfo = new PlayerInfo();
        playerInfo.readPlayerInfo(buffer);
    }

    @Override
    public void send(ChannelBuffer buffer) {
        playerInfo.writePlayerInfo(buffer);
    }

    public PlayerInfo getPlayerInfo() {
        return playerInfo;
    }

    public void setPlayerInfo(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
    }
}
