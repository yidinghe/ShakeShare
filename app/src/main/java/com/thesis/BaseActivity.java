package com.thesis;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.thesis.util.Utils;

/**
 * Created by yiding on 7/7/2016.
 */
public class BaseActivity extends Activity {

    protected void sendToastOnUIThread(final String message) {

        Log.d(Utils.TAG, "sendToastOnUIThread:" + message);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivity.this, message, Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
}
