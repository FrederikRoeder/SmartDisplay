package de.fhws.smartdisplay.services;

import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.widget.Toast;

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
        String pack = sbn.getPackageName();
        String ticker = sbn.getNotification().tickerText.toString();
        Bundle extras = sbn.getNotification().extras;
        String title = extras.getString("android.title");
        String text = extras.getCharSequence("android.text").toString();

        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
    }
}
