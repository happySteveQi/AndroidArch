package com.steve.commonarch.base

import android.util.Log
import androidx.annotation.Keep
import com.steve.commonarch.AppEnv
import java.lang.Exception

const val TAG = "OkHttp"
const val DEBUG = AppEnv.DEBUG

@Keep
open class BaseResponse<T>(var code: Int, val data: String?, val message: String?) {
    var t: T? = null
    var isProcessed:Boolean = false //是否处理过错误码

    inline fun parseT(clazz:Class<T>):T?{
        if (t == null){
            try {
                data?.let {
//                    val json = AES
                }
            }catch (e:Exception){
                e.printStackTrace()
                if (DEBUG){ Log.e(TAG,"parseT error",e)}
            }
        }
        return t
    }
}