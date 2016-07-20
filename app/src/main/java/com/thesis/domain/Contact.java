package com.thesis.domain;

import android.text.TextUtils;

public class Contact {

    private User user;
    private String masterKey;
    private boolean isStartConversation;
    private String contactName;

    public Contact(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMasterKey() {
        return masterKey;
    }

    public void setMasterKey(String masterKey) {
        this.masterKey = masterKey;
    }

    public boolean isStartConversation() {
        return isStartConversation;
    }

    public void setStartConversation(boolean startConversation) {
        isStartConversation = startConversation;
    }

    public boolean isKeyGenerated() {
        return !TextUtils.isEmpty(masterKey);
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
}
