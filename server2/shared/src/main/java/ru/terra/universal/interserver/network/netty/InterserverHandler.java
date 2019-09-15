package ru.terra.universal.interserver.network.netty;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.*;
import ru.terra.universal.shared.packet.AbstractPacket;

public class InterserverHandler extends SimpleChannelUpstreamHandler {

    private InterserverWorker worker;
    private PacketFrameDecoder decoder;
    private PacketFrameEncoder encoder;
    private Logger log = Logger.getLogger(InterserverHandler.class);
    private Class<? extends InterserverWorker> workerClass;

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
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        // Событие вызывается при подключении клиента. Я создаю здесь Worker игрока — объект, который занимается обработкой данных игрока
        // непостредственно.
        // Я передаю ему канал игрока (функция e.getChannel()), чтобы он мог в него посылать пакеты
        worker = workerClass.newInstance();
        worker.setChannel(e.getChannel());
        worker.setClientHandler(this);
        // worker = new InterserverWorker(this, e.getChannel());
        // log.info("channelConnected");
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        // Событие закрытия канала. Используется в основном, чтобы освободить ресурсы, или выполнить другие действия, которые происходят при
        // отключении пользователя. Если его не обработать, Вы можете и не заметить, что пользователь отключился, если он напрямую не сказал этого
        // серверу, а просто оборвался канал.
        // log.info("channelDisconnected");
        worker.disconnectedFromChannel();
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        // Функция принимает уже готовые AbstractPacket'ы от игрока, поэтому их можно сразу посылать в worker. За их формирование отвечает другой обработчик.
        // log.info("messageReceived");
        if (e.getChannel().isOpen())
            worker.acceptPacket((AbstractPacket) e.getMessage());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        // На канале произошло исключение. Выводим ошибку, закрываем канал.
        // Server.logger.log(Level.WARNING, "Exception from downstream", e.getCause());
        ctx.getChannel().close();
        log.info("exceptionCaught", e.getCause());
    }
}
