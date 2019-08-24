package de.fhws.smartdisplay.view.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import de.fhws.smartdisplay.R;
import de.fhws.smartdisplay.server.ConnectionFactory;
import de.fhws.smartdisplay.server.ServerConnection;

public class GamesFragment extends Fragment {

    private ServerConnection serverConnection;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_games, container, false);

        serverConnection = new ConnectionFactory().buildConnection();

        setupSnake(view);
        setupGameTwo(view);
        setupGameThree(view);

        return view;
    }

    private void setupSnake(View view) {
        ImageButton buttonGameOne = view.findViewById(R.id.imageButtonGame1);
        buttonGameOne.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //todo: open game 1 (Snake)
            }
        });
    }

    private void setupGameTwo(View view) {
        ImageButton buttonGameTwo = view.findViewById(R.id.imageButtonGame2);
        buttonGameTwo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //todo: open game 2
            }
        });
    }

    private void setupGameThree(View view) {
        ImageButton buttonGameThree = view.findViewById(R.id.imageButtonGame3);
        buttonGameThree.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //todo: open game 3
            }
        });
    }
}
