package ru.terra.universal.shared.packet.server;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.universal.shared.annoations.Packet;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.packet.AbstractPacket;

/**
 * Date: 25.04.14
 * Time: 15:39
 */
@Packet(opCode = OpCodes.Server.Login.SMSG_LOGIN_FAILED)
public class LoginFailedPacket extends AbstractPacket {
    private String reason = "";

    public LoginFailedPacket() {
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public void get(ChannelBuffer buffer) {
        reason = readString(buffer);
    }

    @Override
    public void send(ChannelBuffer buffer) {
        writeString(buffer, reason);
    }
}
