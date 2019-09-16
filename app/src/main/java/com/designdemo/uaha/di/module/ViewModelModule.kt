package com.designdemo.uaha.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.designdemo.uaha.di.ViewModelFactory
import com.designdemo.uaha.di.ViewModelKey
import com.designdemo.uaha.view.detail.DetailViewModelImpl
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Module class used to inject dependencies in the ViewModels
 */
@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModelImpl::class)
    abstract fun bindDetailViewModel(detailViewModelImpl: DetailViewModelImpl): ViewModel
}