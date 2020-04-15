package com.steve.commonarch.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.steve.commonarch.App
import com.steve.commonarch.AppEnv
import com.steve.commonarch.base.net.NetBaseParamsManager
import java.io.*
import java.util.*
import java.util.regex.Pattern


/**
 * Created by weisl on 2019/10/15.
 */
object SysUtils {

    private const val DEBUG = AppEnv.DEBUG
    private const val TAG = "debug_SysUtils"
    private const val DEVICE_ID_FILENAME_NEW = "DEV2"
    private const val INVALID_IMEI_FILENAME = "non_imei"
    private var sDeviceId: String? = null


    @SuppressLint("HardwareIds")
    @JvmStatic
    @Synchronized
    fun getDeviceId(context: Context): String {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_PHONE_STATE
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            return generateCashUUID(context)
        }
//        if (IMEIDialogHelper.Companion.isGetIMEIAgree()) {// dialog
            if (DEBUG) {
                Log.i(TAG, "用户同意抓取imei")
            }
            try {
                // IMEI
                val telephonyManager =
                    context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                val imei = telephonyManager.deviceId
                if (invalidCashDeviceId(context, imei)) {
                    if (DEBUG) {
                        Log.i(TAG, "imei 无效")
                    }
                    return generateCashUUID(context)
                }
                if (TextUtils.isEmpty(imei)) {
                    if (DEBUG) {
                        Log.i(TAG, "imei 为空")
                    }
                    return generateCashUUID(context)
                }
                if (DEBUG) {
                    Log.i(TAG, "imei = $imei")
                }

                // imei不能是0;IMEI必须大于10位
                return if ("0" == imei || imei.length <= 10) {
                    generateCashUUID(context)
                } else imei

            } catch (e: Exception) {
                if (DEBUG) {
                    Log.e(TAG, "获取IMEI e = $e")
                }
            }

            return generateCashUUID(context)
//        } else {
//            if (DEBUG) {
//                Log.i(TAG, "用户不同意抓取imei")
//            }
//            return generateCashUUID(context)
//        }
    }

    private fun invalidCashDeviceId(context: Context, str: String): Boolean {

        if (TextUtils.isEmpty(str)) {
            return true
        }

        var `is`: InputStream? = null
        var br: BufferedReader? = null
        try {
            `is` = UtilsCash.openLatestInputFile(context, INVALID_IMEI_FILENAME)
            if (`is` != null) {
                br = BufferedReader(InputStreamReader(`is`))
                var regexp: String? = br.readLine()
                while (regexp != null) {
                    try {
                        val pattern = Pattern.compile(regexp)
                        val match = pattern.matcher(str)
                        if (match.matches()) {
                            return true
                        }
                        regexp = br.readLine()
                    } catch (ex: Exception) {
                        if (DEBUG) {
                            Log.e(TAG, ex.toString())
                        }
                    }

                }
            }
        } catch (ex: Exception) {
            if (DEBUG) {
                Log.e(TAG, ex.toString())
            }
        } finally {
            if (br != null) {
                try {
                    br.close()
                } catch (e: IOException) {
                    if (DEBUG) {
                        Log.e(TAG, e.toString())
                    }
                }

            }
            if (`is` != null) {
                try {
                    `is`.close()
                } catch (ex: Exception) {
                    if (DEBUG) {
                        Log.e(TAG, ex.toString())
                    }
                }

            }
        }
        return false
    }


    /**
     * 获取UUID
     */
    @JvmStatic
    @Synchronized
    fun generateCashUUID(context: Context): String {
        if (sDeviceId == null) {
            val newFile = File(context.filesDir, DEVICE_ID_FILENAME_NEW)
            if (newFile.exists()) {
                if (DEBUG) {
                    Log.d(TAG, "UUID(), newFile exist.")
                }
                //新文件存在，直接读取
                sDeviceId = readCashDeviceIdFile(context, newFile, true)
                if (TextUtils.isEmpty(sDeviceId)) {
                    //可能读取失败，则重新生成
                    createCashUUID(context, newFile, true)
                }
            } else {
                if (DEBUG) {
                    Log.d(TAG, "UUID(), newFile not exist.")
                }
                createCashUUID(context, newFile, true)
            }
        }
        if (DEBUG) {
            Log.d(TAG, "UUID() =$sDeviceId")
        }
        return sDeviceId.orEmpty()
    }

    @JvmStatic
    private fun createCashUUID(context: Context, file: File, encode: Boolean) {
        try {
            val builder = StringBuilder()
            var deviceId: String = generateCashUUID().toString()
            if (!invalidCashDeviceId(context, deviceId)) {
                builder.append("U_")
                builder.append(deviceId)
                if (DEBUG) {
                    Log.d(TAG, "UUID U=$deviceId")
                }
            } else {
                deviceId = NetBaseParamsManager.getRandomString(Random().nextInt(32) + 68)
                builder.append(deviceId)
                if (DEBUG) {
                    Log.d(TAG, "UUID U RandomString =$deviceId")
                }
            }

            sDeviceId = builder.toString()
            if (DEBUG) {
                Log.d(TAG, "UUID UUID=$sDeviceId")
            }


        } catch (ex: Exception) {
            if (DEBUG) Log.d(TAG, "UUID  生成异常 ${ex.toString()}")
            sDeviceId = NetBaseParamsManager.Companion.getRandomString(Random().nextInt(32) + 68)
        }

        writeCashDeviceIdFile(context, sDeviceId.orEmpty(), file, encode)
    }

    /**
     * 获取UUID
     *
     * @return
     */
    private fun generateCashUUID(): String? {
        var id: String? = null
        try {
            id = UUID.randomUUID().toString()
            id = id.replace("-".toRegex(), "").replace(":", "").toLowerCase()
        } catch (ex: java.lang.Exception) {
            if (DEBUG) {
                Log.e(TAG, ex.toString())
            }
        }
        return id
    }

    @JvmStatic
    private fun readCashDeviceIdFile(context: Context, deviceFile: File, decode: Boolean): String? {
        var f: RandomAccessFile? = null
        var deviceId: String? = null
        try {
            f = RandomAccessFile(deviceFile, "r")
            val bytes = ByteArray(f.length().toInt())
            f.readFully(bytes)
            if (decode) {
                deviceId = UtilsCash.DES_decrypt(String(bytes), context.packageName)
            } else {
                deviceId = String(bytes)
            }
        } catch (ex: Throwable) {
            //Caused by: java.lang.ExceptionInInitializerError
        } finally {
            if (f != null) {
                try {
                    f.close()
                } catch (ex: Exception) {
                }

            }
        }
        return deviceId
    }

    @JvmStatic
    private fun writeCashDeviceIdFile(
        context: Context,
        deviceId: String,
        deviceFile: File,
        encode: Boolean
    ) {
        var deviceId = deviceId
        if (TextUtils.isEmpty(deviceId)) {
            return
        }

        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(deviceFile, false)
            if (encode) {
                deviceId = UtilsCash.DES_encrypt(deviceId, context.packageName)
            }
            out.write(deviceId.toByteArray())
        } catch (ex: Exception) {
        } finally {
            if (out != null) {
                try {
                    out.close()
                } catch (ex: Exception) {
                }

            }
        }
    }

    @JvmStatic
    @SuppressLint("MissingPermission")
    fun getPhoneNumber(mContext: Context): String {
        if (ContextCompat.checkSelfPermission(
                mContext,
                Manifest.permission.READ_PHONE_STATE
            ) === PackageManager.PERMISSION_DENIED && ContextCompat.checkSelfPermission(
                mContext,
                Manifest.permission.READ_PHONE_NUMBERS
            ) === PackageManager.PERMISSION_DENIED
        ) {
            return ""
        }
        try {
            val phoneMgr = mContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            return phoneMgr.line1Number
        } catch (e: Exception) {
            if (DEBUG) {
                Log.e(TAG, e.toString())
            }
        }

        return ""
    }

    fun getActionBarHeight(): Int {
        val tv = TypedValue()
        return if (App.getAppContext().theme.resolveAttribute(
                android.R.attr.actionBarSize,
                tv,
                true
            )
        ) {
            TypedValue.complexToDimensionPixelSize(
                tv.data,
                App.getAppContext().resources.displayMetrics
            )
        } else 0
    }
}