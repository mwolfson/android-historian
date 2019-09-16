package com.designdemo.uaha.di.module

import android.content.Context
import com.designdemo.uaha.HistorianApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Singleton
    @Provides
    fun provideApplicationContext(application: HistorianApplication): Context =
            application.applicationContext
}