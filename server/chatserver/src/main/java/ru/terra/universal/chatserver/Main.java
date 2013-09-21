package ru.terra.universal.chatserver;

import org.apache.log4j.BasicConfigurator;
import ru.terra.universal.interserver.network.NetworkManager;
import ru.terra.universal.shared.config.Config;
import ru.terra.universal.shared.config.ConfigConstants;

public class Main {

    public static void main(String[] args) {
        BasicConfigurator.configure();
        NetworkManager.getInstance().start(ChatWorker.class,
                Config.getConfig().getValue(ConfigConstants.FRONTEND_IP, ConfigConstants.FRONTEND_IP_DEFAULT),
                12346);
    }

}
