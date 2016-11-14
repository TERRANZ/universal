package ru.terra.universal.client.game.jmonkey;

import ru.terra.universal.client.game.GameView;
import ru.terra.universal.client.game.entity.Entity;
import ru.terra.universal.client.game.entity.MapObject;
import ru.terra.universal.client.game.entity.Player;

public class JMonkeyGameView extends GameView {
	private JMEGameViewImpl jmeGameViewImpl;

	private static JMonkeyGameView instance = new JMonkeyGameView();

	private JMonkeyGameView() {
		jmeGameViewImpl = new JMEGameViewImpl();
		jmeGameViewImpl.start();
	}

	public static GameView getInstance() {
		return instance;
	}

	@Override
	public void init() {
	}

	@Override
	public void loadPlayer() {
		jmeGameViewImpl.loadPlayer();
	}

	@Override
	public void addMapObject(MapObject entity) {
		jmeGameViewImpl.addMapObject(entity);
	}

	@Override
	public void enemyLoggedIn(Player enemy) {
		jmeGameViewImpl.enemyLoggedIn(enemy);
	}

	@Override
	public void updateEntityPosition(Entity entity) {
		jmeGameViewImpl.updateEntityPosition(entity);
	}

	@Override
	public void entityVectorMove(Entity entity, float x, float y, float z, float h, boolean stop) {
		jmeGameViewImpl.entityVectorMove(entity.getGuid(), x, y, z, h, stop);
	}

	@Override
	public void serverMessage(String message) {
		jmeGameViewImpl.serverMessage(message);

	}

	@Override
	public void playerSay(Player player, String message) {
		jmeGameViewImpl.playerSay(player, message);
	}
}
