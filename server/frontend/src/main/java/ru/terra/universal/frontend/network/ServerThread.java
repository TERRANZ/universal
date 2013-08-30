package ru.terra.universal.frontend.network;

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;
import ru.terra.universal.frontend.network.netty.ServerPipelineFactory;
import ru.terra.universal.frontend.network.netty.ServerWorker;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class ServerThread implements Runnable {
    private int port;
    private String bindAddr;
    private Logger log = Logger.getLogger(ServerThread.class);
    private Class<? extends ServerWorker> serverWorker;

    public ServerThread(int port, String bindAddr, Class<? extends ServerWorker> serverWorker) {
        this.port = port;
        this.bindAddr = bindAddr;
        this.serverWorker = serverWorker;
    }

    @Override
    public void run() {
        log.info("Server thread with " + serverWorker.getCanonicalName() + " started");
        ExecutorService bossExec = new OrderedMemoryAwareThreadPoolExecutor(1, 400000000, 2000000000, 60, TimeUnit.SECONDS);
        ExecutorService ioExec = new OrderedMemoryAwareThreadPoolExecutor(4 /* число рабочих потоков */, 400000000, 2000000000, 60, TimeUnit.SECONDS);
        ServerBootstrap networkServer = new ServerBootstrap(new NioServerSocketChannelFactory(bossExec, ioExec, 4 /*
                                                           * то же самое число рабочих потоков
														   */));
        networkServer.setOption("backlog", 500);
        networkServer.setOption("connectTimeoutMillis", 10000);
        networkServer.setPipelineFactory(new ServerPipelineFactory(serverWorker));
        Channel channel = networkServer.bind(new InetSocketAddress(bindAddr, port));
    }

}
