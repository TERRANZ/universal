package ru.terra.universal.server;

import ru.terra.universal.frontend.network.ServerThread;
import ru.terra.universal.frontend.server.FrontEndServerWorker;
import ru.terra.universal.frontend.server.InterserverFEWorker;

public class Main {
    public static void main(String[] args) {
        new Thread(new ServerThread(12345, "0.0.0.0", FrontEndServerWorker.class)).start();
        new Thread(new ServerThread(12346, "0.0.0.0", InterserverFEWorker.class)).start();
    }
}
