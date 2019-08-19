package de.fhws.smartdisplay.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import de.fhws.smartdisplay.R;
import de.fhws.smartdisplay.database.SettingsData;
import de.fhws.smartdisplay.database.SettingsDataSource;
import de.fhws.smartdisplay.server.ConnectionFactory;
import de.fhws.smartdisplay.server.ServerConnection;

public class SettingsFragment extends Fragment {

    SettingsDataSource dataSource;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_settings, container, false);

        dataSource = new SettingsDataSource(this.getContext());

        setupEditTextName(view);

        ImageButton buttonAllowNotifications = view.findViewById(R.id.imageButtonAllowNotifications);
        buttonAllowNotifications.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                startActivity(intent);
            }
        });

        return view;
    }

    private void setupEditTextName(View view) {
        final EditText editTextName = view.findViewById(R.id.editTextName);

        setupDB();
        editTextName.setText(getName());

        editTextName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    SettingsData settingsData = dataSource.getAll().get(0);
                    settingsData.setName(editTextName.getText().toString());
                    dataSource.update(settingsData);
                }
                return false;
            }
        });
    }

    private String getName() {
        List<SettingsData> settingsList = dataSource.getAll();
        return settingsList.get(0).getName();
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
}
