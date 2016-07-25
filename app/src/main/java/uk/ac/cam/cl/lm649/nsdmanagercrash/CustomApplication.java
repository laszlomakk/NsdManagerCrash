package uk.ac.cam.cl.lm649.nsdmanagercrash;

import android.app.Application;
import android.content.Context;
import android.net.nsd.NsdManager;
import android.util.Log;

import com.android.internal.util.AsyncChannel;

import java.lang.reflect.Field;

public class CustomApplication extends Application {

    private final static String TAG = "CustomApplication";

    protected NsdManager nsdManager;

    @Override
    public void onCreate() {
        super.onCreate();
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
