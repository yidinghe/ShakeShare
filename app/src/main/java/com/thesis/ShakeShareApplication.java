package com.thesis;

import android.app.Application;
import android.util.Log;

import com.backendless.Backendless;
import com.thesis.util.Utils;

/**
 * Created by yiding on 7/7/2016.
 */
public class ShakeShareApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initBackend();
    }

    private void initBackend() {
        Log.d(Utils.TAG, "initBackend()");
        String appVersion = "v1";
        Backendless.initApp(this, Utils.APP_ID, Utils.SECRET_KEY, appVersion);
    }
}
