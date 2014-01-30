package ru.terra.universal.shared.packet.client;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.universal.shared.annoations.Packet;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.packet.AbstractPacket;

/**
 * Date: 14.01.14
 * Time: 13:45
 */
@Packet(opCode = OpCodes.Client.Chat.CMSG_SAY)
public class ChatSayPacket extends AbstractPacket {
    private String message = "";
    private long from = 0L, to = 0L;

    public ChatSayPacket() {
    }


    public ChatSayPacket(String message, long from, long to) {
        this.message = message;
        this.from = from;
        this.to = to;
    }

    @Override
    public void get(ChannelBuffer buffer) {

    }

    @Override
    public void send(ChannelBuffer buffer) {

    }
}
