package uk.ac.cam.cl.lm649.nsdmanagercrash;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootCompletedReceiver extends BroadcastReceiver {

    private static final String TAG = "BootCompletedRec";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive(). received intent.  action: " + intent.getAction());
    }
}
