package ru.terra.universal.client.test;

import ru.terra.universal.client.game.GameStateHolder;
import ru.terra.universal.client.network.ClientWorker;
import ru.terra.universal.client.network.GUIDHOlder;
import ru.terra.universal.interserver.network.NetworkManager;
import ru.terra.universal.shared.packet.client.LoginPacket;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Date: 18.12.13
 * Time: 13:01
 */
public class ClientTest {
    private class TestThread implements Runnable {

        private String ip;

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
        }
    }

    public void start(String ip) {
        new Thread(new TestThread(ip)).start();
    }
}
