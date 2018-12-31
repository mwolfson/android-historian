package com.designdemo.uaha.util

import com.designdemo.uaha.data.model.product.ProductItemStore
import com.designdemo.uaha.data.model.product.ProductRepository
import com.designdemo.uaha.data.model.user.UserDataStore
import com.designdemo.uaha.data.model.user.UserRepository
import com.designdemo.uaha.view.product.ProductViewModelFactory
import com.designdemo.uaha.view.user.UserViewModelFactory

object InjectorUtils {

    fun provideProfileViewModelFactory() : UserViewModelFactory {
        val userRepository = UserRepository.getInstance(UserDataStore.getInstance().userDao)
        return UserViewModelFactory(userRepository)
    }

    fun provideProductViewModelFactory() : ProductViewModelFactory {
        val productRepository = ProductRepository.getInstance(ProductItemStore.getInstance().productDao)
        return ProductViewModelFactory(productRepository)
    }

}