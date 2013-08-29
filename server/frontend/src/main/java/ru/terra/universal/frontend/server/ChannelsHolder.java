package ru.terra.universal.frontend.server;

public class ChannelsHolder {
    private static ChannelsHolder instance = new ChannelsHolder();    
    
    private ChannelsHolder() {
    }

    public static ChannelsHolder getInstance() {
	return instance;
    }
}
