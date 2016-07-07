package com.thesis.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.thesis.util.Utils;

public class User implements Parcelable {
    private String name;
    private String password;
    private String ipAddress;
    private String ownerId;

    public User() {
        super();
    }

    public User(String name, String password) {
        super();
        this.name = name;
        this.password = password;
        ownerId = Utils.getMD5Hash(name);
    }

    public User(String name, String password, String ipAddress) {
        this.name = name;
        this.password = password;
        this.ipAddress = ipAddress;
        ownerId = Utils.getMD5Hash(name);
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String toString() {
        return "User [name=" + name + ", password=" + password + "]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.password);
        dest.writeString(this.ipAddress);
        dest.writeString(this.ownerId);
    }

    protected User(Parcel in) {
        this.name = in.readString();
        this.password = in.readString();
        this.ipAddress = in.readString();
        this.ownerId = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
