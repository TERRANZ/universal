package ru.terra.universal.shared.packet.server;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.universal.shared.annoations.Packet;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.entity.PlayerInfo;
import ru.terra.universal.shared.packet.AbstractPacket;

import java.util.ArrayList;
import java.util.List;

@Packet(opCode = OpCodes.Server.SMSG_CHAR_BOOT)
public class CharBootPacket extends AbstractPacket {

    private PlayerInfo playerInfo = new PlayerInfo();
    private List<String> worlds = new ArrayList<>();

    @Override
    public void get(ChannelBuffer buffer) {
        String pi = readString(buffer);
        int count = buffer.readInt();
        for (int i = 0; i < count; i++)
            worlds.add(readString(buffer));
        playerInfo = new JSONDeserializer<PlayerInfo>().deserialize(pi);
    }

    @Override
    public void send(ChannelBuffer buffer) {
        String pi = new JSONSerializer().deepSerialize(playerInfo);
        writeString(buffer, pi);
        buffer.writeInt(worlds.size());
        if (worlds.size() > 0)
            for (String w : worlds)
                writeString(buffer, w);
    }

    public PlayerInfo getPlayerInfo() {
        return playerInfo;
    }

    public void setPlayerInfo(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
    }

    public List<String> getWorlds() {
        return worlds;
    }

    public void setWorlds(List<String> worlds) {
        this.worlds = worlds;
    }
}
