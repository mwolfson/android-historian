package com.designdemo.uaha.view.product.os

import androidx.lifecycle.LiveData
import com.designdemo.uaha.data.model.product.ProductEntity
import kotlinx.coroutines.Job

interface OsViewModel {

    fun insert(productEntity: ProductEntity): Job
    fun getOsData(): LiveData<List<ProductEntity>>
}