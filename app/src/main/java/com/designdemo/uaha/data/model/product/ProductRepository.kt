package com.designdemo.uaha.data.model.product

class ProductRepository private constructor(private val productItemDao: ProductItemDao) {

    fun initDeviceData(deviceList: List<ProductItem>) {
        productItemDao.initDeviceList(deviceList)
    }

    fun initOsList(osList: List<ProductItem>) {
        productItemDao.initOsList(osList)
    }

    fun initFavesList(osList: List<ProductItem>) {
        productItemDao.initFavList(osList)
    }

    fun getDeviceData() = productItemDao.getDeviceData()

    fun getOsData() = productItemDao.getOsData()

    fun getFavData() = productItemDao.getFavData()

    companion object {
        @Volatile private var instance: ProductRepository? = null

        fun getInstance(quoteDao: ProductItemDao) =
                instance ?: synchronized(this) {
                    instance ?: ProductRepository(quoteDao).also { instance = it }
                }
    }


}