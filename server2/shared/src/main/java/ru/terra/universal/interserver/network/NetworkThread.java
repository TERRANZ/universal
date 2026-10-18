package ru.terra.universal.interserver.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.terra.universal.interserver.network.netty.ClientInitializer;
import ru.terra.universal.interserver.network.netty.InterserverWorker;

public class NetworkThread implements Runnable {

    private final Logger log = LogManager.getLogger(NetworkThread.class);
    private final int port;
    private final String addr;
    private final Class<? extends InterserverWorker> workerClass;

    public NetworkThread(int port, String addr, Class<? extends InterserverWorker> workerClass) {
        super();
        this.port = port;
        this.addr = addr;
        this.workerClass = workerClass;
    }

    @Override
    public void run() {
        final EventLoopGroup group = new MultiThreadIoEventLoopGroup(NioIoHandler.newFactory());
        try {
            final Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ClientInitializer(workerClass));
            final ChannelFuture future = bootstrap.connect(addr, port).sync();
            NetworkManager.getInstance().setChannel(future.channel());
            // Keep the event loop alive for as long as the connection is open.
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.info("Network thread interrupted", e);
            Thread.currentThread().interrupt();
        } finally {
            group.shutdownGracefully();
        }
    }
}
