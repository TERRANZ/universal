package ru.terra.universal.server.shared.packet.interserver;

import org.jboss.netty.buffer.ChannelBuffer;

import ru.terra.universal.server.shared.constants.OpCodes.InterServer;
import ru.terra.universal.server.shared.packet.Packet;

public class HelloPacket extends Packet {

    private String hello;

    public HelloPacket(long sender) {
	super(InterServer.ISMSG_HELLO, sender);
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
