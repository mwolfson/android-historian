package com.designdemo.uaha.view.product.os

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.designdemo.uaha.data.InfoDatabase
import com.designdemo.uaha.data.model.product.ProductEntity
import com.designdemo.uaha.data.model.product.ProductRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class OsViewModelImpl(application: Application) : AndroidViewModel(application), CoroutineScope, OsViewModel {
    override val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private var parentJob = Job()

    private val repositoryImpl: ProductRepositoryImpl
    private val allOses: LiveData<List<ProductEntity>>

    init {
        val productItemDao = InfoDatabase.getDatabase(application, this).productDao()
        repositoryImpl = ProductRepositoryImpl(productItemDao)

        allOses = repositoryImpl.allOsInfo
    }

    override fun insert(productEntity: ProductEntity) = launch(Dispatchers.IO) {
        repositoryImpl.insertItem(productEntity)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    override fun getOsData() = allOses
}
