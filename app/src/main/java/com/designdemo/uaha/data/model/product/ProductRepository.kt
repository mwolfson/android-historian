package com.designdemo.uaha.data.model.product

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class ProductRepository(private val productItemDao: ProductItemDao) {

    val allDeviceInfo: LiveData<List<ProductItem>> = productItemDao.getAllDevices()
    val allOsInfo: LiveData<List<ProductItem>> = productItemDao.getAllOses()
    val allFaveInfo: LiveData<List<ProductItem>> = productItemDao.getAllFaves()

    @WorkerThread
    suspend fun insertItem(productItem: ProductItem) {
        productItemDao.insertItem(productItem)
    }


//    fun initDeviceData(deviceList: List<ProductItem>) {
//        productDao.initDeviceList(deviceList)
//    }
//
//    fun initOsList(osList: List<ProductItem>) {
//        productDao.initOsList(osList)
//    }
//
//    fun initFavesList(osList: List<ProductItem>) {
//        productDao.initFavList(osList)
//    }
//
//    fun getDeviceData() = productDao.getDeviceData()
//
//    fun getOsData() = productDao.getOsData()
//
//    fun getFavData() = productDao.getFavData()
//
//    companion object {
//        @Volatile private var instance: ProductRepository? = null
//
//        fun getInstance(quoteDao: ProductItemDao) =
//                instance ?: synchronized(this) {
//                    instance ?: ProductRepository(quoteDao).also { instance = it }
//                }
//    }


}