package ru.terra.universal.client.network;

import org.apache.log4j.Logger;
import ru.terra.universal.client.game.GameManager;
import ru.terra.universal.client.game.GameStateHolder;
import ru.terra.universal.client.game.GameView;
import ru.terra.universal.client.gui.GuiManager;
import ru.terra.universal.interserver.network.NetworkManager;
import ru.terra.universal.interserver.network.netty.InterserverWorker;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.entity.AbstractEntity;
import ru.terra.universal.shared.entity.PlayerInfo;
import ru.terra.universal.shared.packet.AbstractPacket;
import ru.terra.universal.shared.packet.client.BootMePacket;
import ru.terra.universal.shared.packet.client.LoginPacket;
import ru.terra.universal.shared.packet.movement.MovementPacket;
import ru.terra.universal.shared.packet.movement.PlayerTeleportPacket;
import ru.terra.universal.shared.packet.server.CharBootPacket;
import ru.terra.universal.shared.packet.server.LoginFailedPacket;
import ru.terra.universal.shared.packet.server.WorldIsNotAvail;
import ru.terra.universal.shared.packet.server.WorldStatePacket;
import ru.terra.universal.shared.packet.worldserver.EntityAddPacket;
import ru.terra.universal.shared.packet.worldserver.EntityDelPacket;
import ru.terra.universal.shared.packet.worldserver.PlayerLoggedInPacket;
import ru.terra.universal.shared.packet.worldserver.PlayerLoggedOutPacket;

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
        try {
            switch (packet.getOpCode()) {
                case OpCodes.Server.SMSG_OK: {
                    GameStateHolder.GameState gs = GameStateHolder.getInstance().getGameState();
                    logger.info("OK From server, now gs = " + gs);
                    switch (gs) {
                        case INIT:
                            GUIDHOlder.getInstance().setGuid(packet.getSender());
                            logger.info("Server helloed us, now proceed with login");
                            GameStateHolder.getInstance().setGameState(GameStateHolder.GameState.LOGIN);
                            LoginPacket loginPacket = new LoginPacket();
                            loginPacket.setLogin("mylogin");
                            loginPacket.setPassword("mypass");
                            loginPacket.setSender(GUIDHOlder.getInstance().getGuid());
                            NetworkManager.getInstance().sendPacket(loginPacket);
                            break;
                        case LOGIN:
                            GUIDHOlder.getInstance().setGuid(packet.getSender());
                            BootMePacket bootMePacket = new BootMePacket();
                            bootMePacket.setSender(GUIDHOlder.getInstance().getGuid());
                            NetworkManager.getInstance().sendPacket(bootMePacket);
                            GameStateHolder.getInstance().setGameState(GameStateHolder.GameState.LOGGED_IN);
                            break;
                        case LOGGED_IN:
                            break;
                        case CHAR_BOOT:
                            break;
                        case SERVER_SELECTED:
                            GameStateHolder.getInstance().setGameState(GameStateHolder.GameState.IN_WORLD);
                            new Thread(() -> GameView.getView().start()).start();
                            break;
                        case IN_WORLD:
                            break;
                    }
                }
                break;
                case OpCodes.Server.SMSG_CHAR_BOOT: {
                    logger.info("Received char boot packet");
                    for (String w : ((CharBootPacket) packet).getWorlds()) {
                        logger.info("Available world: " + w);
                    }
                    GameManager.getInstance().setPlayerInfo(((CharBootPacket) packet).getPlayerInfo());
                    GuiManager.getInstance().publicWorlds(((CharBootPacket) packet).getWorlds());
                    GameStateHolder.getInstance().setGameState(GameStateHolder.GameState.CHAR_BOOT);
                }
                break;
                case OpCodes.Server.SMSG_WORLD_STATE: {
                    logger.info("Received world state packet");
                    WorldStatePacket worldStatePacket = (WorldStatePacket) packet;
                    for (AbstractEntity entityInfo : worldStatePacket.getEntityInfos()) {
                        logger.info("World entity : " + entityInfo.toString());
                        GameView.getView().entityAdd(entityInfo);
                    }
                    for (PlayerInfo playerInfo : worldStatePacket.getPlayerInfos()) {
                        logger.info("Player entity : " + playerInfo.toString());
                        if (!playerInfo.getUID().equals(GameManager.getInstance().getPlayerInfo().getUID()))
                            new Thread(() -> {
                                try {
                                    Thread.sleep(10000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                GameView.getView().enemyLoggedIn(playerInfo);
                            }).start();

                    }
                }
                break;
                case OpCodes.Server.Login.SMSG_LOGIN_FAILED: {
                    logger.error("Unable to login: " + ((LoginFailedPacket) packet).getReason());
                }
                break;
                case OpCodes.WorldServer.Movement.MSG_MOVE: {
                    logger.info("Received movement packet");
                    MovementPacket mp = ((MovementPacket) packet);
                    GameView.getView().entityVectorMove(mp.getMovableId(), mp.getX(), mp.getY(), mp.getZ(), mp.getH(), false);
                }
                break;
                case OpCodes.WorldServer.Movement.MSG_MOVE_TELEPORT: {
                    logger.info("Received teleport packet");
                    PlayerTeleportPacket mp = ((PlayerTeleportPacket) packet);
                    GameView.getView().entityVectorMove(mp.getMovableId(), mp.getX(), mp.getY(), mp.getZ(), mp.getH(), true);
                }
                break;
                case OpCodes.WorldServer.PLAYER_IN: {
                    logger.info("New player logged in");
                    GameView.getView().enemyLoggedIn(((PlayerLoggedInPacket) packet).getPlayerInfo());
                }
                break;
                case OpCodes.WorldServer.PLAYER_OUT: {
                    logger.info("Player logged out");
                    GameView.getView().enemyLoggedOut(((PlayerLoggedOutPacket) packet).getGuid());
                }
                break;
                case OpCodes.WorldServer.ENTITY_ADD: {
                    logger.info("Entity add");
                    GameView.getView().entityAdd(((EntityAddPacket) packet).getEntity());
                }
                break;
                case OpCodes.WorldServer.ENTITY_DEL: {
                    logger.info("Entity del");
                    GameView.getView().entityDel(((EntityDelPacket) packet).getGuid());
                }
                break;
                case OpCodes.Server.SMSG_WORLD_NOT_AVAIL: {
                    logger.info("World " + ((WorldIsNotAvail) packet).getWorld() + " is not available");
                }
                break;
            }
        } catch (Exception e) {
            logger.error("Error while processing packet", e);
        }
    }
}
