package ru.terra.universal.loginserver;

import org.apache.log4j.BasicConfigurator;

import ru.terra.universal.interserver.network.NetworkManager;
import ru.terra.universal.loginserver.network.LoginWorker;

public class Main {

    public static void main(String[] args) {
	BasicConfigurator.configure();
	NetworkManager.getInstance().start(LoginWorker.class);
    }

}
