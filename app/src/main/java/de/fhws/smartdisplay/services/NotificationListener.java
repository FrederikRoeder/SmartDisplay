package de.fhws.smartdisplay.services;

import android.app.Notification;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Telephony;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import de.fhws.smartdisplay.server.ConnectionFactory;
import de.fhws.smartdisplay.server.ServerConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationListener extends NotificationListenerService {

    private SharedPreferences settings;
    private ServerConnection serverConnection;

    private String smsPckName = "";
    private boolean lock = false;

    @Override
    public void onCreate() {
        super.onCreate();

        settings = getSharedPreferences("Settings", 0);
        serverConnection = new ConnectionFactory().buildConnection();

        setSmsPackName();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if(settings.getBoolean("NotificationState", false)) {

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


                    Log.d("notpac", "PackageName: " + pack);


                    if(pack.equals(ApplicationPackageNames.WHATSAPP_PACK_NAME)) {
                        sendNotification("WhatsApp");
                    }
                    else if(pack.equals(ApplicationPackageNames.INSTAGRAM_PACK_NAME)) {
                        sendNotification("Instagram");
                    }
                    else if(pack.equals(ApplicationPackageNames.SNAPCHAT_PACK_NAME)) {
                        sendNotification("Snapchat");
                    }
                    else if(pack.equals(ApplicationPackageNames.TWITTER_PACK_NAME)) {
                        sendNotification("Twitter");
                    }
                    else if(pack.equals(ApplicationPackageNames.FACEBOOK_PACK_NAME) || pack.equals(ApplicationPackageNames.FACEBOOK_M_PACK_NAME)) {
                        sendNotification("Facebook");
                    }
                    else if(pack.equals(ApplicationPackageNames.S_MAIL_PACK_NAME) || pack.equals(ApplicationPackageNames.G_MAIL_PACK_NAME)) {
                        sendNotification("Mail");
                    }
                    else if(//pack.equals(ApplicationPackageNames.G_SMS_PACK_NAME) || pack.equals(ApplicationPackageNames.S_SMS_PACK_NAME ) ||
                            pack.equals(smsPckName)) {
                        sendNotification("Sms");
                    }
//                    else if((pack.equals(ApplicationPackageNames.G_PHONE_PACK_NAME) || pack.equals(ApplicationPackageNames.S_PHONE_PACK_NAME)) && !lock) {
//                        sendNotification("Telefon");
//                        setLock();
//                    }
                }
            }
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {

    }

    private void sendNotification(String app) {
        serverConnection.sendNotification(app, settings.getString("Name", "")).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
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

    private void setSmsPackName() {
        smsPckName = Telephony.Sms.getDefaultSmsPackage(this);
    }

    private static final class ApplicationPackageNames {
        public static final String WHATSAPP_PACK_NAME = "com.whatsapp";
        public static final String INSTAGRAM_PACK_NAME = "com.instagram.android";
        public static final String SNAPCHAT_PACK_NAME = "com.snapchat.android";
        public static final String TWITTER_PACK_NAME = "com.twitter.android";
        public static final String FACEBOOK_PACK_NAME = "com.facebook.katana";
        public static final String FACEBOOK_M_PACK_NAME = "com.facebook.orca";

        public static final String G_MAIL_PACK_NAME = "com.google.android.gm";
        public static final String S_MAIL_PACK_NAME = "com.samsung.android.email.provider";

//        public static final String G_SMS_PACK_NAME = "com.google.android.apps.messaging";
//        public static final String S_SMS_PACK_NAME = "com.samsung.android.messaging";
//
//        public static final String G_PHONE_PACK_NAME = "com.google.android.dialer";
//        public static final String S_PHONE_PACK_NAME = "com.samsung.android.incallui";
    }
}
