package ru.terra.universal.frontend.server;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;
import ru.terra.universal.frontend.network.netty.ServerWorker;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.packet.AbstractPacket;
import ru.terra.universal.shared.packet.interserver.CharRegPacket;
import ru.terra.universal.shared.packet.interserver.HelloPacket;
import ru.terra.universal.shared.packet.interserver.RegisterPacket;

public class InterserverFEWorker extends ServerWorker {

    private Logger log = Logger.getLogger(InterserverFEWorker.class);
    private ChannelsHolder channelsHolder = ChannelsHolder.getInstance();
    private CharactersHolder charactersHolder = CharactersHolder.getInstance();
    private TempCharactersHolder tempCharactersHolder = TempCharactersHolder.getInstance();

    @Override
    public void disconnectedFromChannel(Channel removedChannel) {
        log.info("Client disconnected");
    }

    @Override
    public void acceptPacket(AbstractPacket packet) {
        log.info("AbstractPacket accepted opcode = " + packet.getOpCode());
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
                    for (int i = startRange; i < endRange; i++) {
                        channelsHolder.addChannel(i, channel);
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
                    channelsHolder.getChannel(OpCodes.WorldOpcodeStart).write(packet);
                }
                break;
            }
        } else {
            switch (packet.getOpCode()) {
                case OpCodes.Server.SMSG_CHAR_BOOT: {
                    log.info("Char " + packet.getSender() + " is booting now");
                    Channel channel = charactersHolder.getCharChannel(packet.getSender());
                    if (channel != null)
                        channel.write(packet);
                    else
                        log.error("Char channel is null!");
                }
                break;
                case OpCodes.Server.SMSG_OK: {
                    charactersHolder.getCharChannel(packet.getSender()).write(packet);
                }
                break;
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
