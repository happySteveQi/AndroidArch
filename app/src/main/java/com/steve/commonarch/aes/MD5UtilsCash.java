package com.steve.commonarch.aes;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class MD5UtilsCash {
    public static final boolean DEBUG = true;
    private static final String TAG = "MD5Utils";

    public static String getMD5(String input) {
        return input == null ? "" : MD5UtilsCash.getMD5(input.getBytes());
    }

    public static String getMD5(byte[] input) {
        return ByteConvertor.bytesToHexString(MD5UtilsCash.MD5(input));
    }

    private static byte[] MD5(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input);
            return md.digest();
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "", e);
            }
        }
        return null;
    }

    /**
     * 字符串MD5加密,在做Hex
     *
     * @param input
     * @param key
     * @return
     */
    public static String des_encrypt_hex(String input, String key) {
        try {
            byte[] output = MD5UtilsCash.des_encrypt(input, key);
            return ByteConvertor.bytesToHexString(output);
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "", e);
            }
        }
        return "";
    }

    /**
     * Hex字符串解密
     *
     * @param input
     * @param key
     * @return
     */
    public static String des_decrypt_hex(String input, String key) {
        try {
            byte[] bytes = ByteConvertor.hexStringToBytes(input);
            byte[] output = des_decrypt(bytes, key);
            return new String(output);
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "", e);
            }
        }
        return "";
    }

    /**
     * 字符串MD5加密
     *
     * @param input
     * @param key
     * @return
     */
    public static byte[] des_encrypt(String input, String key) {
        try {
            return MD5UtilsCash.des_encrypt(input.getBytes(), key);
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "", e);
            }
        }
        return null;
    }

    /**
     * byte数组MD5加密
     *
     * @param input
     * @param key
     * @return
     */
    public static byte[] des_encrypt(byte[] input, String key) {
        try {
            SecureRandom e = new SecureRandom();
            DESKeySpec dks = new DESKeySpec(MD5UtilsCash.MD5(key.getBytes()));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, e);
            return cipher.doFinal(input);
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "", e);
            }
        }
        return null;
    }

    /**
     * byte数组解密
     *
     * @param input
     * @param key
     * @return
     */
    public static byte[] des_decrypt(byte[] input, String key) {
        try {
            SecureRandom sr = new SecureRandom();
            DESKeySpec dks = new DESKeySpec(MD5(key.getBytes()));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, sr);
            return cipher.doFinal(input);
        } catch (Throwable e) {
            if (DEBUG) {
                Log.e(TAG, "", e);
            }
        }
        return null;
    }

    /**
     * 加密  pwd 不用md5
     *
     * @param src
     * @param pwd
     * @return
     */
    public static byte[] des_encrypt_no_md5(byte[] src, String pwd) {
        try {
            SecureRandom sr = new SecureRandom();
            DESKeySpec dks = new DESKeySpec(pwd.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(1, securekey, sr);
            return cipher.doFinal(src);
        } catch (Throwable var7) {
            return null;
        }
    }

    /**
     * 加密  pwd 不用md5
     *
     * @param src
     * @param pwd
     * @return
     */
    public static byte[] des_decrypt_no_md5(byte[] src, String pwd) {
        try {
            SecureRandom sr = new SecureRandom();
            DESKeySpec dks = new DESKeySpec(pwd.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(2, securekey, sr);
            return cipher.doFinal(src);
        } catch (Throwable var7) {
            return null;
        }
    }

    public static String getMd5(File file) {
        if (!file.exists()) {
            return null;
        }
        InputStream is = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            is = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int read;
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] md5sum = digest.digest();
            return ByteConvertor.bytesToHexString(md5sum);
        } catch (Exception e) {
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // nothing to do
                }
            }
        }
    }
}
