package dev.paj.towatchtonight.util.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import javax.inject.Inject;

public class NetworkUtils {
    private Context applicationContext;

    @Inject
    public NetworkUtils(Context appContext) {
        this.applicationContext = appContext;

    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)applicationContext.getApplicationContext().getSystemService(applicationContext.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
