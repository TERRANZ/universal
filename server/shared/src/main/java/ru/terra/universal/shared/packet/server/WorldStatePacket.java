package ru.terra.universal.shared.packet.server;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.universal.shared.annoations.Packet;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.entity.PlayerInfo;
import ru.terra.universal.shared.entity.WorldEntityInfo;
import ru.terra.universal.shared.packet.AbstractPacket;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 13.01.14
 * Time: 14:57
 */

@Packet(opCode = OpCodes.Server.SMSG_WORLD_STATE)
public class WorldStatePacket extends AbstractPacket {

    private List<WorldEntityInfo> entityInfos = new ArrayList<>();
    private List<PlayerInfo> playerInfos = new ArrayList<>();

    public WorldStatePacket(List<WorldEntityInfo> entityInfos, List<PlayerInfo> playerInfos) {
        this.entityInfos = entityInfos;
        this.playerInfos = playerInfos;
    }

    @Override
    public void get(ChannelBuffer buffer) {
        Integer entityInfosSize = buffer.readInt();
        for (int ei = 0; ei < entityInfosSize; ei++) {
            WorldEntityInfo worldEntityInfo = new WorldEntityInfo();
            worldEntityInfo.readEntityInfo(buffer);
            worldEntityInfo.setModel(buffer.readInt());
            worldEntityInfo.setState(buffer.readInt());
        }
        Integer playerInfosSize = buffer.readInt();
        for (int pi = 0; pi < playerInfosSize; pi++) {
            PlayerInfo playerInfo = new PlayerInfo();
            playerInfo.readEntityInfo(buffer);
            playerInfo.readPlayerInfo(buffer);
            playerInfo.setName(readString(buffer));
            playerInfo.setWorld(readString(buffer));
        }
    }

    @Override
    public void send(ChannelBuffer buffer) {
        buffer.writeInt(entityInfos.size());
        for (WorldEntityInfo worldEntityInfo : entityInfos) {
            worldEntityInfo.writeEntityInfo(buffer);
            buffer.writeInt(worldEntityInfo.getModel());
            buffer.writeInt(worldEntityInfo.getState());
        }
        buffer.writeInt(playerInfos.size());
        for (PlayerInfo playerInfo : playerInfos) {
            playerInfo.writeEntityInfo(buffer);
            playerInfo.writePlayerInfo(buffer);
            writeString(buffer, playerInfo.getName());
            writeString(buffer, playerInfo.getWorld());
        }
    }
}
