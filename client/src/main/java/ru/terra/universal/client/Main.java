package ru.terra.universal.client;

import org.apache.log4j.BasicConfigurator;
import ru.terra.universal.client.test.ClientTest;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        BasicConfigurator.configure();
//        GuiManager.getInstance().startLoginWindow();
        for (int i = 0; i < 10; i++) {
            new ClientTest().start("127.0.0.1");
            Thread.sleep(200);
        }
    }
}
