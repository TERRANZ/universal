package ru.terra.universal.server.shared.packet;

import org.jboss.netty.buffer.ChannelBuffer;

import java.io.IOException;

public abstract class Packet {
    private int opCode;
    private long sguid;

    public Packet(int opCode, long sender) {
        this.opCode = opCode;
        this.sguid = sender;
    }

    public long getSender() {
        return sguid;
    }

    public void setSender(long sender) {
        this.sguid = sender;
    }

    public int getOpCode() {
        return opCode;
    }

    public void setOpCode(int id) {
        this.opCode = id;
    }

    public static Packet read(ChannelBuffer buffer) throws IOException {
        int opcode = buffer.readUnsignedShort();
        long sguid = buffer.readLong();
        Packet packet = PacketFactory.getInstance().getPacket(opcode, sguid);
        if (packet == null)
            throw new IOException("Bad packet ID: " + opcode);

        packet.get(buffer);
        return packet;
    }

    public static Packet write(Packet packet, ChannelBuffer buffer) {
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
