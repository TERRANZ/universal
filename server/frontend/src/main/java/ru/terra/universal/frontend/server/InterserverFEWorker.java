package ru.terra.universal.frontend.server;

import org.apache.log4j.Logger;
import ru.terra.universal.frontend.network.netty.ServerWorker;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.packet.AbstractPacket;
import ru.terra.universal.shared.packet.interserver.HelloPacket;
import ru.terra.universal.shared.packet.interserver.RegisterPacket;

public class InterserverFEWorker extends ServerWorker {

    private Logger log = Logger.getLogger(InterserverFEWorker.class);

    @Override
    public void disconnectedFromChannel() {
        log.info("Client disconnected");
    }

    @Override
    public void acceptPacket(AbstractPacket message) {
        //log.info("AbstractPacket accepted");
        if (message.getOpCode() >= OpCodes.ISOpCodesStart) {
            switch (message.getOpCode()) {
                case OpCodes.InterServer.ISMSG_HELLO: {
                    log.info("Interserver " + ((HelloPacket) message).getHello() + " sent hello to us!");
                }
                break;
                case OpCodes.InterServer.ISMSG_REG: {
                    int startRange = ((RegisterPacket) message).getStartRange();
                    int endRange = ((RegisterPacket) message).getEndRange();
                    log.info("Registering interserver for range " + startRange + " to " + endRange);
                    for (int i = startRange; i < endRange; i++) {
                        ChannelsHolder.getInstance().addChannel(i, channel);
                    }
                }
                break;
                case OpCodes.InterServer.ISMSG_UNREG: {
                }
                break;
            }
        } else {
            switch (message.getOpCode()) {
            }
        }
    }

    @Override
    public void sendHello() {
        HelloPacket helloPacket = new HelloPacket(0);
        helloPacket.setHello("Hello, this is frontend!");
        channel.write(helloPacket);
    }

}
