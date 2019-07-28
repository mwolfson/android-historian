package com.designdemo.uaha.view.product.fav

import androidx.lifecycle.LiveData
import com.designdemo.uaha.data.model.product.ProductEntity
import kotlinx.coroutines.Job

interface FavViewModel {
    fun insert(productEntity: ProductEntity): Job
    fun getFavData(): LiveData<List<ProductEntity>>
}