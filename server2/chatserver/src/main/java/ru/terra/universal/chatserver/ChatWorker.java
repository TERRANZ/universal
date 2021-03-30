package ru.terra.universal.chatserver;

import org.apache.log4j.Logger;
import ru.terra.universal.interserver.network.netty.InterserverWorker;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.constants.OpCodes.InterServer;
import ru.terra.universal.shared.packet.AbstractPacket;
import ru.terra.universal.shared.packet.interserver.HelloPacket;
import ru.terra.universal.shared.packet.interserver.RegisterPacket;

public class ChatWorker extends InterserverWorker {

    private final Logger log = Logger.getLogger(this.getClass());
    private final ChatHandler chatHandler = new ChatHandler(networkManager);

    @Override
    public void disconnectedFromChannel() {
        log.info("Frontend disconnected us");
    }

    @Override
    public void acceptPacket(AbstractPacket packet) {
        final Long sender = packet.getSender();
        switch (packet.getOpCode()) {
            case InterServer.ISMSG_HELLO: {
                final HelloPacket helloPacket = new HelloPacket();
                helloPacket.setHello("chat server");
                final RegisterPacket registerPacket = new RegisterPacket();
                registerPacket.setStartRange(OpCodes.ChatOpcodeStart);
                registerPacket.setEndRange(OpCodes.ChatOpcodeEnd);
                networkManager.sendPacket(helloPacket);
                networkManager.sendPacket(registerPacket);
            }
            break;
            case InterServer.ISMSG_BOOT_CHAR: {
                log.info("Registering char with uid = " + sender);
                chatHandler.handleBootChar(sender);
            }
            break;
            case InterServer.ISMSG_UNREG_CHAR: {
                log.info("Unregistering char with uid = " + sender);
                chatHandler.handlerUnregChar(sender);
            }
            break;
            case OpCodes.ChatServer.Chat.CMSG_SAY: {
                chatHandler.handleSay(sender, packet);
            }
            break;
            case OpCodes.ChatServer.Chat.CMSG_WISP: {
                chatHandler.handleWisp(sender, packet);
            }
            break;
        }
    }

}
