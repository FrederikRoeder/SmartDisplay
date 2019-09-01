package de.fhws.smartdisplay.game;

import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

class ServerCmdParser {
    static Pair<Queue<ServerCmd>, Boolean> parseBuffer(String buffer) {
        boolean lastCmdComplete = isLastCmdComplete(buffer);

        List<String> cmdstrs = getBufferAsStringList(buffer);

        if (!lastCmdComplete)
            cmdstrs.remove(cmdstrs.size() - 1);

        cmdstrs = removePrefix(cmdstrs);
        Queue<ServerCmd> parsedCmds = parseStrCmdList(cmdstrs);

        return new Pair<>(parsedCmds, lastCmdComplete);
    }

    private static LinkedList<String> getBufferAsStringList(String buffer) {
        return new LinkedList<>(Arrays.asList(buffer.split(ServerCmd.postfix)));
    }

    private static boolean isLastCmdComplete(String buffer) {
        return buffer.charAt(buffer.length() - 1) == '\n';
    }

    private static Queue<ServerCmd> parseStrCmdList(List<String> cmdstrs) {
        Queue<ServerCmd> tmp = new ConcurrentLinkedQueue<>();
        for (String cmdstr : cmdstrs) {
            tmp.add(parse(cmdstr));
        }
        return tmp;
    }

    private static List<String> removePrefix(List<String> cmdstrs) {
        List<String> tmp = new ArrayList<>();
        for (String cmdstr : cmdstrs) {
            tmp.add(cmdstr.replaceFirst(ServerCmd.prefix, ""));
        }
        return tmp;
    }

    private static ServerCmd parse(String strCmdWithoutPostfix) {
        return getServerCmdFromStr(strCmdWithoutPostfix);
    }

    private static ServerCmd getServerCmdFromStr(String strCmdWithoutPostfix) {
        for (ServerCmdType cmdType : ServerCmdType.values()) {
            if (strCmdWithoutPostfix.startsWith(cmdType.name())) {
                ServerCmd cmd = new ServerCmd(cmdType);
                cmd.addArgs(extractArgs(strCmdWithoutPostfix, cmdType));
                return cmd;
            }
        }
        throw new IllegalArgumentException("argument '" + strCmdWithoutPostfix + "' not recognized as cmd");
    }

    private static String[] extractArgs(String strCmd, ServerCmdType enumm) {
        String name = enumm.name();
        String args;
        args = strCmd.replaceFirst(name, "");
        args = args.replaceFirst("_", "");
        String[] s = args.split("_");
        if (Objects.equals(s[0], ""))
            return new String[]{};
        return s;
    }
}
