package com.designdemo.uaha.data.model.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ProductItemDao {

    private val deviceListLocal = mutableListOf<ProductItem>()
    private val osListLocal = mutableListOf<ProductItem>()
    private val favListLocal = mutableListOf<ProductItem>()

    private val deviceList = MutableLiveData<List<ProductItem>>()
    private val osList = MutableLiveData<List<ProductItem>>()
    private val favList = MutableLiveData<List<ProductItem>>()

    init {
        deviceList.value = deviceListLocal
        osList.value = osListLocal
        favList.value = favListLocal
    }

    fun initDeviceList(deviceListIn: List<ProductItem>) {
        deviceListLocal.clear()
        deviceListLocal.addAll(deviceListIn)

        deviceList.value = deviceListLocal
    }

    fun initOsList(osListIn: List<ProductItem>) {
        osListLocal.clear()
        osListLocal.addAll(osListIn)

        osList.value = osListLocal
    }

    fun initFavList(favListIn: List<ProductItem>) {
        favListLocal.clear()
        favListLocal.addAll(favListIn)

        favList.value = favListLocal
    }

    fun getDeviceData() = deviceList as LiveData<List<ProductItem>>

    fun getOsData() = osList as LiveData<List<ProductItem>>

    fun getFavData() = favList as LiveData<List<ProductItem>>
}