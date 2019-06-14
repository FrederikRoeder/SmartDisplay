package de.fhws.smartdisplay.services;

import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.widget.Toast;

import java.util.List;

import de.fhws.smartdisplay.database.SettingsData;
import de.fhws.smartdisplay.database.SettingsDataSource;

public class NotificationListener extends NotificationListenerService {

    private SettingsDataSource dataSource;

    @Override
    public void onCreate() {
        super.onCreate();

        dataSource = new SettingsDataSource(this);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if(getNotificationState()) {
            String pack = "";
            String ticker = "";
            String title = "";
            String text = "";

//            pack = sbn.getPackageName();
//            ticker = sbn.getNotification().tickerText.toString();
            Bundle extras = sbn.getNotification().extras;
            title = extras.getString("android.title");
            text = extras.getCharSequence("android.text").toString();

            //todo: prüfen ob Nachricht SMS  / WhatsApp / Insta / Snapchat / Facebook / ... -Nachricht ist
            //todo: prüfen, ob es pack / ticker / title / text gibt
            //todo: vorhandene Daten an Server schicken
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
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
}
