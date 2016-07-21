package com.thesis.sms;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
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

public class MessageActivity extends BaseActivity implements Runnable {

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

    private ServerSocket mServerSocketObj;
    private Socket mServerSocket;
    private BufferedReader mServerIn;
    private PrintWriter mServerOut;

    private String mMessageContent;

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

        new Thread(MessageActivity.this).start();

        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Sending message to web socket server
                sendMessage(inputMsg.getText().toString().trim());
                // Clearing the input filed once message was sent
                inputMsg.setText("");
            }
        });

        listMessages = new ArrayList<>();

        mMessagesListAdapter = new MessagesListAdapter(this, listMessages);
        listViewMessages.setAdapter(mMessagesListAdapter);

    }

    private void startServerSocket() {
        try {
            Log.d(Utils.TAG,"startServerSocket:");
            mServerSocketObj = new ServerSocket(Utils.SOCKET_PORT);
            mServerSocket = mServerSocketObj.accept();
            mServerIn = new BufferedReader(new InputStreamReader(mServerSocket
                    .getInputStream()));
            mServerOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    mServerSocket.getOutputStream())), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startClientSocket() {
        try {
            Log.d(Utils.TAG,"startClientSocket:");
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
        Log.d(Utils.TAG,"sendMessageFromServerToClient:"+ messageCipherText);
        if (mServerSocket!=null&&mServerSocket.isConnected()) {
            if (!mServerSocket.isOutputShutdown()) {
                mServerOut.println(messageCipherText);
            }
        }
    }

    private void sendMessageFromClientToServer(String messageCipherText) {
        Log.d(Utils.TAG,"sendMessageFromClientToServer:"+ messageCipherText);
        if (mClientSocket!=null&&mClientSocket.isConnected()) {
            if (!mClientSocket.isOutputShutdown()) {
                mClientOut.println(messageCipherText);
            }
        }
    }

    private void addMessageToListView(final Message message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMessagesListAdapter.addMessage(message);
                mMessagesListAdapter.notifyDataSetChanged();
                playBeep();
            }
        });
    }

    private Message constructMessage(String message) {
        Message sendMessage = new Message();
        sendMessage.setFromUser(mUser);
        sendMessage.setToUser(mContact.getUser());
        sendMessage.setReceived(false);
        sendMessage.setMessageContent(message);
        return sendMessage;
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


    @Override
    public void run() {
        try {
            Log.d(Utils.TAG,"run:");
            if (mIsClient){
                startClientSocket();
            }else{
                startServerSocket();
            }

            while (true) {
                Socket socket = null;
                BufferedReader in = null;
                if (mIsClient){
                    socket = mClientSocket;
                    in = mClientIn;
                }else{
                    socket = mServerSocket;
                    in = mServerIn;
                }
                if (!socket.isClosed()) {
                    if (socket.isConnected()) {
                        if (!socket.isInputShutdown()) {
                            if ((mMessageContent = in.readLine()) != null) {
                                mMessageContent += "\n";
                                receiveMessage(mMessageContent);
                            } else {

                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void receiveMessage(String mMessageContent) {
        Log.d(Utils.TAG,"receive message,mMessageContent:"+ mMessageContent);
        String messageContent = AES.decrypt(mContact.getMasterKey(), mMessageContent);
        Log.d(Utils.TAG,"receive ,messagePlainText:"+ messageContent);
        Gson gson = new Gson();
        Message message = gson.fromJson(messageContent, Message.class);
        //receiveMessage force set when received
        message.setReceived(true);
        addMessageToListView(message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mIsClient) {
            try {
                if (mClientIn!=null)
                    mClientIn.close();
                if (mClientOut!=null)
                    mClientOut.close();
                if (mClientSocket!=null){
                    mClientSocket.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else{
            try {
                if (mServerIn!=null)
                    mServerIn.close();
                if (mServerOut!=null)
                    mServerOut.close();
                if (mServerSocket!=null){
                    mServerSocket.close();
                }
                if (mServerSocketObj!=null){
                    mServerSocketObj.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
