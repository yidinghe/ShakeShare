package com.thesis.login;

import java.util.List;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.shakeshare.R;
import com.thesis.domain.Contact;
import com.thesis.domain.User;
import com.thesis.util.CommonLibs;
import com.thesis.util.Utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

		getAllUsers();

	}

	private void getAllUsers() {
		if (CommonLibs.getsUserList()!=null && CommonLibs.getsUserList().size() > 0){
			Log.d(Utils.TAG, "getAllUsers, already fetched, no need to fetch from online. list size:"+ CommonLibs.getsUserList().size());
			users = CommonLibs.getsUserList();
			lv.setAdapter(new MyAdapter());
		}else{
			Backendless.Persistence.of(User.class).find(new AsyncCallback<BackendlessCollection<User>>() {
				@Override
				public void handleResponse(BackendlessCollection<User> response) {
					Log.d(Utils.TAG, "getAllUsers, handleResponse:" + response.toString());
					List<User> data = response.getData();
					CommonLibs.setsUserList(data);
					users = data;
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							lv.setAdapter(new MyAdapter());
						}
					});
				}

				@Override
				public void handleFault(BackendlessFault fault) {
					Log.d(Utils.TAG, "getAllUsers, handleFault:" + fault.toString());
				}
			});
		}
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
		finish();
	}

	public void delete_users(View view) {
		Toast.makeText(UsersActivity.this, "All the users have been deleted", 0)
				.show();
		onCreate(null);
	}

}
