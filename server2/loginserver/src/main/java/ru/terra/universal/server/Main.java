package ru.terra.universal.server;

import org.apache.log4j.BasicConfigurator;
import ru.terra.universal.interserver.network.NetworkManager;
import ru.terra.universal.loginserver.LoginWorker;
import ru.terra.universal.shared.config.Config;

import static ru.terra.universal.shared.config.ConfigConstants.*;

public class Main {

    public static void main(String[] args) {
        BasicConfigurator.configure();
        NetworkManager.getInstance().start(LoginWorker.class, Config.getConfig().getValue(FRONTEND_IP, FRONTEND_IP_DEFAULT), FRONTEND_PORT);
    }

}
