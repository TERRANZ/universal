package ru.terra.universal.server;

import ru.terra.universal.charserver.CharWorker;
import ru.terra.universal.interserver.network.NetworkManager;
import ru.terra.universal.shared.config.Config;

import static ru.terra.universal.shared.config.ConfigConstants.*;

public class Main {

    public static void main(String[] args) {
        NetworkManager.getInstance().start(CharWorker.class, Config.getConfig().getValue(FRONTEND_IP, FRONTEND_IP_DEFAULT), FRONTEND_PORT);
    }

}
