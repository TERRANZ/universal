package ru.terra.universal.chatserver;

import org.apache.log4j.Logger;
import ru.terra.universal.interserver.network.NetworkManager;
import ru.terra.universal.shared.packet.AbstractPacket;
import ru.terra.universal.shared.packet.client.chat.ChatSayPacket;
import ru.terra.universal.shared.packet.client.chat.WispSayPacket;

public class ChatHandler {
    private final Logger log = Logger.getLogger(this.getClass());
    private final NetworkManager networkManager;

    public ChatHandler(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }

    public void handleBootChar(final Long sender) {

    }

    public void handlerUnregChar(final Long sender) {

    }

    public void handleSay(final Long sender, final AbstractPacket packet) {
        final ChatSayPacket chatSayPacket = (ChatSayPacket) packet;
        log.info("Player " + sender + " says: " + chatSayPacket.getMessage() + " to: " + chatSayPacket.getTo());
        chatSayPacket.setSender(chatSayPacket.getTo());
        networkManager.sendPacket(chatSayPacket);
    }

    public void handleWisp(final Long sender, final AbstractPacket packet) {
        final WispSayPacket chatSayPacket = (WispSayPacket) packet;
        log.info("Player " + sender + " wisps: " + chatSayPacket.getMessage() + " to: " + chatSayPacket.getTo());
        chatSayPacket.setSender(chatSayPacket.getTo());
        networkManager.sendPacket(chatSayPacket);
    }
}
