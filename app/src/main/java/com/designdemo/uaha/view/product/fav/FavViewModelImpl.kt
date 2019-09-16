package com.designdemo.uaha.view.product.fav

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.designdemo.uaha.data.InfoDatabase
import com.designdemo.uaha.data.model.product.ProductRepository
import com.designdemo.uaha.data.model.product.ProductEntity
import com.designdemo.uaha.data.model.product.ProductRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class FavViewModelImpl(application: Application) : AndroidViewModel(application), CoroutineScope, FavViewModel {
    override val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private var parentJob = Job()

    var repository: ProductRepository
    private val allFaves: LiveData<List<ProductEntity>>

    init {
        val productItemDao = InfoDatabase.getDatabase(application, this).productDao()
        repository = ProductRepositoryImpl(productItemDao)

        allFaves = repository.allFaveInfo
    }

    override fun insert(productEntity: ProductEntity) = launch(Dispatchers.IO) {
        repository.insertItem(productEntity)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    override fun getFavData() = allFaves
}
