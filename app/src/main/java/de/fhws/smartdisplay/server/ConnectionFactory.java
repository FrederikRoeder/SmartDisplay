package de.fhws.smartdisplay.server;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConnectionFactory {

    public ServerConnection buildConnection() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://123.123.123.123:5000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ServerConnection.class);
    }
}
