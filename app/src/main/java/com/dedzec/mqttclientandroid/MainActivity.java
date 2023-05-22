package com.dedzec.mqttclientandroid;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.RequiresApi;

public class MainActivity extends AppCompatActivity {

    @Override
    @RequiresApi(Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));

        /* Check if Internet connection is available */
        if (!isConnected()) {
            Log.d("onCreate", "Internet connection NOT available");

            Toast.makeText(getApplicationContext(), "Internet connection NOT available", Toast.LENGTH_LONG).show();
        }
    }

    private Boolean isConnected() {
        boolean result = false;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NetworkCapabilities  capabilities = (NetworkCapabilities ) cm.getNetworkCapabilities(cm.getActiveNetwork());
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                    result = true;
                }
            }
        } else {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null) {
                // connected to the internet
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    result = true;
                } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    result = true;
                } else if (activeNetwork.getType() == ConnectivityManager.TYPE_VPN) {
                    result = true;
                }
            }
        }
        return result;
    }
}