package ru.terra.universal.client.gui;

import java.util.List;

public class GuiManager {
    private static GuiManager instance = new GuiManager();
    private LoginWindow loginWindow;

    private GuiManager() {
        loginWindow = new LoginWindow();
    }

    public static GuiManager getInstance() {
        return instance;
    }

    public void startLoginWindow() {
        loginWindow.setVisible(true);
    }

    public void publicWorlds(List<String> worlds) {
        loginWindow.publicWorlds( worlds);
    }
}
