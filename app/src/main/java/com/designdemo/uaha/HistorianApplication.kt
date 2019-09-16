package com.designdemo.uaha

import com.designdemo.uaha.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class HistorianApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out HistorianApplication> =
        DaggerAppComponent.builder().create(this@HistorianApplication)
}