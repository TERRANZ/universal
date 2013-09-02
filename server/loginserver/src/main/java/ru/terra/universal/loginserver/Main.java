package ru.terra.universal.loginserver;

import org.apache.log4j.BasicConfigurator;
import ru.terra.universal.interserver.network.NetworkManager;

public class Main {

    public static void main(String[] args) {
        BasicConfigurator.configure();
        NetworkManager.getInstance().start(LoginWorker.class, "127.0.0.1", 12346);
    }

}
