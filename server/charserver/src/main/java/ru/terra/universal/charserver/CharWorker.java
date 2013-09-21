package ru.terra.universal.charserver;

import org.apache.log4j.Logger;
import ru.terra.universal.interserver.network.NetworkManager;
import ru.terra.universal.interserver.network.netty.InterserverWorker;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.constants.OpCodes.InterServer;
import ru.terra.universal.shared.entity.PlayerInfo;
import ru.terra.universal.shared.packet.AbstractPacket;
import ru.terra.universal.shared.packet.interserver.CharInWorldPacket;
import ru.terra.universal.shared.packet.interserver.HelloPacket;
import ru.terra.universal.shared.packet.interserver.RegisterPacket;
import ru.terra.universal.shared.packet.server.CharBootPacket;
import ru.terra.universal.shared.packet.server.OkPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CharWorker extends InterserverWorker {

    private Logger log = Logger.getLogger(this.getClass());
    private List<String> worlds = new ArrayList<>();
    private NetworkManager networkManager = NetworkManager.getInstance();

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
                //TODO: there we load character from db and send
                PlayerInfo playerInfo = new PlayerInfo();
                playerInfo.setName("My first cool world!");
                String wuid = UUID.randomUUID().toString();
                playerInfo.setWorld(wuid);
                CharBootPacket charBootPacket = new CharBootPacket();
                charBootPacket.setPlayerInfo(playerInfo);
                worlds.add(wuid);
                charBootPacket.setWorlds(worlds);
                charBootPacket.setSender(packet.getSender());
                networkManager.sendPacket(charBootPacket);
            }
            break;
            case OpCodes.Client.Char.CMSG_SELECT_SERVER: {
                log.info("Character " + packet.getSender() + " selected server!");
                //There we checks for worlds availability
                OkPacket okPacket = new OkPacket();
                okPacket.setSender(packet.getSender());
                networkManager.sendPacket(okPacket);
                CharInWorldPacket charInWorldPacket = new CharInWorldPacket();
                charInWorldPacket.setSender(packet.getSender());
                networkManager.sendPacket(charInWorldPacket);
            }
            break;
            case InterServer.ISMSG_UNREG_CHAR: {
                log.info("Unregistering char with uid = " + packet.getSender());
            }
            break;
        }
    }

}
