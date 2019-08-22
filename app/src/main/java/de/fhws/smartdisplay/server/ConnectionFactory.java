package de.fhws.smartdisplay.server;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConnectionFactory {

    public ServerConnection buildConnection() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl("http://192.168.43.251:5000/")
                .addConverterFactory(GsonConverterFactory.create()).client(client)
                .build();
        return retrofit.create(ServerConnection.class);
    }
}
