package com.designdemo.uaha.data.model.product

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

interface ProductRepository {
    val allDeviceInfo: LiveData<List<ProductEntity>>
    val allOsInfo: LiveData<List<ProductEntity>>
    val allFaveInfo: LiveData<List<ProductEntity>>
    fun getProductItem(prodName: String): LiveData<ProductEntity>

    @WorkerThread
    suspend fun insertItem(productEntity: ProductEntity)
}