package com.designdemo.uaha.data.model.detail

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class DetailRepository(private val detailEntityDao: DetailEntityDao) {

    val allDetailInfo: LiveData<List<DetailEntity>> = detailEntityDao.getAllDetailItems()

    fun getDetailItem(deviceName: String): LiveData<DetailEntity> {
        return detailEntityDao.getDetailItem(deviceName)
    }

    @WorkerThread
    suspend fun insertDetailItem(detailItem: DetailEntity) {
        detailEntityDao.addDetailItem(detailItem)
    }
}