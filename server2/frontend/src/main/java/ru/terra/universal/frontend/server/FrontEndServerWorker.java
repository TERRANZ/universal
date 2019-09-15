package ru.terra.universal.frontend.server;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;
import ru.terra.universal.frontend.network.netty.ServerWorker;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.packet.AbstractPacket;
import ru.terra.universal.shared.packet.interserver.UnregCharPacket;
import ru.terra.universal.shared.packet.server.OkPacket;

public class FrontEndServerWorker extends ServerWorker {

    private Logger logger = Logger.getLogger(this.getClass());
    private TempCharactersHolder tempCharactersHolder = TempCharactersHolder.getInstance();
    private CharactersHolder charactersHolder = CharactersHolder.getInstance();
    private ChannelsHolder channelsHolder = ChannelsHolder.getInstance();
    private WorldServersChannelsHolder worldServersChannelsHolder = WorldServersChannelsHolder.getInstance();

    @Override
    public void disconnectedFromChannel(Channel removedChannel) {
        logger.info("Player disconnected");
        Long removedChar = charactersHolder.removeChar(removedChannel);
        if (removedChar != null) {
            Channel charChannel = channelsHolder.getChannel(OpCodes.CharOpcodeStart);
            Channel chatChannel = channelsHolder.getChannel(OpCodes.ChatOpcodeStart);
            Channel loginChannel = channelsHolder.getChannel(OpCodes.LoginOpcodeStart);
            UnregCharPacket unregCharPacket = new UnregCharPacket();
            unregCharPacket.setSender(removedChar);
            if (charChannel != null)
                charChannel.write(unregCharPacket);
            if (chatChannel != null)
                chatChannel.write(unregCharPacket);
            if (loginChannel != null)
                loginChannel.write(unregCharPacket);
            worldServersChannelsHolder.getChannels().values().forEach(wch -> wch.values().forEach(wc -> wc.write(unregCharPacket)));
        }
    }

    @Override
    public void acceptPacket(AbstractPacket message) {
//        logger.info("Received packet " + message.getOpCode());
        if (message.getOpCode() >= OpCodes.WorldOpcodeStart && message.getOpCode() <= OpCodes.WorldOpcodeEnd) {
            if (worldServersChannelsHolder.getChannels() != null && worldServersChannelsHolder.getChannels().size() > 0)
                worldServersChannelsHolder.getChannels().values().forEach(wch -> wch.get(message.getOpCode()).write(message));
        } else {
            Channel c = channelsHolder.getChannel(message.getOpCode());
            if (c != null)
                c.write(message);
            else {
                logger.error("Unable to find interserver for opcode " + message.getOpCode());
            }
        }
    }

    @Override
    synchronized public void sendHello() {
        logger.info("Player connected");
        OkPacket okPacket = new OkPacket();
        okPacket.setSender(tempCharactersHolder.getUnusedId());
        logger.info("Temporary player uid = " + okPacket.getSender() + " with channel " + channel.getId());
        tempCharactersHolder.addTempChannel(okPacket.getSender(), channel);
        channel.write(okPacket);
    }
}
