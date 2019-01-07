package com.designdemo.uaha.data.model.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class DetailEntityDao {

    private val detailEntityLocal = mutableMapOf<String, DetailEntity>()

    private val detailList = MutableLiveData<Map<String, DetailEntity>>()

    init {
        detailList.value = detailEntityLocal
    }

    fun addDetail(mapName: String, detailItem: DetailEntity) {
        detailEntityLocal.put(mapName, detailItem)

        detailList.value = detailEntityLocal
    }

    fun getDetailData() = detailList as LiveData<Map<String, DetailEntity>>
}