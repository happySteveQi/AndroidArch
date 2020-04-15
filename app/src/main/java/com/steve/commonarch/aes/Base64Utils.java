package com.steve.commonarch.aes;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;

public class Base64Utils {
    private static final String TAG = "Base64Utils";
    private static final boolean DEBUG = true;
    public static String encode(String str) {
        String result = "";
        if( str != null) {
             try {
                result = new String(Base64.encode(str.getBytes("utf-8"), Base64.NO_WRAP),"utf-8");
             } catch (UnsupportedEncodingException e) {
                 if (DEBUG){
                     Log.e(TAG,"error = "+e.toString());
                 }
             }
        }
        return result;
    }

    public static String encode(byte[] bytes){
        String result = "";
        if( bytes != null) {
            try {
                result = new String(Base64.encode(bytes, Base64.NO_WRAP),"utf-8");
            } catch (UnsupportedEncodingException e) {
                if (DEBUG){
                    Log.e(TAG,"error = "+e.toString());
                }
            }
        }
        return result;
    }

    public static String encodeBase64(byte[] bytes){
        String result = "";
        if( bytes != null) {
            try {
                result = new String(Base64.encode(bytes, Base64.NO_WRAP),"utf-8");
            } catch (UnsupportedEncodingException e) {
                if (DEBUG){
                    Log.e(TAG,"error = "+e.toString());
                }
            }
        }
        return result;
    }

    public static byte[] decode(String str) {
       return Base64.decode(str, Base64.NO_WRAP);
    }
}