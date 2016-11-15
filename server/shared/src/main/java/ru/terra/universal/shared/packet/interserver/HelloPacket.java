package ru.terra.universal.shared.packet.interserver;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.universal.shared.annoations.Packet;
import ru.terra.universal.shared.constants.OpCodes.InterServer;
import ru.terra.universal.shared.packet.AbstractPacket;

@Packet(opCode = InterServer.ISMSG_HELLO)
public class HelloPacket extends AbstractPacket {

    private String hello;

    public HelloPacket() {
    }

    public String getHello() {
        return hello;
    }

    public void setHello(String hello) {
        this.hello = hello;
    }

    @Override
    public void get(ChannelBuffer buffer) {
        hello = readString(buffer);
    }

    @Override
    public void send(ChannelBuffer buffer) {
        writeString(buffer, hello);
    }

}
