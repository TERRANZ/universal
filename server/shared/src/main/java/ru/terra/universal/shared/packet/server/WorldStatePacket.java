package ru.terra.universal.shared.packet.server;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.universal.shared.annoations.Packet;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.entity.AbstractEntity;
import ru.terra.universal.shared.entity.PlayerInfo;
import ru.terra.universal.shared.packet.AbstractPacket;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 13.01.14
 * Time: 14:57
 */

@Packet(opCode = OpCodes.Server.SMSG_WORLD_STATE)
public class WorldStatePacket extends AbstractPacket {

    private List<AbstractEntity> entityInfos = new ArrayList<>();
    private List<PlayerInfo> playerInfos = new ArrayList<>();

    public WorldStatePacket() {
    }

    public WorldStatePacket(List<AbstractEntity> entityInfos, List<PlayerInfo> playerInfos) {
        this.entityInfos = entityInfos;
        this.playerInfos = playerInfos;
    }

    @Override
    public void get(ChannelBuffer buffer) {
        Integer entityInfosSize = buffer.readInt();
        for (int ei = 0; ei < entityInfosSize; ei++) {
            AbstractEntity worldEntityInfo = new AbstractEntity();
            worldEntityInfo.readEntityInfo(buffer);
            entityInfos.add(worldEntityInfo);
        }
        Integer playerInfosSize = buffer.readInt();
        for (int pi = 0; pi < playerInfosSize; pi++) {
            PlayerInfo playerInfo = new PlayerInfo();
            playerInfo.readPlayerInfo(buffer);
            playerInfos.add(playerInfo);
        }
    }

    @Override
    public void send(ChannelBuffer buffer) {
        buffer.writeInt(entityInfos.size());
        for (AbstractEntity worldEntityInfo : entityInfos) {
            worldEntityInfo.writeEntityInfo(buffer);
        }
        buffer.writeInt(playerInfos.size());
        for (PlayerInfo playerInfo : playerInfos) {
            playerInfo.writePlayerInfo(buffer);
        }
    }

    public List<AbstractEntity> getEntityInfos() {
        return entityInfos;
    }

    public List<PlayerInfo> getPlayerInfos() {
        return playerInfos;
    }
}
