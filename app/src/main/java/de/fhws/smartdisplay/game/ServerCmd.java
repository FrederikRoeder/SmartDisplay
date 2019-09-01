package de.fhws.smartdisplay.game;

public class ServerCmd {
    static final String prefix = "server_";
    static final String postfix = "\n";
    private final String name;
    private final ServerCmdType type;
    private String[] args;
    ServerCmd(ServerCmdType type) {
        this.name = type.name();
        this.type = type;
        this.args = new String[]{};
    }

    static ServerCmd get(ServerCmdType type) {
        return new ServerCmd(type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        if (o.getClass().equals(ServerCmdType.class))
            return type.equals(o);

        if (o.getClass() != getClass())
            return false;

        ServerCmd serverCmd = (ServerCmd) o;
        return type == serverCmd.type;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    public ServerCmdType getType() {
        return type;
    }

    public String getArg(int i) {
        if (args.length == 0)
            throw new IllegalArgumentException("The enum " + this.name + " does not contain any arguments");
        return args[i];
    }

    void addArgs(String[] allArgs) {
        if (!this.type.canHaveArguments && allArgs.length > 0) {
            throw new UnsupportedOperationException("the ServerCmd type '" + this.type + "' cannot have any arguments");
        }
        this.args = allArgs;
    }
}

