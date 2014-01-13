package ru.terra.universal.loginserver;

import org.apache.log4j.Logger;
import ru.terra.universal.interserver.network.NetworkManager;
import ru.terra.universal.interserver.network.netty.InterserverWorker;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.constants.OpCodes.InterServer;
import ru.terra.universal.shared.packet.AbstractPacket;
import ru.terra.universal.shared.packet.client.LoginPacket;
import ru.terra.universal.shared.packet.interserver.BootCharPacket;
import ru.terra.universal.shared.packet.interserver.CharRegPacket;
import ru.terra.universal.shared.packet.interserver.HelloPacket;
import ru.terra.universal.shared.packet.interserver.RegisterPacket;
import ru.terra.universal.shared.packet.server.OkPacket;

import java.util.UUID;

public class LoginWorker extends InterserverWorker {

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
                helloPacket.setHello("login server");
                RegisterPacket registerPacket = new RegisterPacket();
                registerPacket.setStartRange(OpCodes.LoginOpcodeStart);
                registerPacket.setEndRange(OpCodes.LoginOpcodeEnd);
                networkManager.sendPacket(helloPacket);
                networkManager.sendPacket(registerPacket);
            }
            break;
            case OpCodes.Client.Login.CMSG_LOGIN: {
                LoginPacket loginPacket = (LoginPacket) packet;
                log.info("Client with login " + loginPacket.getLogin() + " and pass " + loginPacket.getPassword() + " attempting to log in");
                log.info("Client with id " + loginPacket.getSender() + " logged in");
                long uid = UUID.randomUUID().getMostSignificantBits();
                log.info("Client registered with GUID = " + uid);
                OkPacket okPacket = new OkPacket();
                okPacket.setSender(uid);
                CharRegPacket charRegPacket = new CharRegPacket();
                charRegPacket.setSender(uid);
                charRegPacket.setOldId(loginPacket.getSender());
                networkManager.sendPacket(charRegPacket);
                networkManager.sendPacket(okPacket);
            }
            break;
            case OpCodes.Client.Login.CSMG_BOOT_ME: {
                log.info("Client sent Boot Me to us");
                BootCharPacket bootCharPacket = new BootCharPacket();
                bootCharPacket.setSender(packet.getSender());
                networkManager.sendPacket(bootCharPacket);
            }
            break;
            case InterServer.ISMSG_UNREG_CHAR: {
                log.info("Unregistering char with uid = " + packet.getSender());
            }
            break;
        }
    }

}
