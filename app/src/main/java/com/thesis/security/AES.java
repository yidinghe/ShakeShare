package com.thesis.security;

import android.util.Log;

import com.thesis.util.Utils;

import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by yidinghe on 7/20/16.
 */
public class AES {

    private static final byte[] IV = "01234567890ABCDE".getBytes();

    //TODO remove hardcode key logic and use the real key
    private static final String HARD_CODE_KEY = "090907070808";

    public static String encrypt(String masterKey, String clearText) {
        Log.d(Utils.TAG,"encrypt,masterKey:"+masterKey);
        Log.d(Utils.TAG,"encrypt,cleartext:"+clearText);
        byte[] result = null;
        try {
            result = aesGcmEncrypt(getRawKey(HARD_CODE_KEY), clearText.getBytes(),IV);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String content = Utils.bytesToHex(result);
        Log.d(Utils.TAG,"encrypt,result:"+content);
        return content;

    }

    public static String decrypt(String masterKey, String encrypted) {
        Log.d(Utils.TAG,"decrypt,masterKey:"+masterKey);
        Log.d(Utils.TAG,"decrypt,encrypted:"+encrypted);
        try {
            byte[] enc = Utils.hexStringToBytes(encrypted);
            byte[] result = aesGcmDecrypt(getRawKey(HARD_CODE_KEY), enc, IV);
            String content = new String(result);
            Log.d(Utils.TAG,"decrypt,result:"+content);
            return content;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private static SecretKey getRawKey(String seed) throws Exception {
        int keyLength = 128;
        byte[] keyBytes = new byte[keyLength / 8];
        Arrays.fill(keyBytes, (byte) 0x0);

        byte[] passwordBytes = seed.getBytes("UTF-8");
        int length = passwordBytes.length < keyBytes.length ? passwordBytes.length
                : keyBytes.length;
        System.arraycopy(passwordBytes, 0, keyBytes, 0, length);
        SecretKey key = new SecretKeySpec(keyBytes, "AES");

        return key;
    }

    private static byte[] aesCbcEncrypt(SecretKey secretKey, byte[] clear) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(
                IV));
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    private static byte[] aesCbcDecrypt(SecretKey secretKey, byte[] encrypted)
            throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(
                IV));
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }


    private static byte[] aesGcmEncrypt(SecretKey secretKey, byte[] plainData, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(128, iv));
        return cipher.doFinal(plainData);
    }

    private static byte[] aesGcmDecrypt(SecretKey secretKey, byte[] cipherText, byte[] iv) {
        byte[] result = new byte[0];
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(128, iv));
            result = cipher.doFinal(cipherText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
