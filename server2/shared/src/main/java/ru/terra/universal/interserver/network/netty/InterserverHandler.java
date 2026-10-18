package ru.terra.universal.interserver.network.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;
import ru.terra.universal.shared.packet.AbstractPacket;

public class InterserverHandler extends ChannelInboundHandlerAdapter {

    private InterserverWorker worker;
    private final PacketFrameDecoder decoder;
    private final PacketFrameEncoder encoder;
    private final Logger log = Logger.getLogger(InterserverHandler.class);
    private final Class<? extends InterserverWorker> workerClass;

    public PacketFrameDecoder getDecoder() {
        return decoder;
    }

    public PacketFrameEncoder getEncoder() {
        return encoder;
    }

    public InterserverHandler(PacketFrameDecoder decoder, PacketFrameEncoder encoder, Class<? extends InterserverWorker> workerClass) {
        this.decoder = decoder;
        this.encoder = encoder;
        this.workerClass = workerClass;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // Событие вызывается при подключении клиента. Я создаю здесь Worker игрока — объект, который занимается обработкой данных игрока
        // непостредственно.
        // Я передаю ему канал игрока, чтобы он мог в него посылать пакеты
        worker = workerClass.getDeclaredConstructor().newInstance();
        worker.setChannel(ctx.channel());
        worker.setClientHandler(this);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // Событие закрытия канала. Обрабатывается, чтобы освободить ресурсы, когда соединение оборвалось.
        worker.disconnectedFromChannel();
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
        ctx.channel().close();
        log.info("exceptionCaught", cause);
    }
}
