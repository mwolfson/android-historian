package com.designdemo.uaha.util

import com.designdemo.uaha.data.DataStore
import com.designdemo.uaha.data.model.detail.DetailRepository
import com.designdemo.uaha.data.model.product.ProductRepository
import com.designdemo.uaha.data.model.user.UserRepository
import com.designdemo.uaha.view.detail.DetailViewModelFactory
import com.designdemo.uaha.view.product.ProductViewModelFactory
import com.designdemo.uaha.view.user.UserViewModelFactory

object InjectorUtils {

    fun provideProfileViewModelFactory() : UserViewModelFactory {
        val userRepository = UserRepository.getInstance(DataStore.getInstance().userDao)
        return UserViewModelFactory(userRepository)
    }

    fun provideProductViewModelFactory() : ProductViewModelFactory {
        val productRepository = ProductRepository.getInstance(DataStore.getInstance().productDao)
        return ProductViewModelFactory(productRepository)
    }

    fun provideDetailViewModelFactory() : DetailViewModelFactory {
        val detailRepository = DetailRepository.getInstance(DataStore.getInstance().detailDao)
        return DetailViewModelFactory(detailRepository)
    }

}