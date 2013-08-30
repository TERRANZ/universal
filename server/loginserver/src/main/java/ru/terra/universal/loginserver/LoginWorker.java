package ru.terra.universal.loginserver;

import org.apache.log4j.Logger;
import ru.terra.universal.interserver.network.NetworkManager;
import ru.terra.universal.interserver.network.netty.InterserverWorker;
import ru.terra.universal.server.shared.constants.OpCodes;
import ru.terra.universal.server.shared.constants.OpCodes.InterServer;
import ru.terra.universal.server.shared.packet.Packet;
import ru.terra.universal.server.shared.packet.interserver.HelloPacket;
import ru.terra.universal.server.shared.packet.interserver.RegisterPacket;

public class LoginWorker extends InterserverWorker {

    private Logger log = Logger.getLogger(this.getClass());

    @Override
    public void disconnectedFromChannel() {
        log.info("Frontend disconnected us");
    }

    @Override
    public void acceptPacket(Packet packet) {
        //log.info("packet accepted " + packet.getOpCode());
        if (packet.getOpCode() == InterServer.ISMSG_HELLO) {
            HelloPacket helloPacket = new HelloPacket(0);
            helloPacket.setHello("login server");
            RegisterPacket registerPacket = new RegisterPacket(0);
            registerPacket.setStartRange(OpCodes.LoginOpcodeStart);
            registerPacket.setEndRange(OpCodes.LoginOpcodeEnd);
            NetworkManager.getInstance().getChannel().write(helloPacket);
            NetworkManager.getInstance().getChannel().write(registerPacket);
        }
    }

}