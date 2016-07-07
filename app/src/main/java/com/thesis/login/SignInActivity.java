package com.thesis.login;

import com.example.shakeshare.R;
import com.thesis.db.dao.UserDao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignInActivity extends Activity implements OnClickListener {
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
        Intent intent = new Intent(this, FirstActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void start_sign_in_activity() {
        String username = mEditTextUsername.getText().toString().trim();
        String password = mEditTextPassword.getText().toString().trim();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(SignInActivity.this,
                    "Please input username and password", 0).show();
            return;
        }
        UserDao dao = new UserDao(this);
        try {
            if (dao.findPassword(username).compareTo(password) != 0) {
                Toast.makeText(SignInActivity.this,
                        "Invalid username or password", 0).show();
                return;
            }
            if (dao.findPassword(username).compareTo(password) == 0) {
                Toast.makeText(SignInActivity.this, "Welcome back " + username,
                        0).show();
                Intent intent_contacts = new Intent(SignInActivity.this,
                        ContactsActivity.class);
                finish();
                intent_contacts.putExtra("username", username);
                startActivity(intent_contacts);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }
        } catch (Exception e) {
            Toast.makeText(SignInActivity.this,
                    "Something Wrong with Database", 0).show();
            return;
        }
    }
}
