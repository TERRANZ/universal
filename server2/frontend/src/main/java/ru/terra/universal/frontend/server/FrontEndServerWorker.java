package ru.terra.universal.frontend.server;

import java.nio.channels.ClosedChannelException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.netty.channel.Channel;
import ru.terra.universal.frontend.network.netty.ServerWorker;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.packet.AbstractPacket;
import ru.terra.universal.shared.packet.interserver.UnregCharPacket;
import ru.terra.universal.shared.packet.server.OkPacket;

public class FrontEndServerWorker extends ServerWorker {

    private final Logger logger = LogManager.getLogger(this.getClass());
    private final TempCharactersHolder tempCharactersHolder = TempCharactersHolder.getInstance();
    private final CharactersHolder charactersHolder = CharactersHolder.getInstance();
    private final ChannelsHolder channelsHolder = ChannelsHolder.getInstance();
    private final WorldServersChannelsHolder worldServersChannelsHolder = WorldServersChannelsHolder.getInstance();

    @Override
    public void disconnectedFromChannel(final Channel removedChannel) {
        logger.info("Player disconnected");
        final Long removedChar = charactersHolder.removeChar(removedChannel);
        if (removedChar != null) {
            final Channel charChannel = channelsHolder.getChannel(OpCodes.CharOpcodeStart);
            final Channel chatChannel = channelsHolder.getChannel(OpCodes.ChatOpcodeStart);
            final Channel loginChannel = channelsHolder.getChannel(OpCodes.LoginOpcodeStart);
            final UnregCharPacket unregCharPacket = new UnregCharPacket(removedChar);
            if (charChannel != null) {
                charChannel.writeAndFlush(unregCharPacket);
            }
            if (chatChannel != null) {
                chatChannel.writeAndFlush(unregCharPacket);
            }
            if (loginChannel != null) {
                loginChannel.writeAndFlush(unregCharPacket);
            }
            worldServersChannelsHolder.getChannels().values().forEach(wch -> wch.values().forEach(wc -> {
                try {
                    wc.writeAndFlush(unregCharPacket);
                } catch (Exception e) {
//                    logger.error("Channel already closed");
                }
            }));
        }
    }

    @Override
    public void acceptPacket(final AbstractPacket message) {
        if (message.getOpCode() >= OpCodes.WorldOpcodeStart && message.getOpCode() <= OpCodes.WorldOpcodeEnd) {
            if (worldServersChannelsHolder.getChannels() != null
                && worldServersChannelsHolder.getChannels().size() > 0) {
                worldServersChannelsHolder.getChannels().values()
                    .forEach(wch -> wch.get(message.getOpCode()).writeAndFlush(message));
            }
        } else {
            final Channel c = channelsHolder.getChannel(message.getOpCode());
            if (c != null) {
                c.writeAndFlush(message);
            } else {
                logger.error("Unable to find interserver for opcode " + message.getOpCode());
            }
        }
    }

    @Override
    synchronized public void sendHello() {
        logger.info("Player connected");
        final OkPacket okPacket = new OkPacket(tempCharactersHolder.getUnusedId());
        logger.info("Temporary player uid = " + okPacket.getSender() + " with channel " + channel.id());
        tempCharactersHolder.addTempChannel(okPacket.getSender(), channel);
        channel.writeAndFlush(okPacket);
    }
}
