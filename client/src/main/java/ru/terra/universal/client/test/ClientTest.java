package ru.terra.universal.client.test;

import org.apache.log4j.Logger;
import ru.terra.universal.client.game.GameManager;
import ru.terra.universal.client.game.GameStateHolder;
import ru.terra.universal.client.network.ClientWorker;
import ru.terra.universal.client.network.GUIDHOlder;
import ru.terra.universal.interserver.network.NetworkManager;
import ru.terra.universal.shared.packet.client.ChatSayPacket;
import ru.terra.universal.shared.packet.client.LoginPacket;
import ru.terra.universal.shared.packet.client.SelectServerPacket;

import java.util.Date;
import java.util.UUID;

/**
 * Date: 18.12.13
 * Time: 13:01
 */
public class ClientTest {
    private class TestThread implements Runnable {

        private String ip;
        private Logger logger = Logger.getLogger(this.getClass());

        public TestThread(String ip) {
            this.ip = ip;
        }

        @Override
        public void run() {
            NetworkManager.getInstance().start(ClientWorker.class, ip, 12345);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            GameStateHolder.getInstance().setGameState(GameStateHolder.GameState.LOGIN);
            LoginPacket loginPacket = new LoginPacket();
            loginPacket.setLogin("mylogin " + UUID.randomUUID().toString());
            loginPacket.setPassword("mypass");
            loginPacket.setSender(GUIDHOlder.getInstance().getGuid());
            NetworkManager.getInstance().sendPacket(loginPacket);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            GameStateHolder.getInstance().setGameState(GameStateHolder.GameState.SERVER_SELECTED);
            SelectServerPacket selectServerPacket = new SelectServerPacket();
            selectServerPacket.setTargetWorld(GameManager.getInstance().getPlayerInfo().getWorld());
            selectServerPacket.setSender(GUIDHOlder.getInstance().getGuid());
            NetworkManager.getInstance().sendPacket(selectServerPacket);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                logger.info("Saying...");
                ChatSayPacket packet = new ChatSayPacket("Current time is " + new Date().getTime(), 0L);
                packet.setSender(GUIDHOlder.getInstance().getGuid());
                NetworkManager.getInstance().sendPacket(packet);
            }
        }

    }

    public void start(String ip) {
        new Thread(new TestThread(ip)).start();
    }
}
