package com.steve.commonarch.base

import android.util.Log
import androidx.annotation.Keep
import com.steve.commonarch.AppEnv
import com.steve.commonarch.aes.AESNormalUtil
import com.steve.commonarch.aes.GsonUtil
import com.steve.commonarch.base.net.ResponseCode
import java.lang.Exception

const val TAG = "OkHttp"
const val DEBUG = AppEnv.DEBUG

@Keep
open class BaseResponse<T>(var code: Int, val data: String?, val message: String?) {
    var t: T? = null
    var isProcessed: Boolean = false //是否处理过错误码

    inline fun parseT(clazz: Class<T>): T? {
        if (t == null) {
            try {
                data?.let {
                    val json = AESNormalUtil.decrypt(it)!!
                    if (DEBUG) {
                        Log.i(TAG, "parseT>>json:$json")
                    }
                    t = GsonUtil.fromJson(json, clazz) as T?
                }
            } catch (e: Exception) {
                e.printStackTrace()
                if (DEBUG) {
                    Log.e(TAG, "parseT error", e)
                }
            }
        }
        if (DEBUG) {
            Log.i(TAG, "parseT>>t:$t")
        }
        return t
    }

    fun getData(): T? = t

    fun isSuccess() = code == ResponseCode.SUCCESS

    fun isAppForcedUpdate():Boolean{
        return code == ResponseCode.FORCED_UPDATE_CODE
    }

    fun isInvalidCode() = code == ResponseCode.INVALID_AUTH_CODE

    fun isInvalidToken() = code == ResponseCode.INVALIDTOKEN

    override fun toString(): String {
        return "BaseResponse(code=$code, message=$message, t=$t)"
    }
}