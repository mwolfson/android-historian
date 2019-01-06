package com.designdemo.uaha.data.model.detail

class DetailRepository private constructor(private val detailEntityDao: DetailEntityDao){

    fun addDetail(mapName: String, detailItem: DetailEntity) {
        detailEntityDao.addDetail(mapName, detailItem)
    }

    fun getDetailData() = detailEntityDao.getDetailData()

    companion object {
        @Volatile private var instance: DetailRepository?= null

        fun getInstance(detailEntityDao: DetailEntityDao) =
                instance ?: synchronized(this) {
                    instance ?: DetailRepository(detailEntityDao). also { instance = it }
                }
    }
}