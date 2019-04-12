package com.designdemo.uaha.view.product.os

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.designdemo.uaha.data.InfoDatabase
import com.designdemo.uaha.data.model.product.ProductEntity
import com.designdemo.uaha.data.model.product.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class OsViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private var parentJob = Job()

    private val repository: ProductRepository
    private val allOses: LiveData<List<ProductEntity>>

    init {
        val productItemDao = InfoDatabase.getDatabase(application, this).productDao()
        repository = ProductRepository(productItemDao)

        allOses = repository.allOsInfo
    }

    fun insert(productEntity: ProductEntity) = launch(Dispatchers.IO) {
        repository.insertItem(productEntity)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun getOsData() = allOses
}
