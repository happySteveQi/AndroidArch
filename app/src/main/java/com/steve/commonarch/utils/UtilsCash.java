package com.steve.commonarch.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;

import com.steve.commonarch.aes.Base64Utils;
import com.steve.commonarch.aes.MD5UtilsCash;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class UtilsCash {
    private static final String TAG = "Utils";
    private static final boolean DEBUG = false;
    private static final String TIMESTAMP_EXT = ".timestamp";


    public static void copyAssetsFile(Context context, String assetsFileName, File destFile) throws IOException {
        UtilsCash.copyFile(context.getAssets().open(assetsFileName), destFile);
    }

    public static void copyFile(InputStream is, File destFile) throws IOException {

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(destFile);
            byte arrayByte[] = new byte[1024];
            int i = 0;
            while ((i = is.read(arrayByte)) != -1) {
                fos.write(arrayByte, 0, i);
            }
            fos.flush();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    if (DEBUG) {
                        Log.e(TAG, "copyFiles", e);
                    }
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    if (DEBUG) {
                        Log.e(TAG, "copyFile", e);
                    }
                }
            }
        }
    }

    public static String encodeBase64(String str) {
        return new String(Base64Utils.encodeBase64(str.getBytes()));
    }



    public static void dismissDialog(Dialog dialog) {
        try {
            if(dialog == null) {
                return;
            }

            Context context = dialog.getContext();
            if(context != null && context instanceof Activity && ((Activity)context).isFinishing()) {
                return;
            }

            if(dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Throwable var2) {
            ;
        }

    }
    /**
     * 在给定路径的后面附加文件或者目录。
     */
    public static String pathAppend(String path, String more) {
        StringBuffer buffer = new StringBuffer(path);
        if (!path.endsWith("/")) {
            buffer.append('/');
        }
        buffer.append(more);

        return buffer.toString();
    }
    public static boolean makeSurePathExists(String path) {
        File file = new File(path);
        return makeSurePathExists(file);
    }

    public static boolean makeSurePathExists(File path) {
        return path == null?false:(path.isDirectory()?true:(!path.exists()?path.mkdirs():false));
    }

    public static String DES_decrypt(String passwordToken, String password) {
        return MD5UtilsCash.des_decrypt_hex(passwordToken, password);
    }

    public static String DES_encrypt(String securityToken, String passwd) {
        return MD5UtilsCash.des_encrypt_hex(securityToken, passwd);
    }

    /**
     * 封装这个方法是因为保证使用Application context来调用getSystemService，否则会内存泄漏
     */
    public static Object getSystemService(Context context, String name) {
        return context.getApplicationContext().getSystemService(name);
    }


    /**
     * 比对assets 和 data/data/包名/files 中的时间戳文件，读取新文件，若files
     * 中不文件，则先将asserts中文件拷贝到files中，避免v5重复下载，消耗无用流量 到 files
     */
    public static InputStream openLatestInputFile(Context c, String filename) {

        try {
            // 将相关文件复制到data/data/pkg/files中
            File file = new File(c.getFilesDir(), filename);
            if (!file.exists()) {
                UtilsCash.copyAssetsFile(c, filename, file);
            }

            String timestampFileName = filename + TIMESTAMP_EXT;
            File timestampFile = new File(c.getFilesDir(), timestampFileName);
            if (!timestampFile.exists()) {
                UtilsCash.copyAssetsFile(c, timestampFileName, timestampFile);
            }
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "copy filter file fail", e);
            }
        }

        InputStream is = null;

        long timestampOfFile = UtilsCash.getFileTimestamp(c, filename);
        long timestampOfAsset = UtilsCash.getBundleTimestamp(c, filename);

        if (timestampOfFile >= timestampOfAsset) {
            // files 目录的时间戳更新，那么优先读取 files 目录的文件

            try {
                is = c.openFileInput(filename);

                if (DEBUG) {
                    Log.d(TAG, String.format("Opening %s in files directory", filename));
                }
            } catch (Exception e) {
                if (DEBUG) {
                    Log.w(TAG, String.format("%s in files directory not found, skip.", filename), e);
                }
            }
        }

        if (is == null) {
            // is == null 表明没能从 files 目录读到文件，那么到 assets 目录去读读看

            try {
                is = c.getAssets().open(filename);

                if (DEBUG) {
                    Log.d(TAG, String.format("Opening %s in assets", filename));
                }
            } catch (Exception e) {
                if (DEBUG) {
                    Log.e(TAG, filename + " in assets not found, open failed!", e);
                }
            }
        }

        return is;
    }

    /**
     * 读取文件的时间戳
     */
    public static long getFileTimestamp(Context c, String filename) {
        FileInputStream fis = null;
        try {
            fis = c.openFileInput(filename + TIMESTAMP_EXT);
        } catch (Exception e) {
        }

        if (fis != null) {
            return UtilsCash.getTimestampFromStream(fis);
        } else {
            return 0;
        }
    }

    private static long getTimestampFromStream(InputStream fis) {

        DataInputStream dis = null;
        try {
            dis = new DataInputStream(fis);
            String s = dis.readLine();
            return Long.parseLong(s);
        } catch (Exception e) {
            if(DEBUG){
                Log.e(TAG, "", e);
            }
        } finally {
            try {
                if (dis != null) {
                    dis.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
                if(DEBUG){
                    Log.e(TAG, "", e);
                }
            }
        }

        return 0;
    }

    // 对于打包的文件，都是放在 assets 目录的，时间戳自然也在 assets 目录
    public static long getBundleTimestamp(Context c, String filename) {
        InputStream fis = null;
        try {
            fis = c.getAssets().open(filename + TIMESTAMP_EXT);
        } catch (Exception e) {
        }

        if (fis != null) {
            return UtilsCash.getTimestampFromStream(fis);
        } else {
            return 0;
        }
    }
}