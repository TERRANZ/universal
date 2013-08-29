package ru.terra.universal.interserver.network;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;

import ru.terra.universal.interserver.network.netty.InterserverWorker;
import ru.terra.universal.server.shared.packet.Packet;

public class NetworkManager {
    private static NetworkManager instance = new NetworkManager();
    private Logger log = Logger.getLogger(NetworkManager.class);

    private NetworkManager() {
    }

    public static NetworkManager getInstance() {
	return instance;
    }

    public void start(Class<? extends InterserverWorker> workerClass) {
	// frontend port,host
	log.info("Connecting to frontend server");
	Thread t = new Thread(new NetworkThread(12346, "127.0.0.1", workerClass));
	t.start();
    }

    private Channel channel;

    public Channel getChannel() {
	return channel;
    }

    public void setChannel(Channel channel) {
	this.channel = channel;
    }

    private void sendPacket(Packet p) {
	getChannel().write(p);
    }

}
