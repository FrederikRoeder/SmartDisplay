package de.fhws.smartdisplay.server;

public class ServerConfig {
    public static final String IP_WEBSERVER = "192.168.1.124";

    public static final String IP_GAMESERVER = IP_WEBSERVER;
    public static final int PORT_WEBSERVER = 5000;
    public static final int PORT_GAMESERVER = 10000;
    public static final String URL_WEBSERVER = "http://"+ IP_WEBSERVER + ":" + PORT_WEBSERVER + "/";
}
