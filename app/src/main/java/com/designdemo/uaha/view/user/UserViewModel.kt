package com.designdemo.uaha.view.user

import androidx.lifecycle.MutableLiveData
import com.designdemo.uaha.data.model.user.UserEntity
import kotlinx.coroutines.Job

@Suppress("NewLineAtEndOfFile")
interface UserViewModel {

    fun insert(userEntity: UserEntity): Job
    fun getAddUserStatus(): MutableLiveData<Int>
    fun addUserData(userEntity: UserEntity)
    fun startNotif()
    fun cancelNotif()
}