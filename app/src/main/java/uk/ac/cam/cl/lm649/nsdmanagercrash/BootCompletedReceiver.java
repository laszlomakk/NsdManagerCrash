package uk.ac.cam.cl.lm649.nsdmanagercrash;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BootCompletedReceiver extends BroadcastReceiver {

    private static final String TAG = "BootCompletedRec";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive(). received intent.  action: " + intent.getAction());
        //Toast.makeText(context, "Booting Completed", Toast.LENGTH_LONG).show();

        // WARNING: if you run this code, your device will enter into a boot-loop
        //          recovering will be frustrating, so I suggest testing on a virtual device
        CustomApplication.magic();
    }
}
