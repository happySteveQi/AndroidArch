package com.steve.commonarch

import android.content.Context
import android.util.Log
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.steve.commonarch.data.local.SharedPref
import com.steve.commonarch.data.local.SharedPrefKeyManager
import com.steve.commonarch.utils.PackageUtilCash
import com.steve.commonarch.utils.ThreadPoolUtil
import java.io.DataInputStream

class AppEnv {
    companion object {
        private const val TAG = "AppEnv"
        const val DEBUG = true//BuildConfig.APP_DEBUG
        const val APP_VERSION = "1.0.1"//BuildConfig.APP_VERSION
        const val APP_BUILD = "121"//BuildConfig.APP_BUILD
        /**
         * 每个产品单独一个
         */
        const val UIVERSION = 11000
        const val API_ACTION = "andr_ms"
        const val PKGNAME = BuildConfig.APPLICATION_ID
        const val CID_DAT = "cid.dat"
        var CID = -1
        var GAID = ""
        fun getCID(context: Context): Int {
            if (CID < 0) {
                // 先读取Google Play的渠道号
                CID = SharedPref.getInt(SharedPrefKeyManager.KEY_CID, -1)
                if (CID > 0) {
                    if (DEBUG) {
                        Log.d(TAG, " CID from cid: $CID")
                    }

                    return CID
                }

                // 读取包中的渠道号
                val am = context.assets
                var dis: DataInputStream? = null
                try {
                    dis = DataInputStream(am.open(CID_DAT))
                    val code = dis.readLine()
                    CID = Integer.parseInt(code.trim { it <= ' ' })
                    SharedPref.setInt(SharedPrefKeyManager.KEY_CID,
                        CID
                    )
                    if (DEBUG) {
                        Log.d(TAG, " CID form asset file= $CID")
                    }
                } catch (e: Exception) {
                    if (DEBUG) {
                        Log.e(TAG, "[catched]", e)
                    }
                } finally {
                    if (dis != null) {
                        try {
                            dis.close()
                        } catch (e: Exception) {
                        }

                    }
                }
            }

            return CID
        }

        fun initBackgroundTask() {
            ThreadPoolUtil.rxjavaExecutor("application background") {
                initGaid()
            }
        }
        /**
         * 此方法只能在子线程调用
         */
        private fun initGaid() {
            ThreadPoolUtil.rxjavaExecutor("gaid") {
                try {
                    GAID = AdvertisingIdClient.getAdvertisingIdInfo(App.getAppContext()).id
                } catch (e: Exception) {
                    if (DEBUG) {
                        Log.d("gaid", "e = $e")
                    }
                }
            }
        }

        fun getAppVersionCode() =
            PackageUtilCash.getVersionCode(
                App.getAppContext(),
                App.getAppContext().packageName
            )
    }
}