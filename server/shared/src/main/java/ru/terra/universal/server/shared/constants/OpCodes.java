package ru.terra.universal.server.shared.constants;

public interface OpCodes {

    public static final int ISOpCodesStart = 10000;
    public static final int LoginOpcodeStart = 1;
    public static final int LoginOpcodeEnd = 10;
    public static final int CharOpcodeStart = 11;
    public static final int CharOpcodeEnd = 100;
    public static final int WorldOpcodeStart = 101;
    public static final int WorldOpcodeEnd = 200;
    public static final int ChatOpcodeStart = 201;
    public static final int ChatOpcodeEnd = 300;
    

    public interface Client {
	public static final int CMSG_LOGIN = 1;
	public static final int CMSG_SAY = 11;
	public static final int CMSG_WISP = 12;
	public static final int CMSG_LOGOUT = 13;
	public static final int CMSG_PLAYER_INFO_REQUEST = 14;
    }

    public interface Server {
	public static final int SMSG_OK = 501;
	public static final int SMSG_SAY = 502;
	public static final int SMSG_PLAYER_LOGGED_IN = 503;
	public static final int SMSG_PLAYER_LOGGED_OUT = 504;
	public static final int SMSG_ENTITY_ADD = 509;
	public static final int SMSG_ENTITY_DEL = 510;
	public static final int SMSG_PLAYER_IN_GAME = 511;
	public static final int SMSG_PLAYER_INFO = 512;
	public static final int SMSG_MESSAGE = 513;
    }

    public interface InterServer {
	public static final int ISMSG_HELLO = 10001;
	public static final int ISMSG_REG = 10002;
	public static final int ISMSG_UNREG = 10003;
    }
}
