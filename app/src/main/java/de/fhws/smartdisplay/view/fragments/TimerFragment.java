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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.fhws.smartdisplay.R;
import de.fhws.smartdisplay.server.ConnectionFactory;
import de.fhws.smartdisplay.server.ServerConnection;
import de.fhws.smartdisplay.view.popups.TimerPopup;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimerFragment extends Fragment implements TimerPopup.DialogListener {

    private ServerConnection serverConnection;
    private ArrayAdapter<String> adapter;
    private Timer updateTimer;
    private Timer countdownTimer;

    private TextView timeView;
    private ListView timerList;
    private List<String> timer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_timer, container, false);

        serverConnection = new ConnectionFactory().buildConnection();

        timer = new ArrayList<>();
        adapter = new ArrayAdapter<>(getActivity(), R.layout.list_todo, timer);

        timeView = view.findViewById(R.id.textViewTime);

        setupTimerList(view);
        setupAddButton(view);
        setupRefreshButton(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateTimerList();

        countdownTimer = new Timer();
        countdownTimer.schedule(new CountdownTimer(), 0, 1000);

        updateTimer = new Timer();
        updateTimer.schedule(new UpdateTimer(), 30000, 30000);
    }

    @Override
    public void onStop() {
        super.onStop();
        countdownTimer.cancel();
        updateTimer.cancel();
    }

    private void setupTimerList(View view) {
        timerList = view.findViewById(R.id.timerList);
        timerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        timerList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String timer = (String) parent.getItemAtPosition(position);
                String timerShort = timer.substring(0,8);
                serverConnection.deleteTimer(timerShort).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()) {
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                public void run() {
                                    updateTimerList();
                                    Toast.makeText(getContext(), "Timer wird gelöscht", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
                return true;
            }
        });

        timerList.setAdapter(adapter);
    }

    private void updateTimerList() {
        timer = new ArrayList<>();

        serverConnection.getTimerList().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, final Response<String> response) {
                if(response.isSuccessful() && response.body() != null) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            timer = Arrays.asList(response.body().split(";"));
                            if(!timer.isEmpty()) {
                                for(int i = 0; i < timer.size(); i++) {
                                    String expandedTime = timer.get(i);
                                    expandedTime += " Uhr";
                                    timer.set(i, expandedTime);
                                }
                            }
                            adapter = new ArrayAdapter<>(getActivity(), R.layout.list_todo, timer);
                            timerList.setAdapter(adapter);
                        }
                    });
                } else {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            adapter = new ArrayAdapter<>(getActivity(), R.layout.list_todo, timer);
                            timerList.setAdapter(adapter);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
                        adapter = new ArrayAdapter<>(getActivity(), R.layout.list_todo, timer);
                        timerList.setAdapter(adapter);
                    }
                });
            }
        });
    }

    private void setupAddButton(View view) {
        FloatingActionButton addTimer = view.findViewById(R.id.floatingActionButtonTimer);
        addTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimerPopup();
            }
        });
    }

    private void setupRefreshButton(View view) {
        ImageButton refreshButton = view.findViewById(R.id.imageButtonRefreshTimer);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                updateTimerList();
            }
        });
    }

    private void calcCountdown() {
        Handler handler = new Handler(Looper.getMainLooper());
        if(adapter.isEmpty() || adapter.getItem(0).isEmpty()) {
            handler.post(new Runnable() {
                public void run() {
                    timeView.setText("00:00:00");
                }
            });
        } else {
            String timer = adapter.getItem(0);

            String timerH = timer.substring(0, 2);
            int intTimerH = Integer.parseInt(timerH);
            String timerM = timer.substring(3, 5);
            int intTimerM = Integer.parseInt(timerM);
            String timerS = timer.substring(6, 8);
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
            final String countdown = timeH + ":" + timeM + ":" + timeS;

            handler.post(new Runnable() {
                public void run() {
                    timeView.setText(countdown);
                }
            });

            if(intTimeH <= 0 && intTimeM <= 0 && intTimeS <=0) {
                handler.post(new Runnable() {
                    public void run() {
                        updateTimerList();
                    }
                });
            }
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

    class UpdateTimer extends TimerTask {
        public void run() {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                public void run() {
                    updateTimerList();
                }
            });
        }
    }

    class CountdownTimer extends TimerTask {
        public void run() {
            calcCountdown();
        }
    }
}
