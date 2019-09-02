package de.fhws.smartdisplay.server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ConnectionFactory {
    public ServerConnection buildConnection() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.251:5000/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit.create(ServerConnection.class);
    }

//    private String getIP() throws IOException, InterruptedException {
//        String ip = "";
//        String myIPAddress = getMyIPAddress();
//        String subnet = myIPAddress.substring(0, myIPAddress.lastIndexOf("."));
//        String currentHost;
//        for (int i = 0; i < 255; i++) {
//            currentHost = subnet + "." + i;
//            Process p1 = Runtime.getRuntime().exec("ping -c 1 " + currentHost);
//            int returnVal = p1.waitFor();
//            boolean reachable = (returnVal == 0);
//            if (reachable) {
//                //currentHost (the IP Address) actually exists in the network
//            }
//        }
//        return ip;
//    }
//
//    private String getMyIPAddress() {
//        String myIP = null;
//        try {
//            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
//            for (NetworkInterface intf : interfaces) {
//                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
//                for (InetAddress addr : addrs) {
//                    if (!addr.isLoopbackAddress()) {
//                        String sAddr = addr.getHostAddress().toUpperCase();
//                        boolean isIPv4 = Inet4Address.getByName(sAddr) != null;
//                        if (isIPv4)
//                            myIP = sAddr;
//                    }
//                }
//            }
//
//        } catch (SocketException e) {
//            e.printStackTrace();
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//        return myIP;
//    }
}
