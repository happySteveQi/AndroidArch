package com.steve.commonarch.base.net

/**
 * Created by weisl on 2018/12/19.
 */
class HttpResponseException(var code: Int, var any: Any? = null, message: String?) :
    Exception(message) {
    override fun toString(): String {
        return "HttpResponseException(code=$code,message=$message,any=$any)"
    }
}