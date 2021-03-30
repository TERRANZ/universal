package ru.terra.universal.charserver;

import org.apache.log4j.Logger;
import ru.terra.universal.interserver.network.netty.InterserverWorker;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.constants.OpCodes.InterServer;
import ru.terra.universal.shared.packet.AbstractPacket;
import ru.terra.universal.shared.packet.client.SelectServerPacket;
import ru.terra.universal.shared.packet.interserver.HelloPacket;
import ru.terra.universal.shared.packet.interserver.ISWorldAvaiPacket;
import ru.terra.universal.shared.packet.interserver.RegisterPacket;

public class CharWorker extends InterserverWorker {

    private final Logger log = Logger.getLogger(this.getClass());
    private final CharHandler charHandler = new CharHandler(networkManager);

    @Override
    public void disconnectedFromChannel() {
        log.info("Frontend disconnected us");
    }

    @Override
    public void acceptPacket(final AbstractPacket packet) {
        final Long sender = packet.getSender();
        switch (packet.getOpCode()) {
            case InterServer.ISMSG_HELLO: {
                final HelloPacket helloPacket = new HelloPacket();
                helloPacket.setHello("char server");
                final RegisterPacket registerPacket = new RegisterPacket();
                registerPacket.setStartRange(OpCodes.CharOpcodeStart);
                registerPacket.setEndRange(OpCodes.CharOpcodeEnd);
                networkManager.sendPacket(helloPacket);
                networkManager.sendPacket(registerPacket);
            }
            break;
            case InterServer.ISMSG_BOOT_CHAR: {
                log.info("Registering character with uid = " + sender);
                charHandler.handleBootChar(sender);
            }
            break;
            case OpCodes.Client.Char.CMSG_SELECT_SERVER: {
                log.info("Character " + sender + " selected server!");
                networkManager.sendPacket(new ISWorldAvaiPacket(sender, ((SelectServerPacket) packet).getTargetWorld()));
            }
            break;
            case InterServer.ISMSG_UNREG_CHAR: {
                log.info("Unregistering char with uid = " + sender);
                charHandler.handleUnregChar(sender);
            }
            break;
            case InterServer.ISMSG_UPDATE_CHAR: {
                charHandler.handleUpdateChar(packet);
            }
            break;
            case InterServer.ISMSG_IS_WORLD_AVAIL: {
                charHandler.handleIsWorldAvail(sender, packet);
            }
            break;
        }
    }

}
