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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class CharWorker extends InterserverWorker {

    private Logger log = Logger.getLogger(this.getClass());
    private List<PlayerInfo> players = new LinkedList<>();
    private CharLoader charLoader = new JsonCharLoaderImpl();
    private CharSaver charSaver = new JsonCharSaverImpl();

    @Override
    public void disconnectedFromChannel() {
        log.info("Frontend disconnected us");
    }

    @Override
    public void acceptPacket(AbstractPacket packet) {
        switch (packet.getOpCode()) {
            case InterServer.ISMSG_HELLO: {
                HelloPacket helloPacket = new HelloPacket();
                helloPacket.setHello("char server");
                RegisterPacket registerPacket = new RegisterPacket();
                registerPacket.setStartRange(OpCodes.CharOpcodeStart);
                registerPacket.setEndRange(OpCodes.CharOpcodeEnd);
                networkManager.sendPacket(helloPacket);
                networkManager.sendPacket(registerPacket);
            }
            break;
            case InterServer.ISMSG_BOOT_CHAR: {
                log.info("Registering character with uid = " + packet.getSender());
                PlayerInfo playerInfo = charLoader.loadCharacter(packet.getSender());
                if (playerInfo == null) {
                    playerInfo = new PlayerInfo();
                    playerInfo.setUID(packet.getSender());
                    playerInfo.setName("My Cool player " + playerInfo.getUID());
                    playerInfo.setWorld("newScene");
                    float min = 20.0f;
                    float max = 50.0f;

                    Random rand = new Random();

                    playerInfo.setX(rand.nextFloat() * (max - min) + min);
                    playerInfo.setY(rand.nextFloat() * (max - min) + min);
                    playerInfo.setZ(rand.nextFloat() * (max - min) + min);
                    charSaver.save(playerInfo);
                }

                CharBootPacket charBootPacket = new CharBootPacket();
                charBootPacket.setPlayerInfo(playerInfo);
                List<String> worlds = new ArrayList<>();
                worlds.add(playerInfo.getWorld());
                charBootPacket.setWorlds(worlds);
                charBootPacket.setSender(packet.getSender());
                networkManager.sendPacket(charBootPacket);
                players.add(playerInfo);
            }
            break;
            case OpCodes.Client.Char.CMSG_SELECT_SERVER: {
                log.info("Character " + packet.getSender() + " selected server!");
                ISWorldAvaiPacket isWorldAvaiPacket = new ISWorldAvaiPacket(((SelectServerPacket) packet).getTargetWorld());
                isWorldAvaiPacket.setSender(packet.getSender());
                networkManager.sendPacket(isWorldAvaiPacket);
            }
            break;
            case InterServer.ISMSG_UNREG_CHAR: {
                log.info("Unregistering char with uid = " + packet.getSender());
                PlayerInfo playerInfoToRemove = null;
                for (PlayerInfo playerInfo : players)
                    if (playerInfo.getUID().equals(packet.getSender()))
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
                    OkPacket okPacket = new OkPacket();
                    okPacket.setSender(packet.getSender());
                    networkManager.sendPacket(okPacket);
                    PlayerInfo playerInfo = null;
                    for (PlayerInfo pi : players) {
                        if (pi.getUID().equals(packet.getSender()))
                            playerInfo = pi;
                    }
                    if (playerInfo != null) {
                        CharInWorldPacket charInWorldPacket = new CharInWorldPacket(playerInfo);
                        charInWorldPacket.setSender(packet.getSender());
                        networkManager.sendPacket(charInWorldPacket);
                    }
                } else {
                    WorldIsNotAvail worldIsNotAvail = new WorldIsNotAvail();
                    worldIsNotAvail.setWorld(((ISWorldAvaiPacket) packet).getWorld());
                    worldIsNotAvail.setSender(packet.getSender());
                    networkManager.sendPacket(worldIsNotAvail);
                }
            }
            break;
        }
    }

}
