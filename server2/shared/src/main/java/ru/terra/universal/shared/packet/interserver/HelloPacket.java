package ru.terra.universal.shared.packet.interserver;

import io.netty.buffer.ByteBuf;
import ru.terra.universal.shared.annoations.Packet;
import ru.terra.universal.shared.constants.OpCodes.InterServer;
import ru.terra.universal.shared.packet.AbstractPacket;

@Packet(opCode = InterServer.ISMSG_HELLO)
public class HelloPacket extends AbstractPacket {

    private String hello;

    public String getHello() {
        return hello;
    }

    public void setHello(String hello) {
        this.hello = hello;
    }

    @Override
    public void get(ByteBuf buffer) {
        hello = readString(buffer);
    }

    @Override
    public void send(ByteBuf buffer) {
        writeString(buffer, hello);
    }

}
