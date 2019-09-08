package de.fhws.smartdisplay.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;

import de.fhws.smartdisplay.server.ConnectionFactory;
import de.fhws.smartdisplay.server.ServerConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        Log.d("notpac", "Call 1");


        SharedPreferences settings = context.getSharedPreferences("Settings", 0);
        ServerConnection serverConnection = new ConnectionFactory().buildConnection();


        Log.d("notpac", "Call vor if");


        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        if (TelephonyManager.EXTRA_STATE_RINGING.equals(state) && settings.getBoolean("NotificationState", false)) {


            Log.d("notpac", "Call in if");


            serverConnection.sendNotification("Telefon", settings.getString("Name", "")).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {


                    Log.d("notpac", "Call onResponse");


                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {


                    Log.d("notpac", "Call onFailure");


                }
            });
        }
    }
}