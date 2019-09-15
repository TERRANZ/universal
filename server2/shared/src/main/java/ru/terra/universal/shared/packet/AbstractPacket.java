package ru.terra.universal.shared.packet;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.universal.shared.annoations.Packet;

import java.io.IOException;

public abstract class AbstractPacket {
    private Integer opCode = 0;
    protected Long sender = 0L;

    public AbstractPacket() {
        this.opCode = getClass().getAnnotation(Packet.class).opCode();
    }

    public AbstractPacket(final Long sender) {
        this.sender = sender;
        this.opCode = getClass().getAnnotation(Packet.class).opCode();
    }

    public Long getSender() {
        return sender;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }

    public Integer getOpCode() {
        return opCode;
    }

    public void setOpCode(Integer id) {
        this.opCode = id;
    }

    public static AbstractPacket read(final ChannelBuffer buffer) throws IOException {
        final int opcode = buffer.readUnsignedShort();
        Long sguid = buffer.readLong();
        final AbstractPacket packet = PacketFactory.getInstance().getPacket(opcode, sguid);
        if (packet == null)
            throw new IOException("Bad packet ID: " + opcode);

        packet.get(buffer);
        return packet;
    }

    public static AbstractPacket write(final AbstractPacket packet, final ChannelBuffer buffer) {
        buffer.writeChar(packet.getOpCode());
        buffer.writeLong(packet.getSender());
        packet.send(buffer);
        return packet;
    }

    // Функции, которые должен реализовать каждый класс пакета

    /**
     * Вызывается при приёме пакета, в этом методе производим вычитывание из буфера данных
     */
    public abstract void get(final ChannelBuffer buffer);

    /**
     * Вызывается при отсылке пакета, в этом методе производим запись в буфер данных
     */
    public abstract void send(final ChannelBuffer buffer);

    public static String readString(final ChannelBuffer buffer) {
        final int length = buffer.readShort();
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; ++i)
            builder.append(buffer.readChar());
        return builder.toString();
    }

    public static void writeString(final ChannelBuffer buffer, final String text) {
        if (text == null || text.length() == 0)
            return;
        buffer.writeShort(text.length());
        for (int i = 0; i < text.length(); ++i) {
            buffer.writeChar(text.charAt(i));
        }
    }
}
