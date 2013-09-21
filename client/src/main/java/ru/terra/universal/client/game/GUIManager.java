package ru.terra.universal.client.game;

public class GUIManager
{
	private static GUIManager instance = new GUIManager();

	public static GUIManager getInstance()
	{
		return instance;
	}

	private GUIManager()
	{
	}

	public void start()
	{
		GameView gameView = GameView.getView();
		gameView.init();
	}
}
