package com.steve.commonarch.utils

import android.app.Activity
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.steve.commonarch.AppEnv
import com.steve.commonarch.data.local.SharedPref
import java.util.*

/**
 * Created by weisl on 2019/10/14.
 */
object PackageUtilCash {


    val TAG = "PackageUtilCash"
    private val DEBUG = AppEnv.DEBUG
    fun getInstallApp(context: Context): List<PackageInfo> {
        val list = context.packageManager.getInstalledPackages(
            PackageManager.GET_UNINSTALLED_PACKAGES
        )
        var i = list.size - 1
        while (i > 0) {
            if (!PackageUtilCash.isAppLaunchable(context, list[i].packageName)) {
                list.removeAt(i)
            }
            i--
        }
        return list
    }

    fun isAppLaunchable(context: Context, pkgName: String): Boolean {
        return null != context.packageManager.getLaunchIntentForPackage(pkgName)
    }

    fun getAppPackageInfo(context: Context, pkgName: String): PackageInfo? {
        try {
            val pm = context.packageManager
            return pm.getPackageInfo(pkgName, PackageManager.GET_SIGNATURES)
        } catch (e: Exception) {
            if (DEBUG) {
                Log.e(TAG, "error = $e")
            }
        }

        return null
    }

    fun getVersionCode(context: Context, pkgName: String): Int {
        var versionCode = 1
        val pkgInfo = getAppPackageInfo(context, pkgName)
        if (pkgInfo != null) {
            versionCode = pkgInfo.versionCode
        }
        return versionCode
    }

    fun isDiffVersionCash(context: Context, key: String): Boolean {
        val versionCode = SharedPref.getInt(key, -1)
        val currentVersionCode = getVersionCode(context, AppEnv.PKGNAME)
        return versionCode != currentVersionCode
    }

    fun setCurrentVersionCash(context: Context, key: String) {
        val currentVersionCode = PackageUtilCash.getVersionCode(context, AppEnv.PKGNAME)
        SharedPref.setInt(key, currentVersionCode)
    }

    private fun isSystemApp(pInfo: PackageInfo): Boolean {
        return pInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }

    private fun isSystemUpdateApp(pInfo: PackageInfo): Boolean {
        return pInfo.applicationInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP != 0
    }

    /**
     * 是否是预装
     *
     * @return
     */
    fun isPreInstall(pInfo: PackageInfo): Boolean {
        return isSystemApp(pInfo) || isSystemUpdateApp(pInfo)
    }

    fun queryAppUsageStats(context: Context): List<UsageStats>? {
        val enable = true//需要检查权限 把代码先删了
        if (AppEnv.DEBUG) {
            Log.i(TAG, "ENABLE= $enable")
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            val usageStatsManager = context.applicationContext
                .getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
            val calendar = Calendar.getInstance()
            calendar.time = Date()
            val endt = calendar.timeInMillis//结束时间
            calendar.add(Calendar.DAY_OF_MONTH, -1)//时间间隔为一天
            val statt = calendar.timeInMillis//开始时间
            return usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, statt, endt)
        }
        return null
    }

    fun jumpUsageStatPermissionPage(context: Context) {
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        if (context !is Activity) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }
}