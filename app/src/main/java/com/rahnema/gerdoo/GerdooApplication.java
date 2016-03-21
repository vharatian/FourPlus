package com.rahnema.gerdoo;

import android.app.Application;
import android.util.Log;

/**
 * Created by psycho on 3/14/16.
 */
public class GerdooApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                Log.e("psycho", "error", ex);
            }
        });

//        FontsOverride.setDefaultFont(this, "DEFAULT", "irsans.ttf");
    }
}
