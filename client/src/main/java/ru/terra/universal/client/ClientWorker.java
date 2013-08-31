package ru.terra.universal.client;

import org.apache.log4j.Logger;
import ru.terra.universal.interserver.network.netty.InterserverWorker;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.packet.AbstractPacket;
import ru.terra.universal.shared.packet.client.LoginPacket;

/**
 * User: Vadim Korostelev
 * Date: 30.08.13
 * Time: 16:15
 */
public class ClientWorker extends InterserverWorker {
    private Logger logger = Logger.getLogger(this.getClass());

    @Override
    public void disconnectedFromChannel() {
    }

    @Override
    public void acceptPacket(AbstractPacket packet) {
        logger.info("Packet accepted");
        switch (packet.getOpCode()) {
            case OpCodes.Server.SMSG_OK: {
                getChannel().write(new LoginPacket(packet.getSender()));
            }
            break;
        }
    }
}
