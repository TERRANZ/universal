package ru.terra.universal.shared.constants;

public interface OpCodes {

    int ISOpCodesStart = 10000;
    int LoginOpcodeStart = 1;
    int LoginOpcodeEnd = 10;
    int CharOpcodeStart = 11;
    int CharOpcodeEnd = 100;
    int WorldOpcodeStart = 101;
    int WorldOpcodeEnd = 200;
    int ChatOpcodeStart = 201;
    int ChatOpcodeEnd = 300;


    interface Client {
        interface Login {
            int CMSG_LOGIN = 2;
            int CMSG_LOGOUT = 3;
            int CSMG_BOOT_ME = 4;
        }

        interface Char {
            int CMSG_SELECT_SERVER = 11;
        }

        interface Movement {
            int CMSG_MOVE_START = 102;
            int CMSG_MOVE = 103;
            int CMSG_MOVE_STOP = 104;
            int CMSG_MOVE_TELEPORT = 105;
            int CMSG_MOVE_FORWARD = 106;
            int CMSG_MOVE_BACK = 107;
            int CMSG_MOVE_LEFT = 108;
            int CMSG_MOVE_RIGHT = 109;
        }

        interface Chat {
            int CMSG_SAY = 202;
            int CMSG_WISP = 203;
        }


    }

    interface Server {
        int SMSG_OK = 501;
        int SMSG_CHAR_BOOT = 502;
        int SMSG_UPDATE = 503;
        int SMSG_CHAT_MESSAGE = 504;
        int SMSG_WORLD_STATE = 505;

        interface Login {
            int SMSG_LOGIN_FAILED = 506;
        }
    }

    interface InterServer {
        int ISMSG_HELLO = 10001;
        int ISMSG_REG = 10002;
        int ISMSG_UNREG = 10003;
        int ISMSG_BOOT_CHAR = 10004;
        int ISMSG_CHAR_REG = 10005;
        int ISMSG_CHAR_IN_WORLD = 10006;
        int ISMSG_REG_WORLD = 10007;
        int ISMSG_UNREG_CHAR = 10008;
        int ISMSG_UPDATE_CHAR = 10010;
    }
}
