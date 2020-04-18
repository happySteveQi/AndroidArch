package com.steve.commonarch.base.net

import android.content.res.Resources
import com.steve.commonarch.App
import com.steve.commonarch.R
import com.steve.commonarch.base.BaseResponse
import com.steve.commonarch.utils.expand.toast
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ApiExceptionHandler {

    val resource: Resources = App.getAppContext().resources

    fun <T> showToast(response: BaseResponse<T>, isUseDefaultTips: Boolean = true) {
        val msgResId: Int = when (response.code) {
            ResponseCode.SOCKET_TIME_OUT -> R.string.network_error
            ResponseCode.UNKNOWN_HOST_EXCEPTION -> R.string.no_internet_connection
            else -> 0
        }
        if (msgResId != 0) {
            toast(msgResId)
            response.isProcessed = true
        } else if (isUseDefaultTips) {
            toast(App.getAppContext().getString(R.string.unknown_error, response.code))
            response.isProcessed = true
        }
    }

    fun <T> exceptionTransformResponse(t: Throwable): BaseResponse<T> {
        var code = ResponseCode.FAILED
        var obj: Any? = null
        when (t) {
            is SocketTimeoutException -> {
                code = ResponseCode.SOCKET_TIME_OUT
            }
            is UnknownHostException -> {
                code = ResponseCode.UNKNOWN_HOST_EXCEPTION
            }
            is HttpResponseException -> {
                code = t.code
                obj = t.any
            }
        }
        val response = BaseResponse<T>(code = code, data = "", message = "")
        if (obj != null) {
            response.t = obj as T?
        }
        return response
    }
}