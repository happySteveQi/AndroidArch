package com.steve.commonarch.base.net

import androidx.annotation.Keep
import com.steve.commonarch.BuildConfig
import okhttp3.Interceptor
import javax.inject.Inject

class ApiManager @Inject constructor() {
    companion object {
        fun getInstance(): ApiManager = ApiManager()
    }
    private val mHeaderInterceptor = Interceptor{
        chain ->
        val original = chain.request()
        val builder = original.newBuilder()

        builder.method(original.method(),original.body())
        chain.proceed(builder.build())
    }
}

@Keep // In case to be minified
data class ApiConfig(
    val apiHost: String = BuildConfig.H5_BASE_HOST
)