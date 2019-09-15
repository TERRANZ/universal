package ru.terra.universal.worldserver;

import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import ru.terra.universal.interserver.network.netty.InterserverWorker;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.constants.OpCodes.InterServer;
import ru.terra.universal.shared.entity.PlayerInfo;
import ru.terra.universal.shared.packet.AbstractPacket;
import ru.terra.universal.shared.packet.interserver.CharInWorldPacket;
import ru.terra.universal.shared.packet.interserver.HelloPacket;
import ru.terra.universal.shared.packet.interserver.RegisterPacket;
import ru.terra.universal.shared.packet.movement.MovementPacket;
import ru.terra.universal.shared.packet.server.WorldStatePacket;
import ru.terra.universal.worldserver.world.WorldWorker;

public class WorldserverWorker extends InterserverWorker {

    private Logger log = Logger.getLogger(this.getClass());
    private WorldWorker worldWorker = WorldWorker.getInstance();

    @Override
    public void disconnectedFromChannel() {
        log.info("Frontend disconnected us");
    }

    @Override
    public void acceptPacket(AbstractPacket packet) {
        final Long sender = packet.getSender();
        switch (packet.getOpCode()) {
            case InterServer.ISMSG_HELLO: {
                final HelloPacket helloPacket = new HelloPacket();
                helloPacket.setHello("world server");
                final RegisterPacket registerPacket = new RegisterPacket();
                registerPacket.setStartRange(OpCodes.WorldOpcodeStart);
                registerPacket.setEndRange(OpCodes.WorldOpcodeEnd);
                registerPacket.setWorld("newScene");
                networkManager.sendPacket(helloPacket);
                networkManager.sendPacket(registerPacket);
            }
            break;
            case InterServer.ISMSG_CHAR_IN_WORLD: {
                log.info("Character " + sender + " is in world now!");
                final PlayerInfo playerInfo = ((CharInWorldPacket) packet).getPlayerInfo();
                worldWorker.addPlayer(playerInfo);
                log.info("Character " + playerInfo + " is in world now");

                final WorldStatePacket worldStatePacket = new WorldStatePacket(worldWorker.getEntities(), Lists.newArrayList(worldWorker.getPlayers().values()));
                worldStatePacket.setSender(sender);
                networkManager.sendPacket(worldStatePacket);
            }
            break;
            case InterServer.ISMSG_UNREG_CHAR: {
                log.info("Unregistering char with uid = " + sender);
                worldWorker.removePlayer(packet.getSender());
            }
            break;
            case OpCodes.WorldServer.Movement.MSG_MOVE: {
                worldWorker.playerMove((MovementPacket) packet);
            }
            break;
            case OpCodes.WorldServer.Movement.MSG_MOVE_TELEPORT: {
                worldWorker.updatePlayerPosition((MovementPacket) packet);
            }
            break;
        }
    }
}
