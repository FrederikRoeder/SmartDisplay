package de.fhws.smartdisplay.view.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
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

import de.fhws.smartdisplay.R;

public class SettingsFragment extends Fragment {

    SharedPreferences settings;
    SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_settings, container, false);

        settings = this.getActivity().getSharedPreferences("Settings", 0);
        editor = settings.edit();

        setupEditTextName(view);
        setupButtonAllowNotifications(view);

        return view;
    }

    private void setupEditTextName(View view) {
        final EditText editTextName = view.findViewById(R.id.editTextName);

        editTextName.setText(settings.getString("Name", ""));

        editTextName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    editor.putString("Name", editTextName.getText().toString());
                    editor.commit();
                }
                return false;
            }
        });
    }

    private void setupButtonAllowNotifications(View view) {
        ImageButton buttonAllowNotifications = view.findViewById(R.id.imageButtonAllowNotifications);
        buttonAllowNotifications.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                startActivity(intent);
            }
        });
    }
}
