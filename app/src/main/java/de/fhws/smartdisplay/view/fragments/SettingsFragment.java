package de.fhws.smartdisplay.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.List;

import de.fhws.smartdisplay.R;
import de.fhws.smartdisplay.database.SettingsData;
import de.fhws.smartdisplay.database.SettingsDataSource;
import de.fhws.smartdisplay.server.ServerConnection;

public class SettingsFragment extends Fragment {

    SettingsDataSource dataSource;
    ServerConnection serverConnection;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_settings, container, false);

        dataSource = new SettingsDataSource(this.getContext());
        serverConnection = new ServerConnection();

        EditText editTextName = view.findViewById(R.id.editTextName);
        editTextName.setText(getName());
        //todo: bei Änderung des Namens (Bestätigung auf der Tastatur) neuen Namen an Server schicken +
        //SettingsData settingsData = dataSource.getAll().get(0);
        //settingsData.setName(editTextName.getText().toString());
        //dataSource.update(settingsData);

        ImageButton buttonAllowNotifications = view.findViewById(R.id.imageButtonAllowNotifications);
        buttonAllowNotifications.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                startActivity(intent);
            }
        });
        return view;
    }

    private String getName() {
        List<SettingsData> settingsList = dataSource.getAll();
        if(settingsList.isEmpty()) {
            SettingsData settingsData = new SettingsData();
            dataSource.create(settingsData);
            return "";
        } else if(settingsList.size() > 1) {
            dataSource.deleteAll();
            SettingsData settingsData = new SettingsData();
            dataSource.create(settingsData);
            return "";
        } else {
            return settingsList.get(0).getName();
        }
    }
}
