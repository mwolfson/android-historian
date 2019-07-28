package com.designdemo.uaha.view.product.device

import androidx.lifecycle.LiveData
import com.designdemo.uaha.data.model.product.ProductEntity
import kotlinx.coroutines.Job

interface DeviceViewModel {

    fun insert(productEntity: ProductEntity): Job
    fun getDeviceData(): LiveData<List<ProductEntity>>
}