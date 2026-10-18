package ru.terra.universal.shared.packet.server;

import io.netty.buffer.ByteBuf;
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

    public CharBootPacket() {
    }

    public CharBootPacket(final Long sender, final PlayerInfo playerInfo, final List<String> worlds) {
        super(sender);
        this.playerInfo = playerInfo;
        this.worlds = worlds;
    }

    @Override
    public void get(ByteBuf buffer) {
        int count = buffer.readInt();
        for (int i = 0; i < count; i++)
            worlds.add(readString(buffer));
        playerInfo.readPlayerInfo(buffer);
    }

    @Override
    public void send(ByteBuf buffer) {
        buffer.writeInt(worlds.size());
        if (worlds.size() > 0)
            for (String w : worlds)
                writeString(buffer, w);
        playerInfo.writePlayerInfo(buffer);
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
