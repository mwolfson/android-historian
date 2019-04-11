package com.designdemo.uaha.view.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.designdemo.uaha.data.InfoDatabase
import com.designdemo.uaha.data.model.detail.DetailEntity
import com.designdemo.uaha.data.model.detail.DetailRepository
import com.designdemo.uaha.data.model.product.ProductEntity
import com.designdemo.uaha.data.model.product.ProductRepository
import com.designdemo.uaha.net.FonoApiFactory
import com.designdemo.uaha.net.WikiApiFactory
import com.support.android.designlibdemo.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import kotlin.coroutines.CoroutineContext

class DetailViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private var parentJob = Job()

    private val detailRepository: DetailRepository
    private val prodRepository: ProductRepository
    private val allDetails: LiveData<List<DetailEntity>>

    companion object {
        val TAG = "DetailViewModel"
        // To use the FONO API, you will need to add your own API key to the gradle.properties file
        // App will degrade gracefully if KEY is not found
        const val TOKEN = BuildConfig.FONO_API_KEY
    }

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

    fun updateProductFromRefresh(productIn: String) {
        val product = productIn.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        getFonoNetInfo(product[0])
    }

    fun getFonoNetInfo(productKeyIn: String) {
        progressBarVisibility.postValue(true)
        launch(Dispatchers.IO) {
            try {
                val product = productKeyIn.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val result = FonoApiFactory().create().getDevice(TOKEN, product[0], product[1], null).await()
                if (result.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        val resultList = result.body()!!

                        val detailToSave = resultList.last().copy(productKey = productKeyIn)
                        insertDetail(detailToSave)
                        progressBarVisibility.postValue(false)
                    }
                }
            } catch (e: Exception) {
                progressBarVisibility.postValue(false)
                Log.e(TAG, "Dang what is the exception $e", e)
            }
        }
    }

    fun getWikiNetInfo(queryIn: String) {
        launch(Dispatchers.IO) {
            try {
                val result = WikiApiFactory().create().getWikiResponse(queryIn).await()

                if (result.isSuccessful) {
                    val wikiItem = result.body()!!

                    Log.d(TAG, "The wiki response did return $wikiItem")
                } else {
                    Log.d(TAG, "WIKI response DIDN'T work")
                }
            } catch (ex: Exception) {
                Log.e(TAG, "Dang WIKI response wasn't right $ex", ex)
            }
        }
    }

    fun insertFav(productEntity: ProductEntity) = launch(Dispatchers.IO) {
        prodRepository.insertItem(productEntity)
    }

    fun getDetailsForItem(deviceName: String): LiveData<DetailEntity> = detailRepository.getDetailItem(deviceName)

    fun getProductForName(deviceName: String): LiveData<ProductEntity> = prodRepository.getProductItem(deviceName)

    private var progressBarVisibility = MutableLiveData<Boolean>()

    fun getProgressBarVisibility(): LiveData<Boolean> {
        return progressBarVisibility
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}