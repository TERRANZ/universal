package ru.terra.universal.frontend.server;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;
import ru.terra.universal.frontend.network.netty.ServerWorker;
import ru.terra.universal.shared.packet.AbstractPacket;
import ru.terra.universal.shared.packet.server.OkPacket;

public class FrontEndServerWorker extends ServerWorker {

    private Logger logger = Logger.getLogger(this.getClass());

    @Override
    public void disconnectedFromChannel() {
    }

    @Override
    public void acceptPacket(AbstractPacket message) {
        Channel interchan = ChannelsHolder.getInstance().getChannel(message.getOpCode());
        if (interchan != null)
            interchan.write(message);
        else {
            logger.error("Unable to find interserver for opcode " + message.getOpCode());
        }
    }

    @Override
    public void sendHello() {
        channel.write(new OkPacket());
    }
}
