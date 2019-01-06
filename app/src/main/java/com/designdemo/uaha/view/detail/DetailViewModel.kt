package com.designdemo.uaha.view.detail

import androidx.lifecycle.ViewModel
import com.designdemo.uaha.data.model.detail.DetailEntity
import com.designdemo.uaha.data.model.detail.DetailRepository

class DetailViewModel(private val detailRepository: DetailRepository) : ViewModel() {

    fun getDetailData() = detailRepository.getDetailData()

    fun addDetail(mapName: String, detail: DetailEntity) {
        detailRepository.addDetail(mapName, detail)
    }
}