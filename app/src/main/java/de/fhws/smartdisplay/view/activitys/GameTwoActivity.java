package de.fhws.smartdisplay.view.activitys;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Queue;
import java.util.concurrent.TimeUnit;

import de.fhws.smartdisplay.R;
import de.fhws.smartdisplay.game.ClientCmd;
import de.fhws.smartdisplay.game.GameClient;
import de.fhws.smartdisplay.game.ServerCmd;
import de.fhws.smartdisplay.server.ConnectionFactory;
import de.fhws.smartdisplay.server.ServerConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameTwoActivity extends AppCompatActivity {

    private ServerConnection serverConnection;

    private static final String TAG = "GameTwoActivity";
    private GameClient gameClient;

    private TextView textViewPoints;
    private Button connectButton;
    private ImageButton closeButton;
    private boolean lock = false;
    private String playerId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_two);

        serverConnection = new ConnectionFactory().buildConnection();

        setupTextViewPoints();
        setupConnectButton();
        setupCloseButton();
        setupUpButton();
        setupDownButton();
    }

    private void setupTextViewPoints() {
        textViewPoints = findViewById(R.id.textViewPointsPong);
        changePoints("0");
    }

    private void setupConnectButton() {
        connectButton = findViewById(R.id.buttonConnectPong);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!lock) {
                    setLock();
                    serverConnection.startPong().enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()) {
                                Handler handler = new Handler(Looper.getMainLooper());
                                handler.post(new Runnable() {
                                    public void run() {
                                        if (gameClient != null) {
                                            gameClient.exit();
                                        }
                                        changePoints("0");
                                        createGameClient();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Serververbindung konnte nicht hergestellt werden", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    private void setupCloseButton() {
        closeButton = findViewById(R.id.imageButtonClosePong);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gameClient != null) {
                    gameClient.exit();
                }
                finish();
            }
        });
    }

    private void setupUpButton() {
        ImageButton upButton = findViewById(R.id.imageButtonUpPong);
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gameClient != null) {
                    gameClient.send(ClientCmd.UP);
                }
            }
        });
    }

    private void setupDownButton() {
        ImageButton downButton = findViewById(R.id.imageButtonDownPong);
        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gameClient != null) {
                    gameClient.send(ClientCmd.DOWN);
                }
            }
        });
    }

    private void createGameClient() {
        gameClient = new GameClient(new GameClient.GameClientCallback() {
            @Override
            public void command(Queue<ServerCmd> queue) {
                runOnUiThread(() -> {
                    ServerCmd cmd;
                    while ((cmd = queue.poll()) != null){
                        Log.d(TAG, "command: " + cmd.getType());
                        switch (cmd.getType()) {
                            case EXIT:
                                connectButton.setVisibility(View.VISIBLE);
                                break;
                            case CONNECTION_FAILED:
                                Toast.makeText(getApplicationContext(), "Spielverbindung konnte nicht hergestellt werden", Toast.LENGTH_SHORT).show();
                                connectButton.setVisibility(View.VISIBLE);
                                break;
                            case PLAYER_ID_DEAD:
                                String deadId = cmd.getArg(0);
                                break;
                            case PLAYER_ID_POINT:
                                String id = cmd.getArg(0);
                                if (id.equals(playerId)) {
                                    String points = cmd.getArg(1);
                                    changePoints(points);
                                }
                                break;
                            case SEND_ID:
                                playerId = cmd.getArg(0);
                                connectButton.setVisibility(View.GONE);
                                break;
                            case SERVER_CONNECTION_LOST:
                                connectButton.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(), "Spielverbindung unterbrochen", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
            }
        });
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        gameClient.start();
    }

    private void changePoints(String pionts) {
        textViewPoints.setText("Punkte: " + pionts);
    }

    private void setLock() {
        lock = true;
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        lock = false;
                    }
                },
                3000
        );
    }
}
