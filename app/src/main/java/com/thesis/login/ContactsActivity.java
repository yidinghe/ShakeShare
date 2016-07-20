package com.thesis.login;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.shakeshare.R;
import com.thesis.domain.Contact;
import com.thesis.domain.User;
import com.thesis.shakeshare.EntryActivity;
import com.thesis.util.CommonLibs;
import com.thesis.util.Utils;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ContactsActivity extends Activity implements OnClickListener {
    private ListView lv;
    private MyAdapter myAdapter;

    private User mUser;

    private Contact mContact;

    private List<Contact> contacts = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_activity);

        Intent intent = getIntent();
        mUser = intent.getParcelableExtra("signInUser");

        Button bt_back2_first = (Button) this.findViewById(R.id.bt_back2_first);
        Button bt_add_contact = (Button) this.findViewById(R.id.bt_add_contact);
        bt_back2_first.setOnClickListener(this);
        bt_add_contact.setOnClickListener(this);

        lv = (ListView) findViewById(R.id.lv);
        myAdapter = new MyAdapter();

        getAllUsers();

    }

    private void getAllUsers() {

        if (CommonLibs.getsContactList()!=null&&CommonLibs.getsContactList().size()>0){
            Log.d(Utils.TAG, "getAllUsers, already constructed the contact list, no need to fetch from online. list size:"+CommonLibs.getsContactList().size());
            contacts = CommonLibs.getsContactList();
            updateListView();
            return;
        }

        Backendless.Persistence.of(User.class).find(new AsyncCallback<BackendlessCollection<User>>() {
            @Override
            public void handleResponse(BackendlessCollection<User> response) {
                Log.d(Utils.TAG, "getAllUsers, handleResponse:" + response.toString());
                List<User> data = response.getData();
                CommonLibs.setsUserList(data);
                for (User serverUser : data) {
                    if (!serverUser.getOwnerId().equals(mUser.getOwnerId())) {
                        Contact contact = new Contact(serverUser);
                        contacts.add(contact);
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateListView();
                    }
                });
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.d(Utils.TAG, "getAllUsers, handleFault:" + fault.toString());
            }
        });
    }

    private void updateListView() {
        CommonLibs.setsContactList(contacts);
        lv.setAdapter(myAdapter);
        this.registerForContextMenu(lv);
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return contacts.size();
        }

        @Override
        public Object getItem(int position) {
            return contacts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.list_item, null);
            TextView tv_username = (TextView) view.findViewById(R.id.tv_username);
            TextView tv_contactname = (TextView) view.findViewById(R.id.tv_contactname);
            TextView tv_ipAddress = (TextView) view.findViewById(R.id.tv_ipAddress);
            TextView tv_sharedKey = (TextView) view.findViewById(R.id.tv_sharedKey);

            Contact contact = contacts.get(position);

            User contactUser = contact.getUser();

            if (!TextUtils.isEmpty(contactUser.getName())){
                tv_username.setText("userName:"+contactUser.getName());
            }

            if (!TextUtils.isEmpty(contactUser.getIpAddress())){
                tv_ipAddress.setText("ipAddress:"+contactUser.getIpAddress());
            }

            if (!TextUtils.isEmpty(contact.getContactName())){
                tv_contactname.setText("contactName:"+contact.getContactName());
            }else{
                tv_contactname.setText("contactName: Not set.");
            }

            if (contact.isKeyGenerated()&&(!TextUtils.isEmpty(contact.getMasterKey()))){
                tv_sharedKey.setText("sharedKey:"+contact.getMasterKey());
                tv_sharedKey.setTextColor(getResources().getColor(R.color.text_mark));
            }else{
                tv_sharedKey.setText("sharedKey: Not set.");
            }

            if (contact.isStartConversation()){
                tv_username.setTextColor(getResources().getColor(R.color.text_mark));
            }else{
                tv_username.setTextColor(getResources().getColor(R.color.text_regular));
            }


            return view;
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
        menu.add(0, Menu.FIRST + 1, 0, "Release Contact");
        menu.add(0, Menu.FIRST + 2, 0, "Generate Key");
        menu.add(0, Menu.FIRST + 3, 0, "Send Message");
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo;
        menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        mContact = contacts.get(menuInfo.position);
        final User contactUser = contacts.get(menuInfo.position).getUser();
        switch (item.getItemId()) {
            case Menu.FIRST:
                    editContactName(mContact);
                break;
            case Menu.FIRST + 1:
                    releaseContact(mContact);
                break;
            case Menu.FIRST + 2:
                startGenerateKeyActivity(contactUser);
                break;
            case Menu.FIRST + 3:
//                Intent intent_message = new Intent(this, MessageActivity.class);
//                intent_message.putExtra("name", username);
//                startActivity(intent_message);
//                overridePendingTransition(R.anim.slide_in_right,
//                        R.anim.slide_out_left);
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void startGenerateKeyActivity(User contactUser) {
        Intent intent = new Intent(this, EntryActivity.class);
        intent.putExtra("user", mUser);
        intent.putExtra("contactUser", contactUser);
        startActivityForResult(intent, Utils.REQUEST_CODE_GENERATE_KEY);
        overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case Activity.RESULT_OK:
                Log.d(Utils.TAG, "onActivityResult,Activity.RESULT_OK.");
                String generateKey = data.getStringExtra("masterKey");
                Log.d(Utils.TAG, "generateKey:"+generateKey);
                mContact.setMasterKey(generateKey);
                myAdapter.notifyDataSetChanged();
                break;
            case Activity.RESULT_CANCELED:
                Log.d(Utils.TAG, "onActivityResult,Activity.RESULT_CANCELED.");
                break;
        }
    }

    private void editContactName(final Contact contact) {
        AlertDialog.Builder ad_add_contact = new AlertDialog.Builder(this);
        ad_add_contact.setTitle("Please input contact name");
        final EditText input = new EditText(this);
        ad_add_contact.setView(input);
        ad_add_contact.setCancelable(false);
        ad_add_contact.setPositiveButton("SAVE",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        contact.setContactName(input.getText().toString().trim());
                        myAdapter.notifyDataSetChanged();
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

    private void releaseContact(final Contact contact) {
        new AlertDialog.Builder(this)
                .setTitle("Are you sure to drop conversation " + contact.getContactName() + "?")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                contact.setStartConversation(false);

                            }
                        }).setNegativeButton("CANCEL", null).show();
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
        ad_add_contact.setPositiveButton("SAVE",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String contactname = input.getText().toString();
                        onCreate(null);
                    }
                });
        ad_add_contact.setNegativeButton("CONCEL",
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
