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

        return view;
    }

    private void setupNotificationSwitch(View view) {
        final Switch notificationSwitch = view.findViewById(R.id.homeSwitchNotification);
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
        final Switch clockSwitch = view.findViewById(R.id.homeSwitchClock);

        //todo: Switch-Informationen vom Server ziehen und in der nächsten Zeile "false" ersetzen
        clockSwitch.setChecked(getClockState());
        clockSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //todo: Switch-Informationen an Server senden
                try {
                    serverConnection.switchClock().execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setupTodoSwitch(View view) {
        final Switch todoSwitch = view.findViewById(R.id.homeSwitchToDo);

        //todo: Switch-Informationen vom Server ziehen und in der nächsten Zeile "false" ersetzen
        todoSwitch.setChecked(getTodoState());
        todoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //todo: Switch-Informationen an Server senden
                try {
                    serverConnection.switchTodo().execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setupTimerSwitch(View view) {
        final Switch timerSwitch = view.findViewById(R.id.homeSwitchTimer);
        //todo: Switch-Informationen vom Server ziehen und in der nächsten Zeile "false" ersetzen
        timerSwitch.setChecked(getTimerState());
        timerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //todo: Switch-Informationen an Server senden
                serverConnection.switchTimer();
            }
        });
    }

    private void setupTempSwitch(View view) {
        final Switch tempSwitch = view.findViewById(R.id.homeSwitchTemp);
        //todo: Switch-Informationen vom Server ziehen und in der nächsten Zeile "false" ersetzen
        tempSwitch.setChecked(getTemperatureState());
        tempSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //todo: Switch-Informationen an Server senden
                try {
                    serverConnection.switchTemperature().execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setupEffectSwitch(View view) {
        final Switch effectSwitch = view.findViewById(R.id.homeSwitchToDo);
        //todo: Switch-Informationen vom Server ziehen und in der nächsten Zeile "false" ersetzen
        effectSwitch.setChecked(getEffectState());
        effectSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //todo: Switch-Informationen an Server senden
                try {
                    serverConnection.switchEffect().execute();
                } catch (IOException e) {
                    e.printStackTrace();
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
        return  false;
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
        return  false;
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
        return  false;
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
        return  false;
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
}
