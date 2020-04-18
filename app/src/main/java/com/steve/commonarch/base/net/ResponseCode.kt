package com.steve.commonarch.base.net

/**
 * Created by weisl on 2019/8/13.
 */
object ResponseCode {

    const val FORCED_UPDATE_CODE = 800800 //强制升级

    //小于0的是本地自定义的错误码
    const val FAILED = -1
    const val SOCKET_TIME_OUT = -2
    const val UNKNOWN_HOST_EXCEPTION = -3

    const val SUCCESS = 0

    const val INVALIDTOKEN = 400114//token过期和无效
    const val INVALID_AUTH_CODE = 400116 //验证码错误
}