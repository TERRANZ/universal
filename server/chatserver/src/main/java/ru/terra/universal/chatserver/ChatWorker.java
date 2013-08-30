package ru.terra.universal.chatserver;

import org.apache.log4j.Logger;

import ru.terra.universal.interserver.network.NetworkManager;
import ru.terra.universal.interserver.network.netty.InterserverWorker;
import ru.terra.universal.server.shared.constants.OpCodes;
import ru.terra.universal.server.shared.constants.OpCodes.InterServer;
import ru.terra.universal.server.shared.packet.Packet;
import ru.terra.universal.server.shared.packet.interserver.HelloPacket;
import ru.terra.universal.server.shared.packet.interserver.RegisterPacket;

public class ChatWorker extends InterserverWorker {

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
	    helloPacket.setHello("chat server");
	    RegisterPacket registerPacket = new RegisterPacket(0);
	    registerPacket.setStartRange(OpCodes.ChatOpcodeStart);
	    registerPacket.setEndRange(OpCodes.ChatOpcodeEnd);
	    NetworkManager.getInstance().getChannel().write(helloPacket);
	    NetworkManager.getInstance().getChannel().write(registerPacket);
	}
    }

}
