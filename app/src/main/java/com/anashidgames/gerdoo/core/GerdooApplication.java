package com.anashidgames.gerdoo.core;

import android.app.Application;
import android.util.Log;

import com.anashidgames.gerdoo.core.service.GerdooServer;

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
                throw new RuntimeException(ex);
            }
        });

        GerdooServer.INSTANCE.setContext(this);

//        FontsOverride.setDefaultFont(this, "DEFAULT", "irsans.ttf");
    }


}
