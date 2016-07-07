package com.thesis.login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.shakeshare.R;
import com.thesis.db.dao.Contact;
import com.thesis.db.dao.ContactDao;
import com.thesis.shakeshare.EntryActivity;
import com.thesis.sms.MessageActivity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ContactsActivity extends Activity implements OnClickListener {
	private ListView lv;
	private List<Contact> contacts;
	private ContactDao dao;
	private String username;
	private List<Map<String, Object>> data;
	private Map<String, Object> map;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts_activity);

		Intent intent = getIntent();
		username = intent.getStringExtra("username");

		Button bt_back2_first = (Button) this.findViewById(R.id.bt_back2_first);
		Button bt_add_contact = (Button) this.findViewById(R.id.bt_add_contact);
		bt_back2_first.setOnClickListener(this);
		bt_add_contact.setOnClickListener(this);

		dao = new ContactDao(this);
		contacts = dao.findAll(username);
		lv = (ListView) findViewById(R.id.lv);
		/*
		 * LinearLayout ll_root = (LinearLayout) findViewById(R.id.ll_root);
		 * ContactDao dao = new ContactDao(this); List<Contact> contacts =
		 * dao.findAll(); for(Contact contact:contacts){ String info =
		 * contact.toString(); TextView tv = new TextView(this);
		 * tv.setTextSize(20); tv.setTextColor(Color.BLACK); tv.setText(info);
		 * ll_root.addView(tv); }
		 */

		// lv.setAdapter(new MyAdapter());

		data = new ArrayList<Map<String, Object>>();

		putContact2Map();

		lv.setAdapter(new SimpleAdapter(this, data, R.layout.list_item,
				new String[] { "contactname", "iconid" }, new int[] { R.id.tv,
						R.id.iv }));

		this.registerForContextMenu(lv);
	}

	// private class MyAdapter extends BaseAdapter {
	//
	// @Override
	// public int getCount() {
	// return contacts.size();
	// }
	//
	// @Override
	// public Object getItem(int position) {
	// return null;
	// }
	//
	// @Override
	// public long getItemId(int position) {
	// return 0;
	// }
	//
	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	// TextView tv = new TextView(getApplicationContext());
	// tv.setTextSize(40);
	// tv.setTextColor(Color.BLACK);
	// Contact contact = contacts.get(position);
	// tv.setText(contact.toStringNameOnly());
	// return tv;
	// }
	// }

	private void putContact2Map() {
		for (Contact contact : contacts) {
			map = new HashMap<String, Object>();
			map.put("contactname", contact.getName() + contact.getKey());
			if (contact.getKey() == null) {
				map.put("iconid", R.drawable.btn_rating_star_off_normal);
			} else {
				map.put("iconid", R.drawable.btn_rating_star_off_pressed);
			}
			data.add(map);
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_back2_first:
			back2_first_activity();
			break;
		case R.id.bt_add_contact:
			start_add_contact_activity();
			break;
		default:
			break;
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Please select one");
		menu.add(0, Menu.FIRST, 0, "Edit Contact Name");
		menu.add(0, Menu.FIRST + 1, 0, "Delete Contact");
		menu.add(0, Menu.FIRST + 2, 0, "Generate Key");
		menu.add(0, Menu.FIRST + 3, 0, "Send Message");
	}

	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo menuInfo;
		menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		final String contactname = contacts.get(menuInfo.position).getName();
		switch (item.getItemId()) {
		case Menu.FIRST:
			editContactName(contactname);
			break;
		case Menu.FIRST + 1:
			deleteContact(contactname);
			break;
		case Menu.FIRST + 2:
			Intent intent = new Intent(this, EntryActivity.class);
			Bundle extras = new Bundle();
			extras.putString("username", username);
			extras.putString("contactname", contactname);
			intent.putExtras(extras);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			break;
		case Menu.FIRST + 3:
			Intent intent_message = new Intent(this, MessageActivity.class);
			intent_message.putExtra("name", username);
			startActivity(intent_message);
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			break;
		}
		return super.onContextItemSelected(item);
	}

	private void editContactName(final String contactname) {
		AlertDialog.Builder ad_add_contact = new AlertDialog.Builder(this);
		ad_add_contact.setTitle("Please input contact name");
		final EditText input = new EditText(this);
		ad_add_contact.setView(input);
		ad_add_contact.setCancelable(false);
		ad_add_contact.setPositiveButton("Save",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						String newname = input.getText().toString();
						if (!dao.find(username, newname)) {
							dao.updateName(username,contactname,newname);
						} else {
							Toast.makeText(ContactsActivity.this,
									"Contact already exists", 0).show();
						}
						onCreate(null);
					}
				});
		ad_add_contact.setNegativeButton("CANCEL",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				});
		AlertDialog alertDialog = ad_add_contact.create();
		Window window = alertDialog.getWindow();
		window.setGravity(Gravity.CENTER_VERTICAL);
		window.setWindowAnimations(android.R.anim.fade_in);
		alertDialog.show();
	}

	private void deleteContact(final String contactname) {
		new AlertDialog.Builder(this)
		.setTitle("Are you sure to delete " + contactname + "?")
		.setPositiveButton( "Yes",
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog,
					int whichButton) {
				dao.delete(username, contactname);
				onCreate(null);
			}
		}).setNegativeButton("Cancle", null).show();
	}
	
	private void back2_first_activity() {
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	private void start_add_contact_activity() {
		AlertDialog.Builder ad_add_contact = new AlertDialog.Builder(this);
		ad_add_contact.setTitle("Please input contact name");
		final EditText input = new EditText(this);
		ad_add_contact.setView(input);
		ad_add_contact.setCancelable(false);
		ad_add_contact.setPositiveButton("Save",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						String contactname = input.getText().toString();
						if (!dao.find(username, contactname)) {
							dao.addContactName(contactname, username);
						} else {
							Toast.makeText(ContactsActivity.this,
									"Contact already exists", Toast.LENGTH_SHORT).show();
						}
						onCreate(null);
					}
				});
		ad_add_contact.setNegativeButton("Cancle",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						onCreate(null);
					}
				});
		// ad_add_contact.create().show();
		AlertDialog alertDialog = ad_add_contact.create();
		Window window = alertDialog.getWindow();
		window.setGravity(Gravity.CENTER_VERTICAL);
		window.setWindowAnimations(android.R.anim.fade_in);
		alertDialog.show();
	}
}
