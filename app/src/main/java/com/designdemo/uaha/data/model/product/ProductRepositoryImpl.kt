package com.designdemo.uaha.data.model.product

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class ProductRepositoryImpl(private val productItemDao: ProductItemDao) : ProductRepository {

    override val allDeviceInfo: LiveData<List<ProductEntity>> = productItemDao.getAllDevices()
    override val allOsInfo: LiveData<List<ProductEntity>> = productItemDao.getAllOses()
    override val allFaveInfo: LiveData<List<ProductEntity>> = productItemDao.getAllFaves()

    override fun getProductItem(prodName: String) = productItemDao.getProductItem(prodName)

    @WorkerThread
    override suspend fun insertItem(productEntity: ProductEntity) {
        productItemDao.insertItem(productEntity)
    }
}
