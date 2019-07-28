package com.designdemo.uaha.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.designdemo.uaha.data.model.detail.DetailEntity
import com.designdemo.uaha.data.model.product.ProductEntity
import kotlinx.coroutines.Job

interface DetailViewModel {
    fun insertDetail(detailItem: DetailEntity): Job
    fun updateProductFromRefresh(productIn: String)
    fun getFonoNetInfo(productKeyIn: String)
    fun getWikiNetInfo(queryIn: String)
    fun insertFav(productEntity: ProductEntity): Job
    fun getDetailsForItem(deviceName: String): LiveData<DetailEntity>
    fun getProductForName(deviceName: String): LiveData<ProductEntity>
    fun getProgressBarVisibility(): MutableLiveData<Boolean>
}