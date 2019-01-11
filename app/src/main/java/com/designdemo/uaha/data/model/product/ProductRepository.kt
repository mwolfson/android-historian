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

}