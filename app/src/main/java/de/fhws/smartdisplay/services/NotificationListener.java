package de.fhws.smartdisplay.services;

import android.app.Notification;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

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

    private boolean lock;

    @Override
    public void onCreate() {
        super.onCreate();

        dataSource = new SettingsDataSource(this);
        serverConnection = new ConnectionFactory().buildConnection();

        lock = false;

        setupDB();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if(getNotificationState()) {

            if ((sbn.getNotification().flags & Notification.FLAG_GROUP_SUMMARY) != 0) {
                return;
            }

            String pack = "";
            String title = "";
            String text = "";

            pack = sbn.getPackageName();
            Bundle extras = sbn.getNotification().extras;
            if(!extras.isEmpty()) {
                if(extras.containsKey("android.title") && extras.getString("android.title") != null) {
                    title = extras.getString("android.title");
                }
                if(extras.containsKey("android.text") && extras.getCharSequence("android.text") != null) {
                    text = extras.getCharSequence("android.text").toString();

                    if(pack.equals(ApplicationPackageNames.SMS_PACK_NAME)) {
                        sendNotification("Sms");
                    }
                    else if(pack.equals(ApplicationPackageNames.WHATSAPP_PACK_NAME)) {
                        sendNotification("WhatsApp");
                    }
                    else if(pack.equals(ApplicationPackageNames.INSTAGRAM_PACK_NAME)) {
                        sendNotification("Instagram");
                    }
                    else if(pack.equals(ApplicationPackageNames.SNAPCHAT_PACK_NAME)) {
                        sendNotification("Snapchat");
                    }
                    else if(pack.equals(ApplicationPackageNames.FACEBOOK_PACK_NAME) || pack.equals(ApplicationPackageNames.FACEBOOKM_PACK_NAME)) {
                        sendNotification("Facebook");
                    }
                    else if(pack.equals(ApplicationPackageNames.TWITTER_PACK_NAME)) {
                        sendNotification("Twitter");
                    }
                    else if(pack.equals(ApplicationPackageNames.MAIL_PACK_NAME) || pack.equals(ApplicationPackageNames.GMAIL_PACK_NAME)) {
                        sendNotification("Mail");
                    }
                }
            }

            if(pack.equals(ApplicationPackageNames.PHONE_PACK_NAME) && !lock) {
                sendNotification("Telefon");
                setLock();
            }
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {

    }

    private void sendNotification(String app) {
        serverConnection.sendNotification(app, getNameFromSettings()).enqueue(new Callback<Void>() {
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
        return settingsList.get(0).getName();
    }

    private boolean getNotificationState() {
        List<SettingsData> settingsList = dataSource.getAll();
        return settingsList.get(0).isNotificationEnabled();
    }

    private void setLock() {
        lock = true;
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        lock = false;
                    }
                },
                30000
        );
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

    private static final class ApplicationPackageNames {
        public static final String SMS_PACK_NAME = "com.samsung.android.messaging";
        public static final String WHATSAPP_PACK_NAME = "com.whatsapp";
        public static final String INSTAGRAM_PACK_NAME = "com.instagram.android";
        public static final String SNAPCHAT_PACK_NAME = "com.snapchat.android";
        public static final String FACEBOOK_PACK_NAME = "com.facebook.katana";
        public static final String FACEBOOKM_PACK_NAME = "com.facebook.orca";
        public static final String TWITTER_PACK_NAME = "com.twitter.android";
        public static final String MAIL_PACK_NAME = "com.samsung.android.email.provider";
        public static final String GMAIL_PACK_NAME = "com.google.android.gm";
        public static final String PHONE_PACK_NAME = "com.samsung.android.incallui";
    }
}
