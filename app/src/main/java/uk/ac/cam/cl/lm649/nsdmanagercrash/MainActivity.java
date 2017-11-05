package uk.ac.cam.cl.lm649.nsdmanagercrash;

import android.app.Activity;

import static java.lang.Thread.sleep;

public class MainActivity extends Activity {

    @Override
    protected void onStart() {
        super.onStart();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Android seems to need some time to register BootCompletedReceiver
                    // if we crash the system too soon with magic(), it won't enter a boot-loop
                    // hence we sleep here...
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    CustomApplication.magic();
                }
            }
        });
        t.run();

    }

}
