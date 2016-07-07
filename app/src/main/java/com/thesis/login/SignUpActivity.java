package com.thesis.login;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;
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

public class SignUpActivity extends BaseActivity implements OnClickListener {
    private EditText mEditTextUsername;
    private EditText mEditTextPassword1;
    private EditText mEditTextPassword2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);
        mEditTextUsername = (EditText) findViewById(R.id.et_username);
        mEditTextPassword1 = (EditText) findViewById(R.id.et_password1);
        mEditTextPassword2 = (EditText) findViewById(R.id.et_password2);
        Button bt_back2_first = (Button) this.findViewById(R.id.bt_back2_first);
        Button bt_sign_up = (Button) this.findViewById(R.id.bt_sign_up);
        bt_back2_first.setOnClickListener(this);
        bt_sign_up.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_back2_first:
                back2_first_activity();
                break;
            case R.id.bt_sign_up:
                start_sign_up_activity();
                break;
            default:
                break;
        }
    }

    private void back2_first_activity() {
        finish();
    }

    private void start_sign_up_activity() {
        String username = mEditTextUsername.getText().toString().trim();
        String password1 = mEditTextPassword1.getText().toString().trim();
        String password2 = mEditTextPassword2.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(SignUpActivity.this, "Please input username", 0)
                    .show();
            return;
        } else if (TextUtils.isEmpty(password1)) {
            Toast.makeText(SignUpActivity.this, "Please input password", 0)
                    .show();
            return;
        } else if (TextUtils.isEmpty(password2)) {
            Toast.makeText(SignUpActivity.this, "Please confirm password", 0)
                    .show();
            return;
        }

        if (password1.compareTo(password2) != 0) {
            Toast.makeText(SignUpActivity.this, "Confirm password fail", 0)
                    .show();
            return;
        }

        User user = new User(username, password1, Utils.getDeviceIpAddress(this));

        checkUserExist(user);

    }

    private void checkUserExist(final User user) {

        Backendless.Persistence.of(User.class).find(new AsyncCallback<BackendlessCollection<User>>() {
            @Override
            public void handleResponse(BackendlessCollection<User> response) {
                Log.d(Utils.TAG, "checkUserExist, handleResponse:" + response.toString());
                List<User> data = response.getData();
                if (data == null || data.size() == 0) {
                    createNewUser(user);
                } else {
                    for (User serverUser : data) {
                        if (serverUser.getOwnerId().equals(user.getOwnerId())) {
                            Log.d(Utils.TAG, "checkUserExist, user exist:");
                            sendToastOnUIThread("User Exist, Please signIn");
                            return;
                        }
                    }
                    createNewUser(user);
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.d(Utils.TAG, "checkUserExist, handleFault:" + fault.toString());
            }
        });

    }

    private void createNewUser(final User user) {
        Backendless.Persistence.of(User.class).save(user, new AsyncCallback<User>() {
            @Override
            public void handleResponse(User response) {
                Log.d(Utils.TAG, "createNewUser, handleResponse:" + response.toString());
                sendToastOnUIThread("createNewUser success, welcome " + user.getName());
                //TODO GO TO Contacts Page
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.d(Utils.TAG, "createNewUser, handleFault:" + fault.toString());
            }
        });
    }


}
