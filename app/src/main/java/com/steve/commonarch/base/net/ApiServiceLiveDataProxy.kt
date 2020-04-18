package com.steve.commonarch.base.net

import androidx.lifecycle.LiveData
import com.steve.commonarch.base.BaseResponse
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable


object ApiServiceLiveDataProxy {

    @JvmStatic
    fun <T> request(
        clazz: Class<T>,
        disposableList: ArrayList<Disposable>? = null,
        flowable: () -> Flowable<BaseResponse<T>>,
        isUseDefaultTips: Boolean = true
    ): LiveData<BaseResponse<T>> =
        LiveDataCall(
            skipLogin = true,
            clazz = clazz,
            flowable = flowable,
            disposableList = disposableList,
            isUseDefaultTips = isUseDefaultTips
        )

    /**
     * 忽略登录的请求,接口返回token无效也无需跳转到登录界面
     */
    @JvmStatic
    fun <T> requestIgnoreLogin(
        clazz: Class<T>,
        disposableList: ArrayList<Disposable>? = null,
        flowable: () -> Flowable<BaseResponse<T>>,
        isUseDefaultTips: Boolean = true
    ): LiveData<BaseResponse<T>> =
        LiveDataCall(
            skipLogin = false,
            clazz = clazz,
            flowable = flowable,
            disposableList = disposableList,
            isUseDefaultTips = isUseDefaultTips
        )
}