package ru.terra.universal.interserver.network.netty;

import org.jboss.netty.channel.Channel;
import ru.terra.universal.interserver.network.NetworkManager;
import ru.terra.universal.shared.packet.AbstractPacket;

public abstract class InterserverWorker {

    private InterserverHandler clientHandler;
    private Channel channel;
    protected NetworkManager networkManager = NetworkManager.getInstance();

    public InterserverWorker(InterserverHandler clientHandler, Channel channel) {
        this.clientHandler = clientHandler;
        this.channel = channel;
    }

    public InterserverWorker() {
    }

    public InterserverHandler getClientHandler() {
        return clientHandler;
    }

    public void setClientHandler(InterserverHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public abstract void disconnectedFromChannel();

    public abstract void acceptPacket(AbstractPacket packet);
}
