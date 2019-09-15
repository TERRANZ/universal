package ru.terra.universal.charserver;

import org.apache.log4j.Logger;
import ru.terra.universal.interserver.network.netty.InterserverWorker;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.constants.OpCodes.InterServer;
import ru.terra.universal.shared.entity.PlayerInfo;
import ru.terra.universal.shared.packet.AbstractPacket;
import ru.terra.universal.shared.packet.client.SelectServerPacket;
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

public class CharWorker extends InterserverWorker {

    private final Logger log = Logger.getLogger(this.getClass());
    private final List<PlayerInfo> players = new LinkedList<>();
    private final CharLoader charLoader = new JsonCharLoaderImpl();
    private final CharSaver charSaver = new JsonCharSaverImpl();

    @Override
    public void disconnectedFromChannel() {
        log.info("Frontend disconnected us");
    }

    @Override
    public void acceptPacket(final AbstractPacket packet) {
        final Long sender = packet.getSender();
        switch (packet.getOpCode()) {
            case InterServer.ISMSG_HELLO: {
                final HelloPacket helloPacket = new HelloPacket();
                helloPacket.setHello("char server");
                final RegisterPacket registerPacket = new RegisterPacket();
                registerPacket.setStartRange(OpCodes.CharOpcodeStart);
                registerPacket.setEndRange(OpCodes.CharOpcodeEnd);
                networkManager.sendPacket(helloPacket);
                networkManager.sendPacket(registerPacket);
            }
            break;
            case InterServer.ISMSG_BOOT_CHAR: {
                log.info("Registering character with uid = " + sender);
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
            break;
            case OpCodes.Client.Char.CMSG_SELECT_SERVER: {
                log.info("Character " + sender + " selected server!");
                networkManager.sendPacket(new ISWorldAvaiPacket(sender, ((SelectServerPacket) packet).getTargetWorld()));
            }
            break;
            case InterServer.ISMSG_UNREG_CHAR: {
                log.info("Unregistering char with uid = " + sender);
                PlayerInfo playerInfoToRemove = null;
                for (final PlayerInfo playerInfo : players)
                    if (playerInfo.getUID().equals(sender))
                        playerInfoToRemove = playerInfo;

                if (playerInfoToRemove != null)
                    players.remove(playerInfoToRemove);

            }
            break;
            case InterServer.ISMSG_UPDATE_CHAR: {
                charSaver.save(((UpdatePlayerPacket) packet).getPlayerInfo());
            }
            break;
            case InterServer.ISMSG_IS_WORLD_AVAIL: {
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
            break;
        }
    }

}
