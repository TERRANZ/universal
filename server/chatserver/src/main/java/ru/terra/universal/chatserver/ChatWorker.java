package ru.terra.universal.chatserver;

import org.apache.log4j.Logger;
import ru.terra.universal.interserver.network.NetworkManager;
import ru.terra.universal.interserver.network.netty.InterserverWorker;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.constants.OpCodes.InterServer;
import ru.terra.universal.shared.packet.AbstractPacket;
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
        switch (packet.getOpCode()) {
            case InterServer.ISMSG_HELLO: {
                HelloPacket helloPacket = new HelloPacket();
                helloPacket.setHello("chat server");
                RegisterPacket registerPacket = new RegisterPacket();
                registerPacket.setStartRange(OpCodes.ChatOpcodeStart);
                registerPacket.setEndRange(OpCodes.ChatOpcodeEnd);
                NetworkManager.getInstance().getChannel().write(helloPacket);
                NetworkManager.getInstance().getChannel().write(registerPacket);
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

        }
    }

}
