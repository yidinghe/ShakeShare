package com.thesis.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by yiding on 7/7/2016.
 */
public class Utils {
    public static final String TAG = "ShakeShare";

    public static final String APP_ID = "94D83408-A83E-8A2E-FF8C-5980C2524500";

    public static final String SECRET_KEY = "2F1F2E09-7B33-B343-FF65-3F7322662800";

    public static final int REQUEST_CODE_GENERATE_KEY = 1001;

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String getDeviceIpAddress() {
        WifiManager wifiMan = (WifiManager) CommonLibs.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        int ipAddress = wifiInf.getIpAddress();
        String ip = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
        Log.d(Utils.TAG, "ip:" + ip);
        return ip;
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars).toLowerCase();
    }


    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));

        }
        return d;
    }

    public static String getSHA256Encode(String value) {
        if (value == null) {
            return "";
        }

        MessageDigest digest = null;
        String ret = value;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            digest.update(value.getBytes());
            byte[] userEmailBytes = digest.digest();
            ret = Base64.encodeToString(userEmailBytes, Base64.NO_WRAP);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static String getMD5Hash(String value) {
        if (value == null) {
            return "";
        }

        MessageDigest digest = null;
        String ret = value;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.update(value.getBytes());
            byte[] valueBytes = digest.digest();
            ret = bytesToHex(valueBytes).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return ret;
    }


    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
