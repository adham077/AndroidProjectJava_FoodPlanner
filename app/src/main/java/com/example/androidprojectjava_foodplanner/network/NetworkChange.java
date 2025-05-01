package com.example.androidprojectjava_foodplanner.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;

public class NetworkChange extends BroadcastReceiver {

    private static volatile NetworkChange instance;
    private static NetworkListener listener;
    private NetworkChange() {}

    public static NetworkChange getInstance() {
        if (instance == null) {
            synchronized (NetworkChange.class) {
                if (instance == null) {
                    instance = new NetworkChange();
                }
            }
        }
        return instance;
    }

    public void setListener(NetworkListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            Network network = cm.getActiveNetwork();
            NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
            boolean isConnected = capabilities != null &&
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);

            if (listener != null) {
                listener.onChange(isConnected);
            }
        }
    }

    public boolean isConnected(Object context) {
        ConnectivityManager cm = (ConnectivityManager) ((Context)context).getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        Network network = cm.getActiveNetwork();
        NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
        return capabilities != null &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
    }

}