package com.designdemo.uaha.view.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.designdemo.uaha.data.InfoDatabase
import com.designdemo.uaha.data.model.detail.DetailEntity
import com.designdemo.uaha.data.model.detail.DetailRepository
import com.designdemo.uaha.data.model.product.ProductEntity
import com.designdemo.uaha.data.model.product.ProductRepository
import com.designdemo.uaha.net.FonoApiFactory
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class DetailViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private var parentJob = Job()

    private val detailRepository: DetailRepository
    private val prodRepository: ProductRepository
    private val allDetails: LiveData<List<DetailEntity>>

    init {
        val detailItemDao = InfoDatabase.getDatabase(application, this).detailDao()
        detailRepository = DetailRepository(detailItemDao)

        val prodItemDao = InfoDatabase.getDatabase(application, this).productDao()
        prodRepository = ProductRepository(prodItemDao)

        allDetails = detailRepository.allDetailInfo
    }

    fun insertDetail(detailItem: DetailEntity) = launch(Dispatchers.IO) {
        detailRepository.insertDetailItem(detailItem)
    }

    fun getFonoNetInfo(productKeyIn: String) {
        launch(Dispatchers.IO) {
            try {
                val product = productKeyIn.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val result = FonoApiFactory().create().getDevice(DetailActivity.TOKEN, product[0], product[1], null).await()
                if (result.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        val resultList = result.body()!!

                        val detailToSave = resultList.last().copy(productKey = productKeyIn)
                        insertDetail(detailToSave)
                    }
                }
            } catch (e: Exception) {
                Log.d("DetailActivityViewModel", "Dang what is the exception ${e.toString()}", e)
            }
        }
    }


    fun insertFav(productEntity: ProductEntity) = launch(Dispatchers.IO) {
        prodRepository.insertItem(productEntity)
    }

    fun getDetailsForItem(deviceName: String): LiveData<DetailEntity> = detailRepository.getDetailItem(deviceName)

    fun getProductForName(deviceName: String): LiveData<ProductEntity> = prodRepository.getProductItem(deviceName)

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

}