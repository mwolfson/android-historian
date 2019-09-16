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
import com.designdemo.uaha.data.model.product.ProductRepositoryImpl
import com.designdemo.uaha.net.FonoApiFactory
import com.designdemo.uaha.net.WikiApiFactory
import com.support.android.designlibdemo.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import kotlin.coroutines.CoroutineContext

class DetailViewModelImpl(application: Application) : AndroidViewModel(application), CoroutineScope, DetailViewModel {

    override val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private var parentJob = Job()

    private val detailRepository: DetailRepository
    private val prodRepositoryImpl: ProductRepositoryImpl
    private val allDetails: LiveData<List<DetailEntity>>

    companion object {
        val TAG = "DetailViewModelImpl"
        // To use the FONO API, you will need to add your own API key to the gradle.properties file
        // App will degrade gracefully if KEY is not found
        const val TOKEN = BuildConfig.FONO_API_KEY
    }

    init {
        val detailItemDao = InfoDatabase.getDatabase(application, this).detailDao()
        detailRepository = DetailRepository(detailItemDao)

        val prodItemDao = InfoDatabase.getDatabase(application, this).productDao()
        prodRepositoryImpl = ProductRepositoryImpl(prodItemDao)

        allDetails = detailRepository.allDetailInfo
    }

    override fun insertDetail(detailItem: DetailEntity) = launch(Dispatchers.IO) {
        detailRepository.insertDetailItem(detailItem)
    }

    override fun updateProductFromRefresh(productIn: String) {
        val product = productIn.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        getFonoNetInfo(product[0])
    }

    override fun getFonoNetInfo(productKeyIn: String) {
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

    override fun getWikiNetInfo(queryIn: String) {
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

    override fun insertFav(productEntity: ProductEntity) = launch(Dispatchers.IO) {
        prodRepositoryImpl.insertItem(productEntity)
    }

    override fun getDetailsForItem(deviceName: String) = detailRepository.getDetailItem(deviceName)

    override fun getProductForName(deviceName: String) = prodRepositoryImpl.getProductItem(deviceName)

    private var progressBarVisibility = MutableLiveData<Boolean>()

    override fun getProgressBarVisibility() = progressBarVisibility

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}
