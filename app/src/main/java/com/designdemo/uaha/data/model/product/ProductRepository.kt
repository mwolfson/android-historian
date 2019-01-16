package com.designdemo.uaha.data.model.product

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class ProductRepository(private val productItemDao: ProductItemDao) {

    val allDeviceInfo: LiveData<List<ProductEntity>> = productItemDao.getAllDevices()
    val allOsInfo: LiveData<List<ProductEntity>> = productItemDao.getAllOses()
    val allFaveInfo: LiveData<List<ProductEntity>> = productItemDao.getAllFaves()

    fun getProductItem(prodName: String): LiveData<ProductEntity> {
        return productItemDao.getProductItem(prodName)
    }

    @WorkerThread
    suspend fun insertItem(productEntity: ProductEntity) {
        productItemDao.insertItem(productEntity)
    }

}