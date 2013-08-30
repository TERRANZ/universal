package ru.terra.universal.frontend.network.netty;

import org.jboss.netty.channel.Channel;
import ru.terra.universal.server.shared.packet.Packet;


public abstract class ServerWorker {
    protected Channel channel;
    protected ServerHandler playerHandler;

    public ServerWorker() {
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public ServerHandler getPlayerHandler() {
        return playerHandler;
    }

    public void setPlayerHandler(ServerHandler playerHandler) {
        this.playerHandler = playerHandler;
    }

    public abstract void disconnectedFromChannel();

    public abstract void acceptPacket(Packet message);

    public abstract void sendHello();
}
