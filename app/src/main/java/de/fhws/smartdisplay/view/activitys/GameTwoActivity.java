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

    @Override
    protected void onStart() {
        gameClient = new GameClient(new GameClient.GameClientCallback() {
            @Override
            public void command(Queue<ServerCmd> queue) {
                runOnUiThread(() -> {
                    ServerCmd cmd;
                    while ((cmd = queue.poll()) != null){
                        Log.d(TAG, "command: " + cmd.getType());
                        switch (cmd.getType()) {
                            case EXIT:
                                Toast.makeText(getApplicationContext(), "EXIT", Toast.LENGTH_SHORT).show();
                                connectButton.setVisibility(View.VISIBLE);
                                break;
                            case CONNECTION_FAILED:
                                Toast.makeText(getApplicationContext(), "CONNECTION_FAILED", Toast.LENGTH_SHORT).show();
                                break;
                            case PLAYER_ID_DEAD:
                                String deadId = cmd.getArg(0);
                                Toast.makeText(getApplicationContext(), "PLAYER_ID_DEAD: " + deadId, Toast.LENGTH_SHORT).show();
                                break;
                            case SEND_ID:
                                String id = cmd.getArg(0);
                                connectButton.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "your id: " + id, Toast.LENGTH_SHORT).show();
                                break;
                            case SERVER_CONNECTION_LOST:
                                connectButton.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(), "SERVER_CONNECTION_LOST", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
            }
        });
        super.onStart();
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
                serverConnection.startPong().enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.isSuccessful()) {
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                public void run() {
                                    changePoints("0");
                                    gameClient.start();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });
    }

    private void setupCloseButton() {
        closeButton = findViewById(R.id.imageButtonClosePong);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameClient.exit();
                finish();
            }
        });
    }

    private void setupUpButton() {
        ImageButton upButton = findViewById(R.id.imageButtonUpPong);
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameClient.send(ClientCmd.UP);
            }
        });
    }

    private void setupDownButton() {
        ImageButton downButton = findViewById(R.id.imageButtonDownPong);
        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameClient.send(ClientCmd.DOWN);
            }
        });
    }

    private void changePoints(String pionts) {
        textViewPoints.setText("Punkte: " + pionts);
    }
}
