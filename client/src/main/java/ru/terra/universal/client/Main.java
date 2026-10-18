package ru.terra.universal.client;

import ru.terra.universal.client.gui.GuiManager;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        GuiManager.getInstance().startLoginWindow();
//        for (int i = 0; i < 10; i++) {
//            new ClientTest().start("127.0.0.1");
//            Thread.sleep(200);
//        }
    }
}
