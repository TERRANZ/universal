package ru.terra.universal.client.network;

import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.terra.universal.client.game.GameStateHolder;
import ru.terra.universal.interserver.network.NetworkManager;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.packet.client.BootMePacket;
import ru.terra.universal.shared.packet.client.LoginPacket;
import ru.terra.universal.shared.packet.server.LoginFailedPacket;
import ru.terra.universal.shared.packet.server.OkPacket;
import ru.terra.universal.shared.packet.server.WorldIsNotAvail;
import ru.terra.universal.shared.packet.server.WorldStatePacket;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ClientWorkerTest {

    private static final long GUID = 777L;

    private final ClientWorker worker = new ClientWorker();
    private EmbeddedChannel channel;

    @BeforeEach
    void setUp() {
        channel = new EmbeddedChannel();
        NetworkManager.getInstance().setChannel(channel);
        GUIDHOlder.getInstance().setGuid(0L);
        GameStateHolder.getInstance().setNotifier(null);
        GameStateHolder.getInstance().setGameState(GameStateHolder.GameState.INIT);
    }

    @Test
    void answersTheServerHelloWithALoginRequest() {
        worker.acceptPacket(new OkPacket(GUID));

        assertEquals(GameStateHolder.GameState.LOGIN, GameStateHolder.getInstance().getGameState());
        assertEquals(GUID, GUIDHOlder.getInstance().getGuid());

        final LoginPacket login = channel.readOutbound();
        assertNotNull(login);
        assertEquals(OpCodes.Client.Login.CMSG_LOGIN, login.getOpCode());
        assertEquals(GUID, login.getSender());
        assertEquals("mylogin", login.getLogin());
        assertEquals("mypass", login.getPassword());
        assertNull(channel.readOutbound(), "the hello must produce exactly one packet");
    }

    @Test
    void requestsACharacterBootAfterLoginWasAccepted() {
        GameStateHolder.getInstance().setGameState(GameStateHolder.GameState.LOGIN);

        worker.acceptPacket(new OkPacket(GUID));

        assertEquals(GameStateHolder.GameState.LOGGED_IN, GameStateHolder.getInstance().getGameState());
        assertEquals(GUID, GUIDHOlder.getInstance().getGuid());

        final BootMePacket bootMe = channel.readOutbound();
        assertNotNull(bootMe);
        assertEquals(OpCodes.Client.Login.CSMG_BOOT_ME, bootMe.getOpCode());
        assertEquals(GUID, bootMe.getSender());
        assertNull(channel.readOutbound(), "the login acknowledgement must produce exactly one packet");
    }

    @Test
    void ignoresARepeatedOkInLoggedInState() {
        GameStateHolder.getInstance().setGameState(GameStateHolder.GameState.LOGGED_IN);

        worker.acceptPacket(new OkPacket(GUID));

        assertEquals(GameStateHolder.GameState.LOGGED_IN, GameStateHolder.getInstance().getGameState());
        assertNull(channel.readOutbound());
    }

    @Test
    void handlesALoginFailureWithoutSendingAnything() {
        final LoginFailedPacket failed = new LoginFailedPacket();
        failed.setReason("bad password");

        assertDoesNotThrow(() -> worker.acceptPacket(failed));
        assertNull(channel.readOutbound());
    }

    @Test
    void handlesAnUnavailableWorldWithoutSendingAnything() {
        assertDoesNotThrow(() -> worker.acceptPacket(new WorldIsNotAvail(1L, "map_0")));
        assertNull(channel.readOutbound());
    }

    @Test
    void handlesAnEmptyWorldState() {
        assertDoesNotThrow(() -> worker.acceptPacket(new WorldStatePacket(List.of(), List.of())));
        assertNull(channel.readOutbound());
    }
}
