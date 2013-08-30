package ru.terra.universal.client;

import org.apache.log4j.BasicConfigurator;
import ru.terra.universal.interserver.network.NetworkManager;

/**
 * User: Vadim Korostelev
 * Date: 30.08.13
 * Time: 16:15
 */
public class Main {
    public static void main(String[] args) {
        BasicConfigurator.configure();
        NetworkManager.getInstance().start(ClientWorker.class, "127.0.0.1", 12345);
    }
}
