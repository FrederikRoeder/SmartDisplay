package de.fhws.smartdisplay.view.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import de.fhws.smartdisplay.server.CustomeCallback;
import de.fhws.smartdisplay.server.ServerConnection;
import de.fhws.smartdisplay.view.popups.TimerPopup;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimerFragment extends Fragment implements TimerPopup.DialogListener {

    private ArrayAdapter<String> adapter;
    private ServerConnection serverConnection;

    private TextView timeView;
    private ListView timerList;
    private List<String> timer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_timer, container, false);

        serverConnection = new ConnectionFactory().buildConnection();

        timerList = view.findViewById(R.id.timerList);
        timeView = view.findViewById(R.id.textViewTime);

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupTimerList();
        calcCountdown();
    }

    private void setupTimerList() {
        timer = new ArrayList<>();

//        getRequestGeneric(serverConnection.getTimerList(), new CustomeCallback<List<String>>() {
//            @Override
//            public void onResponse(List<String> value) {
//                timer = value;
//            }
//
//            @Override
//            public void onFailure() {
//            }
//        });

        serverConnection.getTimerList().enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, final Response<List<String>> response) {
                if(response.isSuccessful()) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            timer = response.body();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });

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
                serverConnection.deleteTimer(timer).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
                updateTimerList();
                Toast.makeText(getContext(), "Timer gelöscht", Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }

    private void updateTimerList() {
        timer = new ArrayList<>();

//        getRequestGeneric(serverConnection.getTimerList(), new CustomeCallback<List<String>>() {
//            @Override
//            public void onResponse(List<String> value) {
//                timer = value;
//            }
//
//            @Override
//            public void onFailure() {
//            }
//        });

        serverConnection.getTimerList().enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, final Response<List<String>> response) {
                if(response.isSuccessful()) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            timer = response.body();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });

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

//    public <T> void getRequestGeneric(Call<T> call, final CustomeCallback<T> callback){
//        call.enqueue(new Callback<T>() {
//            @Override
//            public void onResponse(Call<T> call, Response<T> response) {
//                callback.onResponse(response.body());
//            }
//
//            @Override
//            public void onFailure(Call<T> call, Throwable t) {
//                callback.onFailure();
//            }
//        });
//    }

    @Override
    public void updateResult() {
        updateTimerList();
    }
}
