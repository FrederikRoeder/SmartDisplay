package de.fhws.smartdisplay.services;

import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import de.fhws.smartdisplay.database.SettingsData;
import de.fhws.smartdisplay.database.SettingsDataSource;
import de.fhws.smartdisplay.server.ConnectionFactory;
import de.fhws.smartdisplay.server.ServerConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationListener extends NotificationListenerService {

    private SettingsDataSource dataSource;
    private ServerConnection serverConnection;

    @Override
    public void onCreate() {
        super.onCreate();

        dataSource = new SettingsDataSource(this);
        serverConnection = new ConnectionFactory().buildConnection();

        setupDB();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if(getNotificationState()) {
            String pack = "";
            String ticker = "";
            String title = "";
            String text = "";

            //pack = sbn.getPackageName();
            //ticker = sbn.getNotification().tickerText.toString();
            Bundle extras = sbn.getNotification().extras;
            if(!extras.isEmpty()) {
                if(extras.containsKey("android.title") && extras.getString("android.title") != null) {
                    title = extras.getString("android.title");
                }
                if(extras.containsKey("android.text") && extras.getCharSequence("android.text") != null) {
                    text = extras.getCharSequence("android.text").toString();

                    String app = "WhatsApp";
                    Toast.makeText(this, text, Toast.LENGTH_LONG).show();

                    sendNotification(text, app);
                }
            }
            //todo: prüfen ob Nachricht SMS  / WhatsApp / Insta / Snapchat / Facebook / ... -Nachricht ist
            //todo: prüfen, ob es pack / ticker / title / text gibt
            //todo: vorhandene Daten (mit Name) an Server schicken
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
    }

    private void sendNotification(String notification, String app) {
        serverConnection.sendNotification(getNameFromSettings(), app, notification).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    private String getNameFromSettings() {
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
        return settingsList.get(0).getName();
    }

    private boolean getNotificationState() {
        List<SettingsData> settingsList = dataSource.getAll();
        return settingsList.get(0).isNotificationEnabled();
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
