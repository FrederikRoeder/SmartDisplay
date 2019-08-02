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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
        timeView = view.findViewById(R.id.textViewTime);
        calcCountdown();

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
        List<String> timer = new ArrayList<>();

        try {
            timer = serverConnection.getTimerList().execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        adapter.clear();
        adapter = new ArrayAdapter<>(getActivity(), R.layout.list_timer, timer);

        calcCountdown();
    }

    private void calcCountdown() {

        //todo: Countdown bis zum nächsten Alarm anzeigen

        if(adapter.isEmpty() || adapter.getItem(0).isEmpty()) return;
        String timer = adapter.getItem(0);

        String timerH = timer.substring(0, 2);
        int intTimerH = Integer.parseInt(timerH);
        String timerM = timer.substring(3, 5);
        int intTimerM = Integer.parseInt(timerM);
        String timerS = timer.substring(6);
        int intTimerS = Integer.parseInt(timerS);

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String time = dateFormat.format(date);

        String timeH = time.substring(0, 2);
        int intTimeH = Integer.parseInt(timeH);
        String timeM = time.substring(3, 5);
        int intTimeM = Integer.parseInt(timeM);
        String timeS = time.substring(6);
        int intTimeS = Integer.parseInt(timeS);

        int tempM = 0;
        int tempH = 0;
        if(intTimerS - intTimeS < 0) {
            intTimeS = (intTimerS + 60) - intTimeS;
            tempM = 1;
        } else {
            intTimeS = intTimerS - intTimeS;
        }
        if(intTimerM - intTimeM - tempM < 0) {
            intTimeM = (intTimerM + 60) - intTimeM - tempM;
            tempH = 1;
        } else {
            intTimeM = intTimerM - intTimeM - tempM;
        }
        if(intTimerH - intTimeH - tempH < 0) {
            intTimeH = (intTimerH + 24) - intTimeH - tempH;
        } else {
            intTimeH = intTimerH - intTimeH - tempH;
        }

        if(intTimeH < 10) {
            timeH = "0" + intTimeH;
        } else {
            timeH = "" + intTimeH;
        }
        if(intTimeM < 10) {
            timeM = "0" + intTimeM;
        } else {
            timeM = "" + intTimeM;
        }
        if(intTimeS < 10) {
            timeS = "0" + intTimeS;
        } else {
            timeS = "" + intTimeS;
        }
        String countdown = timeH + ":" + timeM + ":" + timeS;

        timeView.setText(countdown);

        if(intTimeH <= 0 && intTimeM <= 0 && intTimeS <=0) {
            updateTimerList();
        }
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
