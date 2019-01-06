package com.designdemo.uaha.view.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.designdemo.uaha.data.model.detail.DetailRepository

class DetailViewModelFactory (private val detailRepository: DetailRepository) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailViewModel(detailRepository) as T
    }
}