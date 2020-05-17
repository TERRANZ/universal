package ru.terra.universal.loginserver;

import java.util.Date;
import org.apache.log4j.Logger;
import ru.terra.universal.interserver.network.netty.InterserverWorker;
import ru.terra.universal.shared.constants.OpCodes;
import ru.terra.universal.shared.constants.OpCodes.InterServer;
import ru.terra.universal.shared.entity.AccountInfo;
import ru.terra.universal.shared.packet.AbstractPacket;
import ru.terra.universal.shared.packet.client.LoginPacket;
import ru.terra.universal.shared.packet.interserver.BootCharPacket;
import ru.terra.universal.shared.packet.interserver.CharRegPacket;
import ru.terra.universal.shared.packet.interserver.HelloPacket;
import ru.terra.universal.shared.packet.interserver.RegisterPacket;
import ru.terra.universal.shared.packet.server.OkPacket;
import ru.terra.universal.shared.persistance.CharLoader;
import ru.terra.universal.shared.persistance.CharSaver;
import ru.terra.universal.shared.persistance.impl.JsonCharLoaderImpl;
import ru.terra.universal.shared.persistance.impl.JsonCharSaverImpl;
import ru.terra.universal.shared.util.CryptoUtil;

public class LoginWorker extends InterserverWorker {

    private Logger logger = Logger.getLogger(this.getClass());
    private CharLoader charLoader = new JsonCharLoaderImpl();
    private CharSaver charSaver = new JsonCharSaverImpl();

    @Override
    public void disconnectedFromChannel() {
        logger.info("Frontend disconnected us");
    }

    @Override
    public void acceptPacket(AbstractPacket packet) {
        try {
            final Long sender = packet.getSender();
            switch (packet.getOpCode()) {
                case InterServer.ISMSG_HELLO: {
                    final HelloPacket helloPacket = new HelloPacket();
                    helloPacket.setHello("login server");
                    final RegisterPacket registerPacket = new RegisterPacket();
                    registerPacket.setStartRange(OpCodes.LoginOpcodeStart);
                    registerPacket.setEndRange(OpCodes.LoginOpcodeEnd);
                    networkManager.sendPacket(helloPacket);
                    networkManager.sendPacket(registerPacket);
                }
                break;
                case OpCodes.Client.Login.CMSG_LOGIN: {
                    final LoginPacket loginPacket = (LoginPacket) packet;
                    logger.info("Client with login " + loginPacket.getLogin() + " and pass " + loginPacket.getPassword()
                        + " attempting to log in");
                    final AccountInfo accountInfo = charLoader
                        .findAccount(loginPacket.getLogin(), loginPacket.getPassword()).orElseGet(() -> {
                            final AccountInfo newAccountInfo = new AccountInfo(
                                loginPacket.getLogin(),
                                CryptoUtil.encryptMD5(loginPacket.getPassword()),
                                false,
                                new Date(),
                                0L,
                                0,
                                new Date().getTime()
                            );
                            charSaver.save(newAccountInfo);
                            return newAccountInfo;
                        });

                    logger.info("Client with id " + sender + " logged in");
                    logger.info("Client registered with GUID = " + accountInfo.getUid());

                    final CharRegPacket charRegPacket = new CharRegPacket(accountInfo.getUid());
                    charRegPacket.setOldId(sender);
                    networkManager.sendPacket(charRegPacket);
                    networkManager.sendPacket(new OkPacket(accountInfo.getUid()));
                }
                break;
                case OpCodes.Client.Login.CSMG_BOOT_ME: {
                    logger.info("Client sent Boot Me to us");
                    networkManager.sendPacket(new BootCharPacket(sender));
                }
                break;
                case InterServer.ISMSG_UNREG_CHAR: {
                    logger.info("Unregistering char with uid = " + sender);
                }
                break;
            }
        } catch (Exception e) {
            logger.error("Error while processing packet", e);
        }
    }

}
