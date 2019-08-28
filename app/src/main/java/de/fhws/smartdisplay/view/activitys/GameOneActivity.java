package de.fhws.smartdisplay.view.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import de.fhws.smartdisplay.R;

public class GameOneActivity extends AppCompatActivity {

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

            }
        });
    }

    private  void setupDownButton() {
        ImageButton downButton = findViewById(R.id.imageButtonDownSnake);
        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private  void setupLeftButton() {
        ImageButton leftButton = findViewById(R.id.imageButtonLeftSnake);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private  void setupRightButton() {
        ImageButton rightButton = findViewById(R.id.imageButtonRightSnake);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
