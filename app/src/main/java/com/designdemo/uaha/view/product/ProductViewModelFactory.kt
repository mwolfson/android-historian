package com.designdemo.uaha.view.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.designdemo.uaha.data.model.product.ProductRepository

class ProductViewModelFactory(private val productRepository: ProductRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProductViewModel(productRepository) as T
    }
}