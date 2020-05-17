package ru.terra.universal.client.test;

import static ru.terra.universal.client.game.GameStateHolder.GameState.CHAR_BOOT;
import static ru.terra.universal.client.game.GameStateHolder.GameState.INIT;
import static ru.terra.universal.client.game.GameStateHolder.GameState.IN_WORLD;
import static ru.terra.universal.client.game.GameStateHolder.GameState.LOGGED_IN;
import static ru.terra.universal.client.game.GameStateHolder.GameState.LOGIN;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import ru.terra.universal.client.game.GameManager;
import ru.terra.universal.client.game.GameStateChangeNotifier;
import ru.terra.universal.client.game.GameStateHolder;
import ru.terra.universal.client.network.GUIDHOlder;
import ru.terra.universal.interserver.network.NetworkManager;
import ru.terra.universal.interserver.network.netty.InterserverWorker;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.entity.AbstractEntity;
import ru.terra.universal.shared.entity.PlayerInfo;
import ru.terra.universal.shared.packet.AbstractPacket;
import ru.terra.universal.shared.packet.client.BootMePacket;
import ru.terra.universal.shared.packet.client.LoginPacket;
import ru.terra.universal.shared.packet.client.SelectServerPacket;
import ru.terra.universal.shared.packet.movement.MovementPacket;
import ru.terra.universal.shared.packet.movement.PlayerTeleportPacket;
import ru.terra.universal.shared.packet.server.CharBootPacket;
import ru.terra.universal.shared.packet.server.LoginFailedPacket;
import ru.terra.universal.shared.packet.server.WorldIsNotAvail;
import ru.terra.universal.shared.packet.server.WorldStatePacket;

public class TestWorker extends InterserverWorker {

    private class TestGameStateHolder {

        private GameStateChangeNotifier notifier;
        private GameStateHolder.GameState gameState = INIT;

        public GameStateHolder.GameState getGameState() {
            synchronized (gameState) {
                return gameState;
            }
        }

        public void setGameState(GameStateHolder.GameState gameState) {
            synchronized (gameState) {
                logger.info("Changing game state from " + getGameState().toString() + " to " + gameState.toString());
                if (notifier != null) {
                    notifier.onGameStateChange(getGameState(), gameState);
                }
                this.gameState = gameState;
            }
        }
    }

    private Logger logger = Logger.getLogger(this.getClass());
    private TestGameStateHolder gs = new TestGameStateHolder();
    private GUIDHOlder gh = new GUIDHOlder();
    private GameManager gm = new GameManager();
    private List<String> worlds = new ArrayList<>();

    @Override
    public void disconnectedFromChannel() {

    }

    @Override
    public void acceptPacket(AbstractPacket packet) {
        try {
            switch (packet.getOpCode()) {
                case OpCodes.Server.SMSG_OK: {
                    logger.info("OK From server, now gs = " + gs);
                    switch (gs.gameState) {
                        case INIT:
                            gh.setGuid(packet.getSender());
                            logger.info("Server helloed us, now proceed with login");
                            gs.setGameState(LOGIN);
                            LoginPacket loginPacket = new LoginPacket();
                            loginPacket.setLogin("mylogin " + new Date().getTime());
                            loginPacket.setPassword("mypass");
                            loginPacket.setSender(gh.getGuid());
                            NetworkManager.getInstance().sendPacket(loginPacket);
                            break;
                        case LOGIN:
                            gh.setGuid(packet.getSender());
                            BootMePacket bootMePacket = new BootMePacket();
                            bootMePacket.setSender(gh.getGuid());
                            NetworkManager.getInstance().sendPacket(bootMePacket);
                            gs.setGameState(LOGGED_IN);
                            break;
                        case LOGGED_IN:
                            break;
                        case CHAR_BOOT:

                            break;
                        case SERVER_SELECTED:
                            gs.setGameState(IN_WORLD);
                            break;
                        case IN_WORLD:
                            break;
                    }
                }
                break;
                case OpCodes.Server.SMSG_CHAR_BOOT: {
                    logger.info("Received char boot packet");
                    worlds.clear();
                    for (String w : ((CharBootPacket) packet).getWorlds()) {
                        logger.info("Available world: " + w);
                        worlds.add(w);
                    }
                    gm.setPlayerInfo(((CharBootPacket) packet).getPlayerInfo());
                    gs.setGameState(CHAR_BOOT);

                    SelectServerPacket selectServerPacket = new SelectServerPacket();
                    selectServerPacket.setTargetWorld(worlds.get(0));
                    selectServerPacket.setSender(gh.getGuid());
                    gs.setGameState(GameStateHolder.GameState.SERVER_SELECTED);
                    NetworkManager.getInstance().sendPacket(selectServerPacket);
                }
                break;
                case OpCodes.Server.SMSG_WORLD_STATE: {
                    logger.info("Received world state packet");
                    WorldStatePacket worldStatePacket = (WorldStatePacket) packet;
                    for (AbstractEntity entityInfo : worldStatePacket.getEntityInfos()) {
                        logger.info("World entity : " + entityInfo.toString());
                    }
                    for (PlayerInfo playerInfo : worldStatePacket.getPlayerInfos()) {
                        logger.info("Player entity : " + playerInfo.toString());
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
                }
                break;
                case OpCodes.WorldServer.Movement.MSG_MOVE_TELEPORT: {
                    logger.info("Received teleport packet");
                    PlayerTeleportPacket mp = ((PlayerTeleportPacket) packet);
                }
                break;
                case OpCodes.WorldServer.PLAYER_IN: {
                    logger.info("New player logged in");
                }
                break;
                case OpCodes.WorldServer.PLAYER_OUT: {
                    logger.info("Player logged out");
                }
                break;
                case OpCodes.WorldServer.ENTITY_ADD: {
                    logger.info("Entity add");
                }
                break;
                case OpCodes.WorldServer.ENTITY_DEL: {
                    logger.info("Entity del");
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
