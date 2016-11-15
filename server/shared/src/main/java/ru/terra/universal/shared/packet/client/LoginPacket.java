package ru.terra.universal.shared.packet.client;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.universal.shared.annoations.Packet;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.packet.AbstractPacket;

@Packet(opCode = OpCodes.Client.Login.CMSG_LOGIN)
public class LoginPacket extends AbstractPacket {

    private String login = "", password = "";

    public LoginPacket() {
    }

    @Override
    public void get(ChannelBuffer buffer) {
        login = readString(buffer);
        password = readString(buffer);
    }

    @Override
    public void send(ChannelBuffer buffer) {
        writeString(buffer, login);
        writeString(buffer, password);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
