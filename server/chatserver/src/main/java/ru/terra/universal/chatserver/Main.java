package ru.terra.universal.chatserver;

import org.apache.log4j.BasicConfigurator;
import ru.terra.universal.interserver.network.NetworkManager;

public class Main {

    public static void main(String[] args) {
        BasicConfigurator.configure();
        NetworkManager.getInstance().start(ChatWorker.class);
    }

}
