package com.designdemo.uaha.view.product.device

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.designdemo.uaha.data.InfoDatabase
import com.designdemo.uaha.data.model.product.ProductItem
import com.designdemo.uaha.data.model.product.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DeviceViewModel(application: Application) : AndroidViewModel(application), CoroutineScope  {
    override val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private var parentJob = Job()

    private val repository : ProductRepository
    private val allDevices : LiveData<List<ProductItem>>

    init {
        val productItemDao = InfoDatabase.getDatabase(application, this).productDao()
        repository = ProductRepository(productItemDao)

        allDevices = repository.allDeviceInfo
    }

    fun insert(productItem: ProductItem) = launch(Dispatchers.IO){
        repository.insertItem(productItem)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun getDeviceData() = allDevices


}