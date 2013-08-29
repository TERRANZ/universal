package ru.terra.universal.interserver.network;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import ru.terra.universal.interserver.network.netty.ClientPipelineFactory;
import ru.terra.universal.interserver.network.netty.InterserverWorker;

public class NetworkThread implements Runnable {

    private int port;
    private String addr;
    private Class<? extends InterserverWorker> workerClass;

    public NetworkThread(int port, String addr, Class<? extends InterserverWorker> workerClass) {
	super();
	this.port = port;
	this.addr = addr;
	this.workerClass = workerClass;
    }

    @Override
    public void run() {
	ChannelFactory factory = new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
	ClientBootstrap bootstrap = new ClientBootstrap(factory);
	bootstrap.setPipelineFactory(new ClientPipelineFactory(workerClass));
	bootstrap.setOption("tcpNoDelay", true);
	bootstrap.setOption("keepAlive", true);
	ChannelFuture c = bootstrap.connect(new InetSocketAddress(addr, port));
	NetworkManager.getInstance().setChannel(c.getChannel());
    }
}
