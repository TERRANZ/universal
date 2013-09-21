package ru.terra.universal.client.game;

import org.apache.log4j.Logger;
import ru.terra.universal.shared.packet.server.OkPacket;


public class GameManager {
    private Logger log = Logger.getLogger(GameManager.class);


    private GameManager() {

    }

    private static GameManager instance = new GameManager();

    public static GameManager getInstance() {
        return instance;
    }


    public void login() {
    }

    public void start() {
        log.info("Starting game manager...");
    }

    public void sendSay(String message) {
    }

    public void playerSaid(long guid, String message) {
    }

    public void ok(OkPacket message) {
        log.info("ok");
    }

    public void serverSaid(String message) {
        GameView.getView().serverMessage(message);
    }
}
