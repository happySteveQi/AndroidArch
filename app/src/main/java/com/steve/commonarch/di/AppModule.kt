package com.steve.commonarch.di

import android.app.Application
import android.content.Context
import com.steve.commonarch.App
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    includes = [AppContextModule::class]
)
class AppModule {
    @Singleton
    @Provides
    fun provideApp(application: App): Application = application
}

@Module
abstract class AppContextModule {
    @Singleton
    @Binds
    abstract fun context(application: App): Context
}