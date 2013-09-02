package ru.terra.universal.client;

import org.apache.log4j.BasicConfigurator;
import ru.terra.universal.interserver.network.NetworkManager;
import ru.terra.universal.shared.packet.client.BootMePacket;
import ru.terra.universal.shared.packet.client.LoginPacket;
import ru.terra.universal.shared.packet.client.SelectServerPacket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        BasicConfigurator.configure();
        GameStateHolder.getInstance().setGameState(GameStateHolder.GameState.INIT);
        NetworkManager.getInstance().start(ClientWorker.class, "127.0.0.1", 12345);
        try {

            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String s = bufferRead.readLine();

                if (s.equals("login")) {
                    GameStateHolder.getInstance().setGameState(GameStateHolder.GameState.LOGIN);
                    LoginPacket loginPacket = new LoginPacket();
                    loginPacket.setLogin("mylogin");
                    loginPacket.setPassword("mypass");
                    loginPacket.setSender(0);
                    NetworkManager.getInstance().sendPacket(loginPacket);
                } else if (s.equals("boot")) {
                    BootMePacket bootMePacket = new BootMePacket();
                    bootMePacket.setSender(GUIDHOlder.getInstance().getGuid());
                    NetworkManager.getInstance().sendPacket(bootMePacket);
                } else if (s.equals("world")) {
                    SelectServerPacket selectServerPacket = new SelectServerPacket();
                    selectServerPacket.setTargetWorld("1");
                    selectServerPacket.setSender(GUIDHOlder.getInstance().getGuid());
                    NetworkManager.getInstance().sendPacket(selectServerPacket);
                }
                //System.out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
