package com.thesis.login;

import java.util.List;

import com.example.shakeshare.R;
import com.thesis.domain.User;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class UsersActivity extends Activity {
	private ListView lv;
	private List<User> users;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.users_activity);
		lv = (ListView) findViewById(R.id.lv);

		lv.setAdapter(new MyAdapter());
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return users.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv = new TextView(getApplicationContext());
			tv.setTextSize(20);
			tv.setTextColor(Color.BLACK);
			User user = users.get(position);
			tv.setText(user.toString());
			return tv;
		}
	}

	public void click_back2_welcome(View view) {
		Intent intent = new Intent(this, FirstActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	public void delete_users(View view) {
		Toast.makeText(UsersActivity.this, "All the users have been deleted", 0)
				.show();
		onCreate(null);
	}

}
