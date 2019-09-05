package de.fhws.smartdisplay.game;

public enum ServerCmdType {
    EXIT(false),
    CONNECTION_FAILED(false),
    PLAYER_ID_DEAD(true),
    PLAYER_ID_POINT(true),
    SEND_ID(true),
    SERVER_CONNECTION_LOST(false);

    boolean canHaveArguments;

    ServerCmdType(boolean hasArguments) {
        this.canHaveArguments = hasArguments;
    }
}
