package com.thesis.login;

import com.example.shakeshare.R;
import com.thesis.util.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;

import weborb.util.log.LogHelper;

public class FirstActivity extends Activity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity);
        Button bt_sign_in = (Button) this.findViewById(R.id.bt_sign_in);
        Button bt_sign_up = (Button) this.findViewById(R.id.bt_sign_up);
        Button bt_users = (Button) this.findViewById(R.id.bt_users);
        bt_sign_in.setOnClickListener(this);
        bt_sign_up.setOnClickListener(this);
        bt_users.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_sign_in:
                start_sign_in_activity();
                break;
            case R.id.bt_sign_up:
                start_sign_up_activity();
                break;
            case R.id.bt_users:
                start_users_activity();
                break;
            default:
                break;
        }
    }

    private void start_sign_up_activity() {
        Intent intent_sign_up = new Intent(FirstActivity.this,
                SignUpActivity.class);
        startActivity(intent_sign_up);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void start_sign_in_activity() {
        Intent intent_sign_in = new Intent(FirstActivity.this,
                SignInActivity.class);
        startActivity(intent_sign_in);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void start_users_activity() {
        Intent intent_users = new Intent(FirstActivity.this,
                UsersActivity.class);
        startActivity(intent_users);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.first, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
