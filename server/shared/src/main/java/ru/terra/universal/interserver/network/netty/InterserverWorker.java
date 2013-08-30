package ru.terra.universal.interserver.network.netty;

import org.jboss.netty.channel.Channel;
import ru.terra.universal.server.shared.packet.Packet;

public abstract class InterserverWorker {

    private InterserverHandler clientHandler;
    private Channel channel;

    public InterserverWorker(InterserverHandler clientHandler, Channel channel) {
        this.clientHandler = clientHandler;
        this.channel = channel;
    }

    public InterserverWorker() {
    }

    public InterserverHandler getClientHandler() {
        return clientHandler;
    }

    public void setClientHandler(InterserverHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public abstract void disconnectedFromChannel();

    public abstract void acceptPacket(Packet packet);
    // switch (packet.getOpCode()) {
    // case Server.SMSG_OK: {
    // gm.ok((OkPacket) packet);
    // }
    // break;
    // case Server.SMSG_SAY: {
    // gm.playerSaid(((SayPacket) packet).getSender(), ((SayPacket) packet).getMessage());
    // }
    // break;
    // case Server.SMSG_PLAYER_LOGGED_IN: {
    // gm.enemyLoggedIn(((PlayerLoggedInPacket) packet).getEnemy());
    // }
    // break;
    // case Server.SMSG_PLAYER_IN_GAME: {
    // gm.enemyAlreadyInGame(((PlayerInGamePacket) packet).getEnemy());
    // }
    // break;
    // case Server.SMSG_MAPOBJECT_ADD: {
    // gm.entityAdd(((MapObjectAddPacket) (packet)).getMapObject());
    // }
    // break;
    // case Client.CMSG_MOVE_BACK:
    // case Client.CMSG_MOVE_FORWARD:
    // case Client.CMSG_MOVE_LEFT:
    // case Client.CMSG_MOVE_RIGHT: {
    // MovementPacket movementPacket = (MovementPacket) packet;
    // gm.entityVectorMoving(movementPacket.getSender(), movementPacket.getX(), movementPacket.getY(), movementPacket.getZ(), movementPacket.getH(), false);
    // }
    // break;
    // case Client.CMSG_MOVE_STOP: {
    // MovementPacket movementPacket = (MovementPacket) packet;
    // gm.entityVectorMoving(movementPacket.getSender(), movementPacket.getX(), movementPacket.getY(), movementPacket.getZ(), movementPacket.getH(), true);
    // }
    // break;
    // case Client.CMSG_MOVE_TELEPORT:
    // case Server.SMSG_MOVE_HEARTBEAT: {
    // MovementPacket movementPacket = (MovementPacket) packet;
    // gm.setEntityPosition(movementPacket.getSender(), movementPacket.getX(), movementPacket.getY(), movementPacket.getZ(), movementPacket.getH());
    // }
    // break;
    // case Server.SMSG_PLAYER_INFO: {
    // PlayerInfoPacket playerInfoPacket = (PlayerInfoPacket) packet;
    // gm.playerInfoUpdate(packet.getSender(), playerInfoPacket.getPlayerInfo());
    // }
    // case Server.SMSG_MESSAGE: {
    // ServerMessagePacket serverMessagePacket = (ServerMessagePacket) packet;
    // gm.serverSaid(serverMessagePacket.getMessage());
    // }
    // }

}
