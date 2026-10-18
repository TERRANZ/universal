package ru.terra.universal.frontend.network.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;
import ru.terra.universal.shared.packet.AbstractPacket;


public class ServerHandler extends ChannelInboundHandlerAdapter {

    private ServerWorker worker;
    private final PacketFrameDecoder decoder;
    private final PacketFrameEncoder encoder;
    private final Logger log = Logger.getLogger(ServerHandler.class);
    private final Class<? extends ServerWorker> serverWorker;

    public PacketFrameDecoder getDecoder() {
        return decoder;
    }

    public PacketFrameEncoder getEncoder() {
        return encoder;
    }

    public ServerHandler(PacketFrameDecoder decoder,
                         PacketFrameEncoder encoder,
                         Class<? extends ServerWorker> serverWorker) {
        this.decoder = decoder;
        this.encoder = encoder;
        this.serverWorker = serverWorker;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // Событие вызывается при подключении клиента. Я создаю здесь Worker игрока — объект, который занимается обработкой данных игрока
        // непостредственно, и передаю ему канал игрока.
        worker = serverWorker.getDeclaredConstructor().newInstance();
        worker.setChannel(ctx.channel());
        worker.setPlayerHandler(this);
        worker.sendHello();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // Событие закрытия канала. Вызывается, когда соединение оборвалось.
        log.info("channelDisconnected");
        worker.disconnectedFromChannel(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // Функция принимает уже готовые AbstractPacket'ы от игрока, поэтому их можно сразу посылать в worker.
        if (ctx.channel().isOpen())
            worker.acceptPacket((AbstractPacket) msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // На канале произошло исключение. Выводим ошибку, закрываем канал.
        log.info("exceptionCaught", cause);
        ctx.channel().close();
    }
}
