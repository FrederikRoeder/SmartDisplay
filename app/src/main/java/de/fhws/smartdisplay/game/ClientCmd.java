package de.fhws.smartdisplay.game;

public enum ClientCmd {
    UP("w"),
    DOWN("s"),
    LEFT("a"),
    RIGHT("d"),
    CONNECTED("connected"),
    EXIT_FROM_CLIENT_ID("client_EXIT_FROM_ID");

    public String asStr;

    ClientCmd(String str) {

        this.asStr = str;
    }
}
