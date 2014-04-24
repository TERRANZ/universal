package ru.terra.universal.client.network;

import org.apache.log4j.Logger;
import ru.terra.universal.client.game.GameManager;
import ru.terra.universal.client.game.GameStateHolder;
import ru.terra.universal.client.gui.GuiManager;
import ru.terra.universal.interserver.network.NetworkManager;
import ru.terra.universal.interserver.network.netty.InterserverWorker;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.entity.PlayerInfo;
import ru.terra.universal.shared.entity.WorldEntity;
import ru.terra.universal.shared.packet.AbstractPacket;
import ru.terra.universal.shared.packet.client.BootMePacket;
import ru.terra.universal.shared.packet.server.CharBootPacket;
import ru.terra.universal.shared.packet.server.WorldStatePacket;

/**
 * User: Vadim Korostelev
 * Date: 30.08.13
 * Time: 16:15
 */
public class ClientWorker extends InterserverWorker {
    private Logger logger = Logger.getLogger(this.getClass());

    @Override
    public void disconnectedFromChannel() {
    }

    @Override
    public void acceptPacket(AbstractPacket packet) {
        logger.info("Packet accepted " + packet.getOpCode());
        switch (packet.getOpCode()) {
            case OpCodes.Server.SMSG_OK: {
                GameStateHolder.GameState gs = GameStateHolder.getInstance().getGameState();
                switch (gs) {
                    case INIT:
                        GUIDHOlder.getInstance().setGuid(packet.getSender());
                        break;
                    case LOGIN:
                        GameStateHolder.getInstance().setGameState(GameStateHolder.GameState.LOGGED_IN);
                        GUIDHOlder.getInstance().setGuid(packet.getSender());
                        BootMePacket bootMePacket = new BootMePacket();
                        bootMePacket.setSender(GUIDHOlder.getInstance().getGuid());
                        NetworkManager.getInstance().sendPacket(bootMePacket);
                        break;
                    case LOGGED_IN:
                        break;
                    case CHAR_BOOT:
                        break;
                    case SERVER_SELECTED:
                        GameStateHolder.getInstance().setGameState(GameStateHolder.GameState.IN_WORLD);
                        break;
                    case IN_WORLD:
                        break;
                }
            }
            break;
            case OpCodes.Server.SMSG_CHAR_BOOT: {
                GameStateHolder.getInstance().setGameState(GameStateHolder.GameState.CHAR_BOOT);
                logger.info("Received char boot packet");
                for (String w : ((CharBootPacket) packet).getWorlds()) {
                    logger.info("Available world: " + w);
                }
                GameManager.getInstance().setPlayerInfo(((CharBootPacket) packet).getPlayerInfo());
                GuiManager.getInstance().publicWorlds(((CharBootPacket) packet).getWorlds());
            }
            break;
            case OpCodes.Server.SMSG_WORLD_STATE: {
                logger.info("Received world state packet");
                WorldStatePacket worldStatePacket = (WorldStatePacket) packet;
                for (WorldEntity worldEntityInfo : worldStatePacket.getEntityInfos()) {
                    logger.info("World entity : " + worldEntityInfo.toString());
                }
                for (PlayerInfo playerInfo : worldStatePacket.getPlayerInfos()) {
                    logger.info("Player entity : " + playerInfo.toString());
                }

            }
            break;

        }
    }
}
