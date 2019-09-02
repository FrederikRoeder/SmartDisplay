package de.fhws.smartdisplay.game;

import android.support.v4.util.Pair;
import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static de.fhws.smartdisplay.game.ServerCmdType.CONNECTION_FAILED;
import static de.fhws.smartdisplay.game.ServerCmdType.EXIT;
import static de.fhws.smartdisplay.game.ServerCmdType.SEND_ID;
import static de.fhws.smartdisplay.game.ServerCmdType.SERVER_CONNECTION_LOST;

public class GameClient extends Thread {
    private static final String TAG = "GameClient";
    private static Charset utf8 = Charset.forName("UTF-8");
    private final GameClientCallback callback;
    private String clientId;
    private String globalReadBuffer = "";
    private Queue<ServerCmd> cmdQueue = new ConcurrentLinkedQueue<>();
    private Queue<ClientCmd> sendQueue = new ConcurrentLinkedQueue<>();

    public GameClient(GameClientCallback callback) {
        this.callback = callback;
    }

    private static void exitGame(SelectionKey sKey) throws IOException {
        sKey.channel().close();
        sKey.cancel();
    }

    private static boolean isExitCondition(Queue<ServerCmd> msg) {
        return msg.contains(ServerCmd.get(EXIT));
    }

    public void send(ClientCmd cmd) {
        sendQueue.add(cmd);
    }

    public void exit() {
        sendQueue.add(ClientCmd.EXIT_FROM_CLIENT_ID);
    }

    @Override
    public void run() {
        sendQueue.add(ClientCmd.CONNECTED);
        SocketChannel channel;
        Selector selector;
        try {
//            String hostAddress = InetAddress.getByName("GAMERZ").getHostAddress();
//            Log.d(TAG, "run: host:" + hostAddress);
            channel = SocketChannel.open();
            channel.socket().connect(new InetSocketAddress("10.31.16.198", 10000), 5000);
            channel.configureBlocking(false);
            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        } catch (IOException ex) {
            Log.e(TAG, "run: ", ex);
            informCallbackWith(CONNECTION_FAILED);
            return;
        }


        try {
            gameLoop:
            while (true) {
                int selectCount = selector.selectNow();
                if (selectCount < 1)
                    continue;

                Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                while (iter.hasNext()) {
                    SelectionKey sKey = iter.next();
                    iter.remove();

                    if (!sKey.isValid()) {
                        continue;
                    }

                    SocketChannel sock = (SocketChannel) sKey.channel();

                    if (!sKey.isValid()) {
                        exitGame(sKey);
                        return;
                    }

                    if (sKey.isReadable()) {
                        boolean handled = handleRead(sock);
                        if (!handled) {
                            informCallbackWith(SERVER_CONNECTION_LOST);
                            return;
                        }

                        Queue<ServerCmd> serverCmds = handleBuffer();

                        if (isExitCondition(serverCmds)) {
                            informCallbackWithAll(serverCmds);
                            exitGame(sKey);
                            break gameLoop;
                        }

                        setClientIdIfPresent(serverCmds);
                        informCallbackWithAll(serverCmds);
                    }

                    if (sKey.isWritable()) {
                        ClientCmd queueItem = sendQueue.poll();
                        if (isExitCondition(queueItem)) {
                            handleWrite(sock, queueItem);
                            exitGame(sKey);
                            break gameLoop;
                        }
                        handleWrite(sock, queueItem);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void setClientIdIfPresent(Queue<ServerCmd> serverCmds) {
        if (serverCmds.contains(ServerCmd.get(SEND_ID))) {
            for (ServerCmd item : serverCmds) {
                if (item.equals(ServerCmd.get(SEND_ID)))
                    clientId = item.getArg(0);
            }
        }
    }

    private boolean isExitCondition(ClientCmd queueItem) {
        return Objects.equals(queueItem, ClientCmd.EXIT_FROM_CLIENT_ID);
    }

    private void informCallbackWithAll(Queue<ServerCmd> serverCmds) {
        cmdQueue.addAll(serverCmds);
        callback.command(cmdQueue);
    }

    private void informCallbackWith(ServerCmdType serverConnectionLost) {
        cmdQueue.add(ServerCmd.get(serverConnectionLost));
        callback.command(cmdQueue);
    }

    private Queue<ServerCmd> handleBuffer() {
        Pair<Queue<ServerCmd>, Boolean> pair = ServerCmdParser.parseBuffer(globalReadBuffer);
        Queue<ServerCmd> commandQeueu = pair.first;
        boolean lastCmdComplete = pair.second;
        if (lastCmdComplete) {
            globalReadBuffer = "";
        } else {
            int fromIndexTillEnd = globalReadBuffer.lastIndexOf('\n') + 1;
            globalReadBuffer = globalReadBuffer.substring(fromIndexTillEnd);
        }

        return commandQeueu;
    }

    private void handleWrite(SocketChannel sock, ClientCmd queueItem) throws IOException {
        if (queueItem == null) {
            try {
                Thread.sleep(1); //save cpu power
            } catch (InterruptedException e) {
                return;
            }
            return;
        }
        ByteBuffer buffer;
        if (queueItem.equals(ClientCmd.EXIT_FROM_CLIENT_ID))
            buffer = utf8.encode(queueItem.asStr + "_" + clientId);
        else {
            buffer = utf8.encode(queueItem.asStr);
        }
        sock.write(buffer);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean handleRead(SocketChannel sock) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int read;
        try {
            read = sock.read(buffer);
            if (read == 0) {
                Thread.sleep(1); //save cpu power
            }
        } catch (IOException | InterruptedException e) {
            return false;
        }
        String str = new String(buffer.array(), 0, read, utf8);
        globalReadBuffer += str;
        return true;
    }

    public interface GameClientCallback {
        void command(Queue<ServerCmd> cmd);
    }

    static class QueueAddingThread extends Thread {

        private final Queue<String> queue;
        private final String[] messages;

        public QueueAddingThread(Queue<String> queue, String[] strings) {
            this.queue = queue;
            messages = strings;
        }

        @Override
        public void run() {
            for (String msge : messages) {
                try {
                    Thread.sleep(500);
                    queue.add(msge);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
