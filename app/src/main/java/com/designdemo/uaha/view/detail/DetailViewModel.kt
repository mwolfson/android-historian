package com.designdemo.uaha.view.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.designdemo.uaha.data.InfoDatabase
import com.designdemo.uaha.data.model.detail.DetailEntity
import com.designdemo.uaha.data.model.detail.DetailRepository
import com.designdemo.uaha.data.model.product.ProductEntity
import com.designdemo.uaha.data.model.product.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DetailViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private var parentJob = Job()

    private val repository: DetailRepository
    private val prodRepository: ProductRepository
    private val allDetails: LiveData<List<DetailEntity>>

    init {
        val detailItemDao = InfoDatabase.getDatabase(application, this).detailDao()
        repository = DetailRepository(detailItemDao)

        val prodItemDao = InfoDatabase.getDatabase(application, this).productDao()
        prodRepository = ProductRepository(prodItemDao)

        allDetails = repository.allDetailInfo
    }

    fun insert(detailItem: DetailEntity) = launch(Dispatchers.IO) {
        repository.insertDetailItem(detailItem)
    }

    fun insertFav(productEntity: ProductEntity) = launch(Dispatchers.IO) {
        prodRepository.insertItem(productEntity)
    }

    fun getDetailsForItem(deviceName: String): LiveData<DetailEntity> = repository.getDetailItem(deviceName)

    fun getProductForName(deviceName: String): LiveData<ProductEntity> = prodRepository.getProductItem(deviceName)

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

}