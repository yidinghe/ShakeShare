package com.thesis.login;

import com.example.shakeshare.R;
import com.thesis.db.dao.ContactSQLiteOpenHelper;
import com.thesis.db.dao.UserDao;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends Activity implements OnClickListener {
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
		Intent intent = new Intent(this, FirstActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	private void start_sign_up_activity() {
		String username = mEditTextUsername.getText().toString().trim();
		String password1 = mEditTextPassword1.getText().toString().trim();
		String password2 = mEditTextPassword2.getText().toString().trim();

		if (TextUtils.isEmpty(username)) {
			Toast.makeText(SignUpActivity.this, "Please input username", 0)
					.show();
			return;
		}

		else if (TextUtils.isEmpty(password1)) {
			Toast.makeText(SignUpActivity.this, "Please input password", 0)
					.show();
			return;
		}

		else if (TextUtils.isEmpty(password2)) {
			Toast.makeText(SignUpActivity.this, "Please confirm password", 0)
					.show();
			return;
		}

		if (password1.compareTo(password2) != 0) {
			Toast.makeText(SignUpActivity.this, "Confirm password fail", 0)
					.show();
			return;
		}

		try {
			UserDao dao = new UserDao(this);
			if (dao.find(username)) {
				Toast.makeText(SignUpActivity.this,
						"Username is already exist", 0).show();
				return;
			}
			dao.add(username, password1);
			create_table_usercontact(username);
			Toast.makeText(SignUpActivity.this, "Welcome " + username, 0)
					.show();
			Intent intent_contacts = new Intent(SignUpActivity.this,
					ContactsActivity.class);
			intent_contacts.putExtra("username", username);
			startActivity(intent_contacts);
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
		} catch (Exception e) {
			Toast.makeText(SignUpActivity.this, "Create accout fail", 0).show();
			return;
		}
	}
	
	private void create_table_usercontact(String username) {
		ContactSQLiteOpenHelper helper = new ContactSQLiteOpenHelper(
				SignUpActivity.this);
		SQLiteDatabase db = helper.getWritableDatabase();
		String sql = "create table if not exists " + username
				+ "(name varchar(20), key varchar(20))";
		db.execSQL(sql);
	}
}
