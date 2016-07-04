/**
 Copyright (C) 2016 Laszlo Makk
 This project is licensed under the Apache 2.0 License
 */

package uk.ac.cam.cl.lm649.nsdmanagercrash;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.internal.util.AsyncChannel;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    public static final String SERVICE_TYPE = "_http._tcp";
    protected NsdManager nsdManager;
    private static final String TAG = "NsdManagerCrash";
    private String myServiceName = "nameOfMyVeryCoolService";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "Application started");

        nsdManager = (NsdManager) getSystemService(Context.NSD_SERVICE);

        NsdServiceInfo serviceInfo  = new NsdServiceInfo();
        serviceInfo.setServiceName(myServiceName);
        serviceInfo.setServiceType(SERVICE_TYPE);

        Log.d(TAG, "calling magic()");
        magic();
        Log.d(TAG, "starting a resolve");
        nsdManager.resolveService(serviceInfo, new CustomResolveListener());
        Log.d(TAG, "Finished hacks.");
    }

    private void magic(){
        try {
            Field f = nsdManager.getClass().getDeclaredField("mAsyncChannel");
            f.setAccessible(true);
            AsyncChannel mAsyncChannel = (AsyncChannel) f.get(nsdManager);
            Log.d(TAG, "calling mAsyncChannel.disconnect()");
            mAsyncChannel.disconnect();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private class CustomResolveListener implements NsdManager.ResolveListener {
        @Override
        public void onResolveFailed(final NsdServiceInfo serviceInfo, int errorCode) {
            Log.e(TAG, "onResolveFailed() "+errorCode);
        }
        @Override
        public void onServiceResolved(NsdServiceInfo serviceInfo) {
            Log.d(TAG, "onServiceResolved() "+serviceInfo);
        }
    }

}
