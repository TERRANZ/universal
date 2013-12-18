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

    @Override
    public void disconnectedFromChannel(Channel removedChannel) {
        logger.info("Player disconnected");
        Long removedChar = charactersHolder.removeChar(removedChannel);
        if (removedChar != null) {
            Channel charChannel = channelsHolder.getChannel(OpCodes.CharOpcodeStart);
            Channel chatChannel = channelsHolder.getChannel(OpCodes.ChatOpcodeStart);
            Channel loginChannel = channelsHolder.getChannel(OpCodes.LoginOpcodeStart);
            Channel worldChannel = channelsHolder.getChannel(OpCodes.WorldOpcodeStart);
            UnregCharPacket unregCharPacket = new UnregCharPacket();
            unregCharPacket.setSender(removedChar);
            if (charChannel != null)
                charChannel.write(unregCharPacket);
            if (chatChannel != null)
                chatChannel.write(unregCharPacket);
            if (loginChannel != null)
                loginChannel.write(unregCharPacket);
            if (worldChannel != null)
                worldChannel.write(unregCharPacket);
        }
    }

    @Override
    public void acceptPacket(AbstractPacket message) {
        Channel interchan = channelsHolder.getChannel(message.getOpCode());
        if (interchan != null)
            interchan.write(message);
        else {
            logger.error("Unable to find interserver for opcode " + message.getOpCode());
        }
    }

    @Override
    synchronized public void sendHello() {
        logger.info("Player connected");
        OkPacket okPacket = new OkPacket();
        okPacket.setSender(tempCharactersHolder.size());
        logger.info("Temporary player uid = " + okPacket.getSender());
        tempCharactersHolder.addTempChannel(okPacket.getSender(), channel);
        channel.write(okPacket);
    }
}
