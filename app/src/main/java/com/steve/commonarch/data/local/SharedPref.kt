package com.steve.commonarch.data.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.steve.commonarch.App
import com.steve.commonarch.AppEnv

/**
 * Created by weisl on 2019/10/14.
 */
object SharedPref {
    const val NAME = "CreditBean"
    const val TAG = "SharedPref"
    /**
     * 记录应用的版本versionCode
     */
    const val KEY_APP_VERSION = "key_app_version"

    private var mSP: SharedPreferences

    init {
        mSP = App.getAppContext().getSharedPreferences(NAME, Context.MODE_PRIVATE)
    }

    fun setString(key: String, value: String?) {
        mSP.edit().putString(key, value ?: "").apply()
    }

    fun getString(key: String, defVal: String?): String {
        return mSP.getString(key, defVal ?: "")
    }

    fun setBoolean(key: String, value: Boolean) {
        mSP.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {

        return mSP.getBoolean(key, defValue)
    }

    fun setInt(key: String, value: Int) {
        mSP.edit().putInt(key, value).apply()
    }

    fun getInt(key: String, defValue: Int): Int {
        return mSP.getInt(key, defValue)
    }

    fun setLong(key: String, value: Long) {
        mSP.edit().putLong(key, value).apply()
    }

    fun getLong(key: String, defValue: Long): Long {
        return mSP.getLong(key, defValue)
    }

    fun removeKey(key: String) {
        try {
            if (mSP.contains(key)) {
                mSP.edit().remove(key).apply()
            }
        } catch (e: Exception) {
            if (AppEnv.DEBUG) {
                Log.e(TAG, "remove key e.toString$e")
            }
        }
    }
}