package ru.terra.universal.shared.constants;

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
        public interface Login {
            public static final int CMSG_LOGIN = 2;
            public static final int CMSG_LOGOUT = 3;
            public static final int CSMG_BOOT_ME = 4;
        }

        public interface Char {
            public static final int CMSG_SELECT_SERVER = 11;
        }

        public interface Movement {
            public static final int CMSG_MOVE_START = 20;
            public static final int CMSG_MOVE = 21;
            public static final int CMSG_MOVE_STOP = 22;
        }

        public interface Chat {
            public static final int CMSG_SAY = 202;
            public static final int CMSG_WISP = 203;
        }


    }

    public interface Server {
        public static final int SMSG_OK = 501;
        public static final int SMSG_CHAR_BOOT = 502;
        public static final int SMSG_UPDATE = 503;
        public static final int SMSG_CHAT_MESSAGE = 504;
        public static final int SMSG_WORLD_STATE = 505;
    }

    public interface InterServer {
        public static final int ISMSG_HELLO = 10001;
        public static final int ISMSG_REG = 10002;
        public static final int ISMSG_UNREG = 10003;
        public static final int ISMSG_BOOT_CHAR = 10004;
        public static final int ISMSG_CHAR_REG = 10005;
        public static final int ISMSG_CHAR_IN_WORLD = 10006;
        public static final int ISMSG_REG_WORLD = 10007;
        public static final int ISMSG_UNREG_CHAR = 10008;
        public static final int ISMSG_UPDATE_CHAR = 10010;

    }
}
