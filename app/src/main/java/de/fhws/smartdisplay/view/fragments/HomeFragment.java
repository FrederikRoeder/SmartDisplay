package de.fhws.smartdisplay.view.fragments;

import android.content.ComponentName;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import de.fhws.smartdisplay.R;
import de.fhws.smartdisplay.database.SettingsData;
import de.fhws.smartdisplay.database.SettingsDataSource;
import de.fhws.smartdisplay.server.ConnectionFactory;
import de.fhws.smartdisplay.server.ServerConnection;

public class HomeFragment extends Fragment {

    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";

    private SettingsDataSource dataSource;
    private ServerConnection serverConnection;

    private Switch notificationSwitch;
    private Switch clockSwitch;
    private Switch todoSwitch;
    private Switch timerSwitch;
    private Switch tempSwitch;
    private Switch effectSwitch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_home, container, false);

        dataSource = new SettingsDataSource(this.getContext());
        serverConnection = new ConnectionFactory().buildConnection();

        setupNotificationSwitch(view);
        setupClockSwitch(view);
        setupTodoSwitch(view);
        setupTimerSwitch(view);
        setupTempSwitch(view);
        setupEffectSwitch(view);

        ImageButton refreshButton = view.findViewById(R.id.imageButtonRefreshHome);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                refreshSwitches();
            }
        });

        return view;
    }

    private void setupNotificationSwitch(View view) {
        notificationSwitch = view.findViewById(R.id.homeSwitchNotification);
        notificationSwitch.setChecked(getNotificationState());
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isNotificationServiceEnabled()) {
                    Toast.makeText(getContext(), "Zugriff auf Notifications in Einstellungen erlauben!", Toast.LENGTH_LONG).show();
                    notificationSwitch.setChecked(false);
                }
                if(isChecked) {
                    SettingsData settingsData = dataSource.getAll().get(0);
                    settingsData.setNotificationEnabled(true);
                    dataSource.update(settingsData);
                }
                if(!isChecked) {
                    SettingsData settingsData = dataSource.getAll().get(0);
                    settingsData.setNotificationEnabled(false);
                    dataSource.update(settingsData);
                }
            }
        });
    }

    private void setupClockSwitch(View view) {
        clockSwitch = view.findViewById(R.id.homeSwitchClock);
        clockSwitch.setChecked(getClockState());
        clockSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    try {
                        serverConnection.switchClockOn().execute();
                        //serverConnection.switchClock("1").execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(!isChecked) {
                    try {
                        serverConnection.switchClockOff().execute();
                        //serverConnection.switchClock("0").execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void setupTodoSwitch(View view) {
        todoSwitch = view.findViewById(R.id.homeSwitchToDo);
        todoSwitch.setChecked(getTodoState());
        todoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    try {
                        serverConnection.switchTodoOn().execute();
                        //serverConnection.switchTodo("1").execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(!isChecked) {
                    try {
                        serverConnection.switchTodoOff().execute();
                        //serverConnection.switchTodo("0").execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void setupTimerSwitch(View view) {
        timerSwitch = view.findViewById(R.id.homeSwitchTimer);
        timerSwitch.setChecked(getTimerState());
        timerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    try {
                        serverConnection.switchTimerOn().execute();
                        //serverConnection.switchTimer("1").execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(!isChecked) {
                    try {
                        serverConnection.switchTimerOff().execute();
                        //serverConnection.switchTimer("0").execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void setupTempSwitch(View view) {
        tempSwitch = view.findViewById(R.id.homeSwitchTemp);
        tempSwitch.setChecked(getTemperatureState());
        tempSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    try {
                        serverConnection.switchTemperatureOn().execute();
                        //serverConnection.switchTemperature("1").execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(!isChecked) {
                    try {
                        serverConnection.switchTemperatureOff().execute();
                        //serverConnection.switchTemperature("0").execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void setupEffectSwitch(View view) {
        effectSwitch = view.findViewById(R.id.homeSwitchEffect);
        effectSwitch.setChecked(getEffectState());
        effectSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    try {
                        serverConnection.switchEffectOn().execute();
                        //serverConnection.switchEffect("1").execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(!isChecked) {
                    try {
                        serverConnection.switchEffectOff().execute();
                        //serverConnection.switchEffect("0").execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private boolean getNotificationState() {
        List<SettingsData> settingsList = dataSource.getAll();
        if(settingsList.isEmpty()) {
            SettingsData settingsData = new SettingsData();
            dataSource.create(settingsData);
            return false;
        } else if(settingsList.size() > 1) {
            dataSource.deleteAll();
            SettingsData settingsData = new SettingsData();
            dataSource.create(settingsData);
            return false;
        } else {
            return settingsList.get(0).isNotificationEnabled();
        }
    }

    private boolean getClockState() {
        String clockState = "";
        try {
            clockState = serverConnection.getClockState().execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (clockState == "1") {
            return true;
        }
        return false;
    }

    private boolean getTodoState() {
        String todoState = "";
        try {
            todoState = serverConnection.getTodoState().execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(todoState == "1") {
            return true;
        }
        return false;
    }

    private boolean getTimerState() {
        String timerState = "";
        try {
            timerState = serverConnection.getTimerState().execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(timerState == "1") {
            return true;
        }
        return false;
    }

    private boolean getTemperatureState() {
        String temperatureState = "";
        try {
            temperatureState = serverConnection.getTemperatureState().execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(temperatureState == "1") {
            return true;
        }
        return false;
    }

    private boolean getEffectState() {
        String effectState = "";
        try {
            effectState = serverConnection.getEffectState().execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(effectState == "1") {
            return true;
        }
        return false;
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

    private void refreshSwitches() {
        notificationSwitch.setChecked(getNotificationState());
        clockSwitch.setChecked(getClockState());
        todoSwitch.setChecked(getTodoState());
        timerSwitch.setChecked(getTimerState());
        tempSwitch.setChecked(getTemperatureState());
        effectSwitch.setChecked(getEffectState());
    }
}
