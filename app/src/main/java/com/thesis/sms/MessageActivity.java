package com.thesis.sms;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shakeshare.R;
import com.google.gson.Gson;
import com.thesis.BaseActivity;
import com.thesis.domain.Contact;
import com.thesis.domain.Message;
import com.thesis.domain.User;
import com.thesis.security.AES;
import com.thesis.util.CommonLibs;
import com.thesis.util.Utils;

public class MessageActivity extends BaseActivity {

    // LogCat tag
    private static final String TAG = MessageActivity.class.getSimpleName();

    private Button btnSend;
    private EditText inputMsg;


    // Chat messages list mMessagesListAdapter
    private MessagesListAdapter mMessagesListAdapter;
    private List<Message> listMessages;
    private ListView listViewMessages;



    private User mUser;

    private Contact mContact;
    private boolean mIsClient;

    private Socket mClientSocket;
    private BufferedReader mClientIn;
    private PrintWriter mClientOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSend = (Button) findViewById(R.id.btnSend);
        inputMsg = (EditText) findViewById(R.id.inputMsg);
        listViewMessages = (ListView) findViewById(R.id.list_view_messages);


        // Getting the person name from previous screen
        Intent intent = getIntent();
        mUser = intent.getParcelableExtra("user");
        mContact = CommonLibs.getsConversationContact();
        mIsClient = intent.getBooleanExtra("isClient", true);

        if (mIsClient){
            startClientSocket();
        }else{
            startServerSocket();
        }

        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Sending message to web socket server


                // Clearing the input filed once message was sent
                inputMsg.setText("");
            }
        });

        listMessages = new ArrayList<>();

        mMessagesListAdapter = new MessagesListAdapter(this, listMessages);
        listViewMessages.setAdapter(mMessagesListAdapter);

    }

    private void startServerSocket() {

    }

    private void startClientSocket() {
        try {
            mClientSocket = new Socket(mContact.getUser().getIpAddress(), Utils.SOCKET_PORT);
            mClientIn = new BufferedReader(new InputStreamReader(mClientSocket
                    .getInputStream()));
            mClientOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    mClientSocket.getOutputStream())), true);
        } catch (IOException e) {
            e.printStackTrace();
            sendToastOnUIThread(e.getMessage());
        }
    }

    private void sendMessage(String messageContent){
        Message message = constructMessage(messageContent);

        addMessageToListView(message);

        Gson gson = new Gson();
        String messagePlaintext = gson.toJson(message);

        Log.d(Utils.TAG,"sendMessage,messagePlaintext:"+ messagePlaintext);
        String messageCipherText = AES.encrypt(mContact.getMasterKey(),messagePlaintext);
        Log.d(Utils.TAG,"sendMessage,messageCipherText:"+ messageCipherText);
        if (mIsClient){
            sendMessageFromClientToServer(messageCipherText);
        }else{
            sendMessageFromServerToClient(messageCipherText);
        }

    }

    private void sendMessageFromServerToClient(String messageCipherText) {

    }

    private void sendMessageFromClientToServer(String messageCipherText) {

    }

    private void addMessageToListView(Message message) {
        mMessagesListAdapter.addMessage(message);
        mMessagesListAdapter.notifyDataSetChanged();
    }

    private Message constructMessage(String message) {
        Message sendMessage = new Message();
        sendMessage.setFromUser(mUser);
        sendMessage.setToUser(mContact.getUser());
        sendMessage.setReceived(false);
        sendMessage.setMessageContent(message);
        return sendMessage;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    /**
     * Appending message to list view
     */
    private void appendMessage(final Message m) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                listMessages.add(m);

                mMessagesListAdapter.notifyDataSetChanged();

                // Playing device's notification
                playBeep();
            }
        });
    }

    private void showToast(final String message) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message,
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    /**
     * Plays device's default notification sound
     */
    public void playBeep() {

        try {
            Uri notification = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
                    notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}
