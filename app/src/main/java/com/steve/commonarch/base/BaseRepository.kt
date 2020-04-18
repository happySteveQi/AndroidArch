package com.steve.commonarch.base

import androidx.lifecycle.LiveData
import com.google.gson.JsonObject
import com.steve.commonarch.base.net.ApiServiceLiveDataProxy
import com.steve.commonarch.base.net.NetBaseParamsManager
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File

open class BaseRepository {

    var disposableList: ArrayList<Disposable>? = null

    /**
     * construct data param
     */
    fun buildDataParams(jsonObject: JsonObject?, isNeedBase: Boolean = true): String =
        NetBaseParamsManager.buildDataParams(jsonObject, isNeedBase)

    fun <T> request(
        clazz: Class<T>,
        flowable: () -> Flowable<BaseResponse<T>>
    ): LiveData<BaseResponse<T>> {
        return ApiServiceLiveDataProxy.request(clazz, disposableList, flowable)
    }

    fun <T> requestNoDefaultTip(
        clazz: Class<T>,
        flowable: () -> Flowable<BaseResponse<T>>
    ): LiveData<BaseResponse<T>> {
        return ApiServiceLiveDataProxy.request(clazz, disposableList, flowable, false)
    }

    fun <T> requestIgnoreLogin(
        clazz: Class<T>,
        flowable: () -> Flowable<BaseResponse<T>>
    ): LiveData<BaseResponse<T>> {
        return ApiServiceLiveDataProxy.requestIgnoreLogin(
            clazz,
            disposableList,
            flowable
        )
    }

    fun <T> requestIgnoreLoginNoDefaultTip(
        clazz: Class<T>,
        flowable: () -> Flowable<BaseResponse<T>>
    ): LiveData<BaseResponse<T>> {
        return ApiServiceLiveDataProxy.requestIgnoreLogin(
            clazz,
            disposableList,
            flowable,
            false
        )
    }

    /**
     * 创建文件body
     */
    fun createFileRequestBody(file: File): RequestBody {
        return RequestBody.create(MediaType.parse("application/octet-stream"), file)
    }

}