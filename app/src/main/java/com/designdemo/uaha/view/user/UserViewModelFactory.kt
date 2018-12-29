package com.designdemo.uaha.view.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.designdemo.uaha.data.model.user.UserRepository

class UserViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserViewModel(userRepository) as T
    }
}