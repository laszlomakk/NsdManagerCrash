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
    private String myServiceName = "nameOfMyVeryCoolService";
    private static final int HARDCODED_PORT = 51498; //this would normally be bad practice

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "Application started");

        nsdManager = (NsdManager) getSystemService(Context.NSD_SERVICE);

        registerService();
    }

    private void registerService() {
        NsdServiceInfo serviceInfo  = new NsdServiceInfo();
        serviceInfo.setServiceName(myServiceName);
        serviceInfo.setServiceType(SERVICE_TYPE);
        serviceInfo.setPort(HARDCODED_PORT);
        nsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, new CustomRegistrationListener());
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

    private class CustomRegistrationListener implements NsdManager.RegistrationListener {

        @Override
        public void onServiceRegistered(NsdServiceInfo serviceInfo) {
            Log.d(TAG, "onServiceRegistered() "+serviceInfo);
            serviceInfo.setServiceType(SERVICE_TYPE);

            magic();
            nsdManager.resolveService(serviceInfo, new CustomResolveListener());
        }

        @Override
        public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
            Log.e(TAG, "onRegistrationFailed() "+serviceInfo+", "+errorCode);
        }

        @Override
        public void onServiceUnregistered(NsdServiceInfo serviceInfo) {
            Log.d(TAG, "onServiceUnregistered() "+serviceInfo);
        }

        @Override
        public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
            Log.e(TAG, "onUnregistrationFailed() "+serviceInfo+", "+errorCode);
        }
    }

}
