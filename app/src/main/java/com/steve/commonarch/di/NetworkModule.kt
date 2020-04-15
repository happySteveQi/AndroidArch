package com.steve.commonarch.di

import com.steve.commonarch.base.ApiService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.*
import javax.inject.Singleton

@Module
class NetworkModule {

//    @Provides
//    fun provideRetrofit(logInterceptor:HttpLoggingInterceptor):Retrofit{
//        val defaultOkHttp = OkHttpClient.Builder()
//            .readTimeout()
//    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}