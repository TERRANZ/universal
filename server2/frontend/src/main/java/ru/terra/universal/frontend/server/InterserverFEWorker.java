package ru.terra.universal.frontend.server;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;
import ru.terra.universal.frontend.network.netty.ServerWorker;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.packet.AbstractPacket;
import ru.terra.universal.shared.packet.interserver.*;

public class InterserverFEWorker extends ServerWorker {

    private Logger log = Logger.getLogger(InterserverFEWorker.class);
    private ChannelsHolder channelsHolder = ChannelsHolder.getInstance();
    private WorldServersChannelsHolder worldServersChannelsHolder = WorldServersChannelsHolder.getInstance();
    private CharactersHolder charactersHolder = CharactersHolder.getInstance();
    private TempCharactersHolder tempCharactersHolder = TempCharactersHolder.getInstance();

    @Override
    public void disconnectedFromChannel(Channel removedChannel) {
        log.info("Client disconnected");
    }

    @Override
    public void acceptPacket(AbstractPacket packet) {
//        log.info("AbstractPacket accepted opcode = " + packet.getOpCode());
        if (packet.getOpCode() >= OpCodes.ISOpCodesStart) {
            switch (packet.getOpCode()) {
                case OpCodes.InterServer.ISMSG_HELLO: {
                    log.info("Interserver " + ((HelloPacket) packet).getHello() + " sent hello to us!");
                }
                break;
                case OpCodes.InterServer.ISMSG_REG: {
                    int startRange = ((RegisterPacket) packet).getStartRange();
                    int endRange = ((RegisterPacket) packet).getEndRange();
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
                    Channel charChannel = channelsHolder.getChannel(OpCodes.CharOpcodeStart);
                    Channel chatChannel = channelsHolder.getChannel(OpCodes.ChatOpcodeStart);
                    if (charChannel != null) {
                        charChannel.write(packet);
                    }
                    if (chatChannel != null) {
                        chatChannel.write(packet);
                    }
                }
                break;
                case OpCodes.InterServer.ISMSG_CHAR_REG: {
                    Long oldId = (((CharRegPacket) packet).getOldId());
                    log.info("Registering character with oldid = " + oldId + " and new id = " + packet.getSender());
                    charactersHolder.addCharChannel(packet.getSender(), tempCharactersHolder.getTempChannel(oldId));
                    tempCharactersHolder.deleteTempChannel(oldId);
                }
                break;
                case OpCodes.InterServer.ISMSG_CHAR_IN_WORLD: {
                    log.info("Char " + packet.getSender() + " in world now ");
                    Channel wsChan = worldServersChannelsHolder.getChannel(((CharInWorldPacket) packet).getPlayerInfo().getWorld());
                    if (wsChan != null)
                        wsChan.write(packet);
                }
                break;
                case OpCodes.InterServer.ISMSG_UPDATE_CHAR: {
                    log.info("Updating player info from world to char server");
                    channelsHolder.getChannel(OpCodes.CharOpcodeStart).write(packet);
                }
                break;
                case OpCodes.InterServer.ISMSG_IS_WORLD_AVAIL: {
                    String world = ((ISWorldAvaiPacket) packet).getWorld();
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
                    charactersHolder.getCharChannel(packet.getSender()).write(packet);
                }
            }
        }
    }

    @Override
    public void sendHello() {
        HelloPacket helloPacket = new HelloPacket();
        helloPacket.setHello("Hello, this is frontend!");
        channel.write(helloPacket);
    }

}
