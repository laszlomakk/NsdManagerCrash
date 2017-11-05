package uk.ac.cam.cl.lm649.nsdmanagercrash;

import android.app.Application;
import android.content.Context;
import android.net.nsd.NsdManager;
import android.util.Log;

import com.android.internal.util.AsyncChannel;

import java.lang.reflect.Field;

public class CustomApplication extends Application {

    private final static String TAG = "CustomApplication";

    protected static NsdManager nsdManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Application started");

        nsdManager = (NsdManager) getSystemService(Context.NSD_SERVICE);
    }

    protected static void magic(){
        Log.d(TAG, "entered magic()");
        try {
            Field f = nsdManager.getClass().getDeclaredField("mAsyncChannel");
            f.setAccessible(true);
            AsyncChannel mAsyncChannel = (AsyncChannel) f.get(nsdManager);
            Log.d(TAG, "calling mAsyncChannel.sendMessage()");
            mAsyncChannel.sendMessage(NsdManager.NATIVE_DAEMON_EVENT);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Finished hacks.");
    }

}
