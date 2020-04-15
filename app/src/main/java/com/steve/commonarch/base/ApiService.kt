package com.steve.commonarch.base;

import com.steve.commonarch.data.ResultServerTime;

import io.reactivex.Flowable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("/ucenter/api/v1/send/otp")
    fun requestAuthCode(@Field("data") data:String):Flowable<BaseResponse<ResultServerTime>>
}
