package de.fhws.smartdisplay.view.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import de.fhws.smartdisplay.R;
import de.fhws.smartdisplay.view.activitys.GameOneActivity;
import de.fhws.smartdisplay.view.activitys.GameTwoActivity;;

public class GamesFragment extends Fragment {

    SharedPreferences settings;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_games, container, false);

        settings = this.getActivity().getSharedPreferences("Settings", 0);

        setupSnake(view);
        setupPong(view);
        setupTetris(view);

        return view;
    }

    private void setupSnake(View view) {
        ImageButton buttonGameOne = view.findViewById(R.id.imageButtonGame1);
        buttonGameOne.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GameOneActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupPong(View view) {
        ImageButton buttonGameTwo = view.findViewById(R.id.imageButtonGame2);
        buttonGameTwo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GameTwoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupTetris(View view) {
        ImageButton buttonGameThree = view.findViewById(R.id.imageButtonGame3);
        buttonGameThree.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //todo: open game 3
            }
        });
    }
}
