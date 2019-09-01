package de.fhws.smartdisplay.view.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Queue;

import de.fhws.smartdisplay.R;
import de.fhws.smartdisplay.game.ClientCmd;
import de.fhws.smartdisplay.game.GameClient;
import de.fhws.smartdisplay.game.ServerCmd;

public class GameOneActivity extends AppCompatActivity {

    private static final String TAG = "GameOneActivity";
    private GameClient gameClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_one);

        setupCloseButton();
        setupUpButton();
        setupDownButton();
        setupLeftButton();
        setupRightButton();
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
                                Toast.makeText(getApplicationContext(), "your id: " + id, Toast.LENGTH_SHORT).show();
                                break;
                            case SERVER_CONNECTION_LOST:
                                Toast.makeText(getApplicationContext(), "SERVER_CONNECTION_LOST", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
            }
        });
        gameClient.start();
        super.onStart();
    }

    private void setupCloseButton() {
        ImageButton closeButton = findViewById(R.id.imageButtonCloseSnake);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private  void setupUpButton() {
        ImageButton upButton = findViewById(R.id.imageButtonUpSnake);
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameClient.send(ClientCmd.UP);
            }
        });
    }

    private  void setupDownButton() {
        ImageButton downButton = findViewById(R.id.imageButtonDownSnake);
        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameClient.send(ClientCmd.DOWN);
            }
        });
    }

    private  void setupLeftButton() {
        ImageButton leftButton = findViewById(R.id.imageButtonLeftSnake);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameClient.send(ClientCmd.LEFT);
            }
        });
    }

    private  void setupRightButton() {
        ImageButton rightButton = findViewById(R.id.imageButtonRightSnake);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameClient.send(ClientCmd.RIGHT);
            }
        });
    }
}
