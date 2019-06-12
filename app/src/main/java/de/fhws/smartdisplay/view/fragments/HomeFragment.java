package de.fhws.smartdisplay.view.fragments;

import android.content.ComponentName;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;

import de.fhws.smartdisplay.R;
import de.fhws.smartdisplay.database.SettingsData;
import de.fhws.smartdisplay.database.SettingsDataSource;

public class HomeFragment extends Fragment {

    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    //todo: Serverconnection bekommen
    private SettingsDataSource dataSource;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_home, container, false);

        dataSource = new SettingsDataSource(this.getContext());

        Switch todoSwitch = view.findViewById(R.id.homeSwitchToDo);
        Switch timerSwitch = view.findViewById(R.id.homeSwitchTimer);
        Switch tempSwitch = view.findViewById(R.id.homeSwitchTemp);
        Switch effectSwitch = view.findViewById(R.id.homeSwitchEffect);

        setupNotificationSwitch(view);

        return view;
    }

    private void setupNotificationSwitch(View view) {
        Switch notificationSwitch = view.findViewById(R.id.homeSwitchNotification);
        if(!isNotificationServiceEnabled()) {
            Toast.makeText(this.getContext(), "Zugriff auf Notifications in Einstellungen zulassen!", Toast.LENGTH_LONG).show();
        } else {
            notificationSwitch.setChecked(getNotificationState());
        }
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    SettingsData settingsData = dataSource.getAllSettings().get(0);
                    settingsData.setNotificationSet(true);
                    dataSource.update(settingsData);
                }
                if(!isChecked) {
                    SettingsData settingsData = dataSource.getAllSettings().get(0);
                    settingsData.setNotificationSet(false);
                    dataSource.update(settingsData);
                }
            }
        });
    }

    private boolean getNotificationState() {
        List<SettingsData> settingsList = dataSource.getAllSettings();
        if(settingsList.isEmpty()) {
            SettingsData settingsData = new SettingsData();
            dataSource.create(settingsData);
            Toast.makeText(this.getContext(), "erstellt", Toast.LENGTH_LONG).show();
            return false;
        } else if(settingsList.size() > 1) {
            for(int i = 0; i < settingsList.size()-1; i++){
                dataSource.delete(settingsList.get(i).getId());
            }
            return settingsList.get(0).isNotificationSet();
        } else {
            if(settingsList.get(0).isNotificationSet()){
                Toast.makeText(this.getContext(), "N Set", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this.getContext(), "N Not Set", Toast.LENGTH_LONG).show();
            }

            return settingsList.get(0).isNotificationSet();
        }
    }

    private boolean isNotificationServiceEnabled(){
        AppCompatActivity a = new AppCompatActivity();
        String pkgName = a.getPackageName();
        final String flat = Settings.Secure.getString(a.getContentResolver(),
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
