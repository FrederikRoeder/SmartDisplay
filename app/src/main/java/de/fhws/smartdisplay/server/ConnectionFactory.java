package de.fhws.smartdisplay.server;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConnectionFactory {

    public ServerConnection buildConnection() {
        Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl("http://192.168.43.251:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ServerConnection.class);
    }
}
