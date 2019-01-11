package com.designdemo.uaha.util

import com.designdemo.uaha.data.DataStore
import com.designdemo.uaha.data.model.detail.DetailRepository
import com.designdemo.uaha.view.detail.DetailViewModelFactory

object InjectorUtils {

    fun provideDetailViewModelFactory() : DetailViewModelFactory {
        val detailRepository = DetailRepository.getInstance(DataStore.getInstance().detailDao)
        return DetailViewModelFactory(detailRepository)
    }

}