package com.thesis.login;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.shakeshare.R;
import com.thesis.BaseActivity;
import com.thesis.domain.User;
import com.thesis.util.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class SignInActivity extends BaseActivity implements OnClickListener {
    private EditText mEditTextUsername;
    private EditText mEditTextPassword;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_activity);
        mEditTextUsername = (EditText) findViewById(R.id.et_username);
        mEditTextPassword = (EditText) findViewById(R.id.et_password);
        Button bt_back2_first = (Button) this.findViewById(R.id.bt_back2_first);
        Button bt_sign_in = (Button) this.findViewById(R.id.bt_sign_in);
        bt_back2_first.setOnClickListener(this);
        bt_sign_in.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_back2_first:
                back2_first_activity();
                break;
            case R.id.bt_sign_in:
                start_sign_in_activity();
                break;
            default:
                break;
        }
    }

    private void back2_first_activity() {
        finish();
    }

    private void start_sign_in_activity() {
        String username = mEditTextUsername.getText().toString().trim();
        String password = mEditTextPassword.getText().toString().trim();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(SignInActivity.this,
                    "Please input username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        User signInUser = new User(username, password, Utils.getDeviceIpAddress());
        checkUserExist(signInUser);
    }

    private void checkUserExist(final User user) {

        Backendless.Persistence.of(User.class).find(new AsyncCallback<BackendlessCollection<User>>() {
            @Override
            public void handleResponse(BackendlessCollection<User> response) {
                Log.d(Utils.TAG, "checkUserExist, handleResponse:" + response.toString());
                List<User> data = response.getData();
                if (data == null || data.size() == 0) {
                    sendToastOnUIThread("No user on the record, please create user.");
                } else {
                    for (User serverUser : data) {
                        if (serverUser.getName().equals(user.getName())&&serverUser.getPassword().equals(user.getPassword())) {
                            Log.d(Utils.TAG, "checkUserExist, user exist:");
                            signInSuccess(serverUser);
                            return;
                        }
                    }
                    signInFail(user);
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.d(Utils.TAG, "checkUserExist, handleFault:" + fault.toString());
            }
        });

    }

    private void signInFail(User user) {
        sendToastOnUIThread("SignIn Fail " + user.getName() + ", please try again");
    }

    private void signInSuccess(final User user) {
        //update user ipAddress
        user.setIpAddress(Utils.getDeviceIpAddress());
        Backendless.Persistence.save(user, new AsyncCallback<User>() {
            @Override
            public void handleResponse(User response) {
                Log.d(Utils.TAG, "signInSuccess, handleFault:" + response.toString());

                sendToastOnUIThread("Welcome back " + user.getName());
                Intent intent_contacts = new Intent(SignInActivity.this,
                        ContactsActivity.class);
                intent_contacts.putExtra("signInUser", response);
                startActivity(intent_contacts);
                finish();
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.d(Utils.TAG, "signInSuccess, handleFault:" + fault.toString());
            }
        });

    }

}
