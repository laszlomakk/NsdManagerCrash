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
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "Application started");
        nsdManager = (NsdManager) getSystemService(Context.NSD_SERVICE);
        nsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, new CustomDiscoveryListener());
    }

    private void magic(){
        try {
            Field f = nsdManager.getClass().getDeclaredField("mAsyncChannel");
            f.setAccessible(true);
            AsyncChannel mAsyncChannel = (AsyncChannel) f.get(nsdManager);
            mAsyncChannel.disconnect();
            Log.d(TAG, "mAsyncChannel.disconnect() called");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        nsdManager = (NsdManager) getSystemService(Context.NSD_SERVICE);
    }

    private class CustomDiscoveryListener implements NsdManager.DiscoveryListener{
        @Override
        public void onStartDiscoveryFailed(String serviceType, int errorCode) {
            Log.e(TAG, "onStartDiscoveryFailed() "+serviceType+", "+errorCode);
        }
        @Override
        public void onStopDiscoveryFailed(String serviceType, int errorCode) {
            Log.e(TAG, "onStopDiscoveryFailed() "+serviceType+", "+errorCode);
        }
        @Override
        public void onDiscoveryStarted(String serviceType){
            Log.d(TAG, "onDiscoveryStarted(). type: "+serviceType);
        }
        @Override
        public void onDiscoveryStopped(String serviceType) {
            Log.d(TAG, "onDiscoveryStopped(). type: "+serviceType);
        }
        @Override
        public void onServiceFound(final NsdServiceInfo serviceInfo) {
            Log.d(TAG, "onServiceFound(). "+serviceInfo);
            nsdManager.resolveService(serviceInfo, new CustomResolveListener());
            magic();
        }
        @Override
        public void onServiceLost(NsdServiceInfo serviceInfo) {
            Log.d(TAG, "onServiceLost(). "+serviceInfo);
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
