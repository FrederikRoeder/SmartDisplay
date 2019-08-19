package de.fhws.smartdisplay.view.fragments;

import android.content.ComponentName;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
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
import de.fhws.smartdisplay.server.CustomeCallback;
import de.fhws.smartdisplay.server.ServerConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        setupDB();
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setNotificationState();
        setClockState();
        setTodoState();
        setTimerState();
        setTemperatureState();
        setEffectState();
    }

    private void setupDB() {
        List<SettingsData> settingsList = dataSource.getAll();
        if(settingsList.isEmpty()) {
            SettingsData settingsData = new SettingsData();
            dataSource.create(settingsData);
        }
        if(settingsList.size() > 1) {
            dataSource.deleteAll();
            SettingsData settingsData = new SettingsData();
            dataSource.create(settingsData);
        }
    }

    private void setupNotificationSwitch(View view) {
        notificationSwitch = view.findViewById(R.id.homeSwitchNotification);
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isNotificationServiceEnabled()) {
                    Toast.makeText(getContext(), "Zugriff auf Notifications in Einstellungen erlauben!", Toast.LENGTH_LONG).show();
                    notificationSwitch.setChecked(false);
                    SettingsData settingsData = dataSource.getAll().get(0);
                    settingsData.setNotificationEnabled(false);
                    dataSource.update(settingsData);
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

    private void setNotificationState() {
        List<SettingsData> settingsList = dataSource.getAll();
        notificationSwitch.setChecked(settingsList.get(0).isNotificationEnabled());
    }

    private void setupClockSwitch(View view) {
        clockSwitch = view.findViewById(R.id.homeSwitchClock);
        clockSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    try {
                        serverConnection.switchClockOn().execute();
                        //serverConnection.switchClock("1").execute();
                    } catch (NetworkOnMainThreadException e) {
                        Toast.makeText(getContext(), "Fehler!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(!isChecked) {
                    try {
                        serverConnection.switchClockOff().execute();
                        //serverConnection.switchClock("0").execute();
                    } catch (NetworkOnMainThreadException e) {
                        Toast.makeText(getContext(), "Fehler!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void setClockState() {
        getRequestGeneric(serverConnection.getClockState(), new CustomeCallback<String>() {
            @Override
            public void onResponse(String value) {
                clockSwitch.setChecked(value == "1");
            }

            @Override
            public void onFailure() {
                clockSwitch.setChecked(false);
            }
        });
    }

    private void setupTodoSwitch(View view) {
        todoSwitch = view.findViewById(R.id.homeSwitchToDo);
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

    private void setTodoState() {
        getRequestGeneric(serverConnection.getTodoState(), new CustomeCallback<String>() {
            @Override
            public void onResponse(String value) {
                todoSwitch.setChecked(value == "1");
            }

            @Override
            public void onFailure() {
                todoSwitch.setChecked(false);
            }
        });
    }

    private void setupTimerSwitch(View view) {
        timerSwitch = view.findViewById(R.id.homeSwitchTimer);
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

    private void setTimerState() {
        getRequestGeneric(serverConnection.getTimerState(), new CustomeCallback<String>() {
            @Override
            public void onResponse(String value) {
                timerSwitch.setChecked(value == "1");
            }

            @Override
            public void onFailure() {
                timerSwitch.setChecked(false);
            }
        });
    }

    private void setupTempSwitch(View view) {
        tempSwitch = view.findViewById(R.id.homeSwitchTemp);
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

    private void setTemperatureState() {
        getRequestGeneric(serverConnection.getTemperatureState(), new CustomeCallback<String>() {
            @Override
            public void onResponse(String value) {
                tempSwitch.setChecked(value == "1");
            }

            @Override
            public void onFailure() {
                tempSwitch.setChecked(false);
            }
        });
    }

    private void setupEffectSwitch(View view) {
        effectSwitch = view.findViewById(R.id.homeSwitchEffect);
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

    private void setEffectState() {
        getRequestGeneric(serverConnection.getEffectState(), new CustomeCallback<String>() {
            @Override
            public void onResponse(String value) {
                effectSwitch.setChecked(value == "1");
            }

            @Override
            public void onFailure() {
                effectSwitch.setChecked(false);
            }
        });
    }

    private void refreshSwitches() {
        setNotificationState();
        setClockState();
        setTodoState();
        setTimerState();
        setTemperatureState();
        setEffectState();
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

    public <T> void getRequestGeneric(Call<T> call, final CustomeCallback<T> callback){
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                callback.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                callback.onFailure();
            }
        });
    }
}
