package com.steve.commonarch

import android.app.Activity
import android.app.Service
import android.content.Context
import androidx.multidex.MultiDexApplication
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import javax.inject.Inject

const val DEBUG = AppEnv.DEBUG
const val TAG = "AppInit"

class App : MultiDexApplication(), HasActivityInjector, HasServiceInjector {
    companion object {
        private lateinit var mAppContext: Context
        fun getAppContext(): Context = mAppContext
    }

    @Inject
    lateinit var mActivityInjector: DispatchingAndroidInjector<Activity>
    @Inject
    lateinit var mServiceInjector: DispatchingAndroidInjector<Service>

    override fun activityInjector(): AndroidInjector<Activity> = mActivityInjector

    override fun serviceInjector(): AndroidInjector<Service> = mServiceInjector

    override fun onCreate() {
        super.onCreate()
        mAppContext = applicationContext

    }
}