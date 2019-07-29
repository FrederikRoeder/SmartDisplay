package de.fhws.smartdisplay.view.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.fhws.smartdisplay.R;
import de.fhws.smartdisplay.server.ConnectionFactory;
import de.fhws.smartdisplay.server.ServerConnection;
import de.fhws.smartdisplay.view.popups.TimerPopup;

public class TimerFragment extends Fragment implements TimerPopup.DialogListener {

    private ArrayAdapter<String> adapter;
    private ServerConnection serverConnection;

    private TextView timeView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_timer, container, false);

        serverConnection = new ConnectionFactory().buildConnection();

        setupTimerList(view);
        setupTimeView(view);

        FloatingActionButton addTimer = view.findViewById(R.id.floatingActionButtonTimer);
        addTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimerPopup();
            }
        });

        ImageButton refreshButton = view.findViewById(R.id.imageButtonRefreshTimer);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                updateTimerList();
            }
        });

        return view;
    }

    private void setupTimerList(View view) {
        //todo: sortierte Timer vom Server ziehen und in "timer" (next line) speichern
        List<String> timer = new ArrayList<>();

        try {
            timer = serverConnection.getTimerList().execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ListView timerList = view.findViewById(R.id.timerList);

        adapter = new ArrayAdapter<>(getActivity(), R.layout.list_timer, timer);
        timerList.setAdapter(adapter);
        timerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        timerList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String timer = (String) parent.getItemAtPosition(position);
                //todo: Server den zu löschenden "timer" schicken
                try {
                    serverConnection.deleteTimer(timer).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                updateTimerList();
                Toast.makeText(getContext(), "Timer gelöscht", Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }

    private void updateTimerList() {
        //todo: aktualisierte und sortierte Timer vom Server ziehen und in "timer" (next line) speichern
        List<String> timer = new ArrayList<>();

        try {
            timer = serverConnection.getTimerList().execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        adapter.clear();
        adapter = new ArrayAdapter<>(getActivity(), R.layout.list_timer, timer);

        updateTimeView();
    }

    private void setupTimeView(View view) {
        timeView = view.findViewById(R.id.textViewTime);
        //todo: Countdown bis zum nächsten Alarm anzeigen
    }

    private void updateTimeView() {
        //timeVieew.
    }

    private void openTimerPopup() {
        TimerPopup timerPopup = TimerPopup.newInstance();
        timerPopup.setTargetFragment(this, 0);
        timerPopup.show(getActivity().getSupportFragmentManager(), "TimerPopup");
    }

    @Override
    public void updateResult() {
        updateTimerList();
    }
}
