package ru.terra.universal.chatserver;

import org.apache.log4j.Logger;
import ru.terra.universal.interserver.network.netty.InterserverWorker;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.constants.OpCodes.InterServer;
import ru.terra.universal.shared.packet.AbstractPacket;
import ru.terra.universal.shared.packet.client.ChatSayPacket;
import ru.terra.universal.shared.packet.interserver.HelloPacket;
import ru.terra.universal.shared.packet.interserver.RegisterPacket;

public class ChatWorker extends InterserverWorker {

    private Logger log = Logger.getLogger(this.getClass());

    @Override
    public void disconnectedFromChannel() {
        log.info("Frontend disconnected us");
    }

    @Override
    public void acceptPacket(AbstractPacket packet) {
//        log.info("Received opcode "+packet.getOpCode());
        switch (packet.getOpCode()) {
            case InterServer.ISMSG_HELLO: {
                HelloPacket helloPacket = new HelloPacket();
                helloPacket.setHello("chat server");
                RegisterPacket registerPacket = new RegisterPacket();
                registerPacket.setStartRange(OpCodes.ChatOpcodeStart);
                registerPacket.setEndRange(OpCodes.ChatOpcodeEnd);
                networkManager.sendPacket(helloPacket);
                networkManager.sendPacket(registerPacket);
            }
            break;
            case InterServer.ISMSG_BOOT_CHAR: {
                log.info("Registering char with uid = " + packet.getSender());
            }
            break;
            case InterServer.ISMSG_UNREG_CHAR: {
                log.info("Unregistering char with uid = " + packet.getSender());
            }
            break;
            case OpCodes.ChatServer.Chat.CMSG_SAY: {
                ChatSayPacket chatSayPacket = (ChatSayPacket) packet;
                log.info("Player " + packet.getSender() + " says: " + chatSayPacket.getMessage() + " to: " + chatSayPacket.getTo());
                chatSayPacket.setSender(chatSayPacket.getTo());
                networkManager.sendPacket(chatSayPacket);
            }
            break;
            case OpCodes.ChatServer.Chat.CMSG_WISP: {
            }
            break;
        }
    }

}
