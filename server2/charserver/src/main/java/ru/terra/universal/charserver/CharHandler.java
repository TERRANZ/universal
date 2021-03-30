package ru.terra.universal.charserver;

import ru.terra.universal.interserver.network.NetworkManager;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.entity.PlayerInfo;
import ru.terra.universal.shared.packet.AbstractPacket;
import ru.terra.universal.shared.packet.interserver.*;
import ru.terra.universal.shared.packet.server.CharBootPacket;
import ru.terra.universal.shared.packet.server.OkPacket;
import ru.terra.universal.shared.packet.server.WorldIsNotAvail;
import ru.terra.universal.shared.persistance.CharLoader;
import ru.terra.universal.shared.persistance.CharSaver;
import ru.terra.universal.shared.persistance.impl.JsonCharLoaderImpl;
import ru.terra.universal.shared.persistance.impl.JsonCharSaverImpl;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static com.google.common.collect.Lists.newArrayList;

public class CharHandler {
    private final List<PlayerInfo> players = new LinkedList<>();
    private final CharLoader charLoader = new JsonCharLoaderImpl();
    private final CharSaver charSaver = new JsonCharSaverImpl();
    private final NetworkManager networkManager;

    public CharHandler(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }

    public void handleBootChar(final Long sender) {
        final PlayerInfo playerInfo = charLoader.loadCharacter(sender).orElseGet(() -> {
            final PlayerInfo newPlayerInfo = new PlayerInfo();
            newPlayerInfo.setUID(sender);
            newPlayerInfo.setName("My Cool player " + newPlayerInfo.getUID());
            newPlayerInfo.setWorld("newScene");
            final float min = 20.0f;
            final float max = 50.0f;

            final Random rand = new Random();

            newPlayerInfo.setX(rand.nextFloat() * (max - min) + min);
            newPlayerInfo.setY(rand.nextFloat() * (max - min) + min);
            newPlayerInfo.setZ(rand.nextFloat() * (max - min) + min);
            charSaver.save(newPlayerInfo);

            return newPlayerInfo;
        });

        networkManager.sendPacket(new CharBootPacket(sender, playerInfo, newArrayList(playerInfo.getWorld())));
        players.add(playerInfo);
    }

    public void handleUnregChar(final Long sender) {
        PlayerInfo playerInfoToRemove = null;
        for (final PlayerInfo playerInfo : players)
            if (playerInfo.getUID().equals(sender))
                playerInfoToRemove = playerInfo;

        if (playerInfoToRemove != null)
            players.remove(playerInfoToRemove);
    }

    public void handleUpdateChar(final AbstractPacket packet) {
        charSaver.save(((UpdatePlayerPacket) packet).getPlayerInfo());
    }

    public void handleIsWorldAvail(final Long sender, final AbstractPacket packet) {
        if (((ISWorldAvaiPacket) packet).getAvail()) {
            networkManager.sendPacket(new OkPacket(sender));
            PlayerInfo playerInfo = null;
            for (PlayerInfo pi : players) {
                if (pi.getUID().equals(sender))
                    playerInfo = pi;
            }
            if (playerInfo != null) {
                networkManager.sendPacket(new CharInWorldPacket(sender, playerInfo));
            }
        } else {
            networkManager.sendPacket(new WorldIsNotAvail(sender, ((ISWorldAvaiPacket) packet).getWorld()));
        }
    }
}
