package com.steve.commonarch.base.dot

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import com.steve.commonarch.App
import com.steve.commonarch.AppEnv
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


/**
 * Created by weisl on 2018/6/4.
 *
 */
class DisplayUtils {
    companion object {

        fun getCurrentDate(): String {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            return format.format(Date(System.currentTimeMillis()))
        }

        fun getCurrentSdk(): Int {
            return Build.VERSION.SDK_INT
        }

        fun getVersionName(): String {
            return "${AppEnv.APP_VERSION}.${AppEnv.APP_BUILD}"
        }

        fun getScreenWH(): IntArray {
            val displayMetrics = App.getAppContext().resources.displayMetrics
            val widthPixels = displayMetrics.widthPixels
            val heightPixels = displayMetrics.heightPixels
            return intArrayOf(widthPixels, heightPixels)
        }

        fun getScreenWidth(): Int {
            return getScreenWH()[0]
        }
        private fun getRealDisplayMetrics(context: Context): DisplayMetrics {
            val dm = DisplayMetrics()
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                wm.defaultDisplay.getRealMetrics(dm)
            } else {
                // 如果小于15，则忽略虚拟按键(如果有需要可以用反射获取，此处暂不考虑)
                wm.defaultDisplay.getMetrics(dm)
            }
            return dm
        }

        /**
         * 得到真实的屏幕宽度
         */
        fun getRealScreenWidth(): Int {
            return getRealDisplayMetrics(App.getAppContext()).widthPixels
        }

        /**
         * 得到真实的屏幕高度
         */
        fun getRealScreenHeight(): Int {
            return getRealDisplayMetrics(App.getAppContext()).heightPixels
        }

        fun getTotalMem(): Long {
            var totalMomey = 0L
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                val activityManager: ActivityManager =
                    App.getAppContext().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                val memoryInfo = ActivityManager.MemoryInfo()
                activityManager.getMemoryInfo(memoryInfo)
                totalMomey = (memoryInfo.totalMem / 1024).toLong()
            } else {
                val str1 = "/proc/meminfo"
                var localBufferedReader: BufferedReader? = null
                try {
                    val fr = FileReader(str1)
                    localBufferedReader = BufferedReader(fr)
                    val str = localBufferedReader.readLine()
                    val regex = "^[0-9]*$"
                    val split = str.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    for (i in split.indices) {
                        if (split[i].matches(regex.toRegex())) {
                            totalMomey = split[i].toLong()
                            break
                        }
                    }
                } catch (e: IOException) {
                    if (AppEnv.DEBUG) {
                        Log.e(
                            "debug_DisplayUtils",
                            "error = ${e.toString()}"
                        )
                    }
                } finally {
                    try {
                        localBufferedReader?.close()
                    } catch (e: Exception) {
                    }
                }
            }
            return totalMomey
        }

        fun getSystemAvaialbeMemorySize(): Int {
            //获得ActivityManager服务的对象
            val mActivityManager =
                App.getAppContext().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            //获得MemoryInfo对象
            val memoryInfo = ActivityManager.MemoryInfo()
            //获得系统可用内存，保存在MemoryInfo对象上
            mActivityManager.getMemoryInfo(memoryInfo)
            val memSize = memoryInfo.availMem
            return (memSize / 1024).toInt()
        }

        fun getRom(): String {
            return Build.MANUFACTURER
        }

        fun getCpuNum(): Int {
            try {
                //Get directory containing CPU info
                val dir = File("/sys/devices/system/cpu/")
                //Filter to only list the devices we care about
                val files = dir.listFiles(CpuFilter())
                //Return the number of cores (virtual CPU devices)
                return files.size
            } catch (e: Exception) {
                //Print exception
                // Log.d(TAG, "CPU Count: Failed.");
                e.printStackTrace()
                //Default to return 1 core
                return 1
            }
        }

        /** 获取APP安装时间 */
        fun getAppInstallTimer(): Long {
            var installerTimer = 0L
            try {
                val packageInfoList = App.getAppContext().packageManager.getInstalledPackages(0)
                packageInfoList.forEach {
                    if (TextUtils.equals(it.packageName, App.getAppContext().packageName)) {
                        installerTimer = it.firstInstallTime
                    }
                }
            } catch (e: Exception) {
            }
            return installerTimer
        }

        /** 获取APP最后更新时间 */
        fun getAppLastInstallTimer(): Long {
            var installerTimer = 0L
            try {
                val packageInfoList = App.getAppContext().packageManager.getInstalledPackages(0)
                packageInfoList.forEach {
                    if (TextUtils.equals(it.packageName, App.getAppContext().packageName)) {
                        installerTimer = it.lastUpdateTime
                    }
                }
            } catch (e: Exception) {
            }
            return installerTimer
        }

        class CpuFilter : FileFilter {
            override fun accept(pathname: File?): Boolean {
                if (Pattern.matches("cpu[0-9]", pathname?.getName())) {
                    return true
                }
                return false
            }
        }
    }
}