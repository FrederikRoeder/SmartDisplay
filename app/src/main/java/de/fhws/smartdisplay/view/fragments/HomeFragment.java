package de.fhws.smartdisplay.view.fragments;

import android.content.ComponentName;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import de.fhws.smartdisplay.R;
import de.fhws.smartdisplay.server.ConnectionFactory;
import de.fhws.smartdisplay.server.ServerConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";

    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private ServerConnection serverConnection;
    private Timer updateTimer;

    private Switch notificationSwitch;
    private Switch clockSwitch;
    private Switch todoSwitch;
    private Switch timerSwitch;
    private Switch effectSwitch;
    private Switch lightingSwitch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_home, container, false);

        settings = this.getActivity().getSharedPreferences("Settings", 0);
        editor = settings.edit();
        serverConnection = new ConnectionFactory().buildConnection();

        setupNotificationSwitch(view);
        setupClockSwitch(view);
        setupTodoSwitch(view);
        setupTimerSwitch(view);
        setupEffectSwitch(view);
        setupLightingSwitch(view);
        setupColorChangeButton(view);
        setupRefreshButton(view);
        setupEsterEggButton(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setNotificationState();
        refreshSwitches();

        updateTimer = new Timer();
        updateTimer.schedule(new UpdateTimer(), 30000, 30000);
    }

    @Override
    public void onStop() {
        super.onStop();
        updateTimer.cancel();
    }

    private void setupNotificationSwitch(View view) {
        notificationSwitch = view.findViewById(R.id.homeSwitchNotification);
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isNotificationServiceEnabled()) {
                    notificationSwitch.setChecked(false);
                    Toast.makeText(getContext(), "Zugriff auf Notifications in Einstellungen erlauben!", Toast.LENGTH_LONG).show();
                    editor.putBoolean("NotificationState", false);
                    editor.commit();
                } else {
                    if(isChecked) {
                        editor.putBoolean("NotificationState", true);
                        editor.commit();
                    }

                    if(!isChecked) {
                        editor.putBoolean("NotificationState", false);
                        editor.commit();
                    }
                }
            }
        });
    }

    private void setNotificationState() {
        if(isNotificationServiceEnabled()) {
            notificationSwitch.setChecked(settings.getBoolean("NotificationState", false));
        }
    }

    private void setupClockSwitch(View view) {
        clockSwitch = view.findViewById(R.id.homeSwitchClock);
        clockSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    sendStateToServer(clockSwitch, serverConnection.switchClock("1"));
                }

                if(!isChecked) {
                    sendStateToServer(clockSwitch, serverConnection.switchClock("0"));
                }
            }
        });
    }

    private void setClockState() {
        getStateFromServer(clockSwitch, serverConnection.getClockState());
    }

    private void setupTodoSwitch(View view) {
        todoSwitch = view.findViewById(R.id.homeSwitchToDo);
        todoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    sendStateToServer(todoSwitch, serverConnection.switchTodo("1"));
                }

                if(!isChecked) {
                    sendStateToServer(todoSwitch, serverConnection.switchTodo("0"));
                }
            }
        });
    }

    private void setTodoState() {
        getStateFromServer(todoSwitch, serverConnection.getTodoState());
    }

    private void setupTimerSwitch(View view) {
        timerSwitch = view.findViewById(R.id.homeSwitchTimer);
        timerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    sendStateToServer(timerSwitch, serverConnection.switchTimer("1"));
                }

                if(!isChecked) {
                    sendStateToServer(timerSwitch, serverConnection.switchTimer("0"));
                }
            }
        });
    }

    private void setTimerState() {
        getStateFromServer(timerSwitch, serverConnection.getTimerState());
    }

    private void setupEffectSwitch(View view) {
        effectSwitch = view.findViewById(R.id.homeSwitchEffect);
        effectSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    sendStateToServer(effectSwitch, serverConnection.switchEffect("1"));
                }

                if(!isChecked) {
                    sendStateToServer(effectSwitch, serverConnection.switchEffect("0"));
                }
            }
        });
    }

    private void setEffectState() {
        getStateFromServer(effectSwitch, serverConnection.getEffectState());
    }

    private void setupLightingSwitch(View view) {
        lightingSwitch = view.findViewById(R.id.homeSwitchLighting);
        lightingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    sendStateToServer(lightingSwitch, serverConnection.switchLighting("1"));
                }

                if(!isChecked) {
                    sendStateToServer(lightingSwitch, serverConnection.switchLighting("0"));
                }
            }
        });
    }

    private void setLightingState() {
        getStateFromServer(lightingSwitch, serverConnection.getLightingState());
    }

    private void setupColorChangeButton(View view) {
        ImageButton colorChangeButton = view.findViewById(R.id.imageButtonChangeColor);
        colorChangeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                serverConnection.switchColor().enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            }
        });
    }

    private void setupEsterEggButton(View view) {
        Button easerEggButton = view.findViewById(R.id.buttonEasterEgg);
        easerEggButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                serverConnection.easteregg().enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });
    }

    private void setupRefreshButton(View view) {
        ImageButton refreshButton = view.findViewById(R.id.imageButtonRefreshHome);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                refreshSwitches();
            }
        });
    }

    private void refreshSwitches() {
        setClockState();
        setTodoState();
        setTimerState();
        setEffectState();
        setLightingState();
    }

    private void getStateFromServer(final Switch sw, Call<String> call) {
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, final Response<String> response) {
                if(response.isSuccessful()) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            sw.setChecked(response.body().equals("1"));
                        }
                    });
                } else {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            sw.setChecked(false);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
                        sw.setChecked(false);
                    }
                });
            }
        });
    }

    private void sendStateToServer(final Switch sw, final Call<String> call) {
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(!response.isSuccessful()) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            sw.setChecked(false);
                        }
                    });
                } else if(response.body().equals("dbEmpty") && sw == todoSwitch) {
                    sw.setChecked(false);
                    Toast.makeText(getContext(), "Keine Todos vorhanden!", Toast.LENGTH_LONG).show();
                } else if(response.body().equals("dbEmpty") && sw == timerSwitch) {
                    sw.setChecked(false);
                    Toast.makeText(getContext(), "Keine Timer vorhanden!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
                        sw.setChecked(false);
                    }
                });
            }
        });
    }

    private boolean isNotificationServiceEnabled(){
        String pkgName = this.getContext().getPackageName();
        final String flat = Settings.Secure.getString(this.getContext().getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    class UpdateTimer extends TimerTask {
        public void run() {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                public void run() {
                    refreshSwitches();
                }
            });
        }
    }
}
