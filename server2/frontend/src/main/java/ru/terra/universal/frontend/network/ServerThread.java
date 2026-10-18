package ru.terra.universal.frontend.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.terra.universal.frontend.network.netty.ServerInitializer;
import ru.terra.universal.frontend.network.netty.ServerWorker;

public class ServerThread implements Runnable {
    private final int port;
    private final String bindAddr;
    private final Logger log = LogManager.getLogger(ServerThread.class);
    private final Class<? extends ServerWorker> serverWorker;

    public ServerThread(final int port, final String bindAddr, Class<? extends ServerWorker> serverWorker) {
        this.port = port;
        this.bindAddr = bindAddr;
        this.serverWorker = serverWorker;
    }

    @Override
    public void run() {
        log.info("Server thread with " + serverWorker.getCanonicalName() + " started");
        final EventLoopGroup bossExec = new MultiThreadIoEventLoopGroup(1, NioIoHandler.newFactory());
        final EventLoopGroup ioExec = new MultiThreadIoEventLoopGroup(4 /* число рабочих потоков */, NioIoHandler.newFactory());
        try {
            final ServerBootstrap networkServer = new ServerBootstrap();
            networkServer.group(bossExec, ioExec)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 500)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ServerInitializer(serverWorker));
            final ChannelFuture future = networkServer.bind(bindAddr, port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.info("Server thread interrupted", e);
            Thread.currentThread().interrupt();
        } finally {
            bossExec.shutdownGracefully();
            ioExec.shutdownGracefully();
        }
    }
}
