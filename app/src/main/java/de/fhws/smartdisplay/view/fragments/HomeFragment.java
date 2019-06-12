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

import java.util.List;

import de.fhws.smartdisplay.R;
import de.fhws.smartdisplay.database.SettingsData;
import de.fhws.smartdisplay.database.SettingsDataSource;

public class HomeFragment extends Fragment {

    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";

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
        final Switch notificationSwitch = view.findViewById(R.id.homeSwitchNotification);
        notificationSwitch.setChecked(getNotificationState());
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isNotificationServiceEnabled()) {
                    Toast.makeText(getContext(), "Zugriff auf Notifications in Einstellungen zulassen!", Toast.LENGTH_LONG).show();
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
