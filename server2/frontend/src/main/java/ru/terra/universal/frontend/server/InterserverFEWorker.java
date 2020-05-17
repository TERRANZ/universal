package ru.terra.universal.frontend.server;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;
import ru.terra.universal.frontend.network.netty.ServerWorker;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.packet.AbstractPacket;
import ru.terra.universal.shared.packet.interserver.CharInWorldPacket;
import ru.terra.universal.shared.packet.interserver.CharRegPacket;
import ru.terra.universal.shared.packet.interserver.HelloPacket;
import ru.terra.universal.shared.packet.interserver.ISWorldAvaiPacket;
import ru.terra.universal.shared.packet.interserver.RegisterPacket;

public class InterserverFEWorker extends ServerWorker {

    private final Logger log = Logger.getLogger(InterserverFEWorker.class);
    private final ChannelsHolder channelsHolder = ChannelsHolder.getInstance();
    private final WorldServersChannelsHolder worldServersChannelsHolder = WorldServersChannelsHolder.getInstance();
    private final CharactersHolder charactersHolder = CharactersHolder.getInstance();
    private final TempCharactersHolder tempCharactersHolder = TempCharactersHolder.getInstance();

    @Override
    public void disconnectedFromChannel(final Channel removedChannel) {
        log.info("Client disconnected");
    }

    @Override
    public void acceptPacket(final AbstractPacket packet) {
        if (packet.getOpCode() >= OpCodes.ISOpCodesStart) {
            switch (packet.getOpCode()) {
                case OpCodes.InterServer.ISMSG_HELLO: {
                    log.info("Interserver " + ((HelloPacket) packet).getHello() + " sent hello to us!");
                }
                break;
                case OpCodes.InterServer.ISMSG_REG: {
                    final int startRange = ((RegisterPacket) packet).getStartRange();
                    final int endRange = ((RegisterPacket) packet).getEndRange();
                    log.info("Registering interserver for range " + startRange + " to " + endRange);
                    if (startRange == OpCodes.WorldOpcodeStart) {
                        for (int i = startRange; i < endRange; i++) {
                            worldServersChannelsHolder.addChannel(((RegisterPacket) packet).getWorld(), i, channel);
                        }
                    } else {
                        for (int i = startRange; i < endRange; i++) {
                            channelsHolder.addChannel(i, channel);
                        }
                    }
                }
                break;
                case OpCodes.InterServer.ISMSG_UNREG: {
                }
                break;
                case OpCodes.InterServer.ISMSG_BOOT_CHAR: {
                    log.info("Booting char with UID = " + packet.getSender());
                    final Channel charChannel = channelsHolder.getChannel(OpCodes.CharOpcodeStart);
                    final Channel chatChannel = channelsHolder.getChannel(OpCodes.ChatOpcodeStart);
                    if (charChannel != null) {
                        charChannel.write(packet);
                    }
                    if (chatChannel != null) {
                        chatChannel.write(packet);
                    }
                }
                break;
                case OpCodes.InterServer.ISMSG_CHAR_REG: {
                    final Long oldId = (((CharRegPacket) packet).getOldId());
                    log.info("Registering character with oldid = " + oldId + " and new id = " + packet.getSender());
                    charactersHolder.addCharChannel(packet.getSender(), tempCharactersHolder.getTempChannel(oldId));
                    tempCharactersHolder.deleteTempChannel(oldId);
                }
                break;
                case OpCodes.InterServer.ISMSG_CHAR_IN_WORLD: {
                    log.info("Char " + packet.getSender() + " in world now ");
                    final Channel wsChan = worldServersChannelsHolder
                        .getChannel(((CharInWorldPacket) packet).getPlayerInfo().getWorld());
                    if (wsChan != null) {
                        wsChan.write(packet);
                    }
                }
                break;
                case OpCodes.InterServer.ISMSG_UPDATE_CHAR: {
                    log.info("Updating player info from world to char server");
                    channelsHolder.getChannel(OpCodes.CharOpcodeStart).write(packet);
                }
                break;
                case OpCodes.InterServer.ISMSG_IS_WORLD_AVAIL: {
                    final String world = ((ISWorldAvaiPacket) packet).getWorld();
                    log.info("Char server checking is world " + world + " available");
                    ((ISWorldAvaiPacket) packet).setAvail(worldServersChannelsHolder.isWorldAvailable(world));
                    channelsHolder.getChannel(OpCodes.CharOpcodeStart).write(packet);
                }
                break;
            }
        } else {
            if (packet.getOpCode().equals(OpCodes.Server.Login.SMSG_LOGIN_FAILED)) {
                tempCharactersHolder.getTempChannel(packet.getSender()).write(packet);
            } else {
                if (packet.getSender().equals(0L)) {
                    //log.info("Sending packet " + packet.getOpCode() + " to all players");
                    charactersHolder.getChannels().forEach(chan -> charactersHolder.getCharChannel(chan).write(packet));
                } else {
                    try {
                        charactersHolder.getCharChannel(packet.getSender()).write(packet);
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    @Override
    public void sendHello() {
        final HelloPacket helloPacket = new HelloPacket();
        helloPacket.setHello("Hello, this is frontend!");
        channel.write(helloPacket);
    }

}
