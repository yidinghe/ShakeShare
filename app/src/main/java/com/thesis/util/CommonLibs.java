package com.thesis.util;

import android.app.Application;
import android.content.Context;

/**
 * Created by yiding on 7/8/2016.
 */
public class CommonLibs {

    private static Application sApp;

    public static void setsApp(Application sApp) {
        CommonLibs.sApp = sApp;
    }

    public static Context getApplicationContext() {
        if (sApp != null) {
            return sApp.getApplicationContext();
        }
        return null;
    }



}
