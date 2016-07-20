package com.thesis.util;

import android.app.Application;
import android.content.Context;

import com.thesis.domain.Contact;
import com.thesis.domain.User;

import java.util.List;

/**
 * Created by yiding on 7/8/2016.
 */
public class CommonLibs {

    private static Application sApp;
    private static List<Contact> sContactList;
    private static List<User> sUserList;
    private static Contact sConversationContact;

    public static void setsApp(Application sApp) {
        CommonLibs.sApp = sApp;
    }

    public static Context getApplicationContext() {
        if (sApp != null) {
            return sApp.getApplicationContext();
        }
        return null;
    }

    public static void setsContactList(List<Contact> contactList){
        sContactList = contactList;
    }

    public static List<Contact> getsContactList(){
        return sContactList;
    }

    public static void setsUserList(List<User> userList){
        sUserList = userList;
    }

    public static List<User> getsUserList(){
        return sUserList;
    }

    public static Contact getsConversationContact(){
        return sConversationContact;
    }

    public static void setsConversationContact(Contact contact){
        sConversationContact = contact;
    }

}
