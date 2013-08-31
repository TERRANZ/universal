package ru.terra.universal.shared.packet;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.universal.shared.annoations.Packet;

import java.io.IOException;

public abstract class AbstractPacket {
    private int opCode;
    private long sender;

    public AbstractPacket() {
        opCode = getClass().getAnnotation(Packet.class).opCode();
    }

    public long getSender() {
        return sender;
    }

    public void setSender(long sender) {
        this.sender = sender;
    }

    public int getOpCode() {
        return opCode;
    }

    public void setOpCode(int id) {
        this.opCode = id;
    }

    public static AbstractPacket read(ChannelBuffer buffer) throws IOException {
        int opcode = buffer.readUnsignedShort();
        long sguid = buffer.readLong();
        AbstractPacket packet = PacketFactory.getInstance().getPacket(opcode, sguid);
        if (packet == null)
            throw new IOException("Bad packet ID: " + opcode);

        packet.get(buffer);
        return packet;
    }

    public static AbstractPacket write(AbstractPacket packet, ChannelBuffer buffer) {
        buffer.writeChar(packet.getOpCode());
        buffer.writeLong(packet.getSender());
        packet.send(buffer);
        return packet;
    }

    // Функции, которые должен реализовать каждый класс пакета
    public abstract void get(ChannelBuffer buffer);

    public abstract void send(ChannelBuffer buffer);

    protected String readString(ChannelBuffer buffer) {
        int length = buffer.readShort();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; ++i)
            builder.append(buffer.readChar());
        return builder.toString();
    }

    protected void writeString(ChannelBuffer buffer, String text) {
        if (text == null || text.length() == 0)
            return;
        buffer.writeShort(text.length());
        for (int i = 0; i < text.length(); ++i) {
            buffer.writeChar(text.charAt(i));
        }
    }
}
