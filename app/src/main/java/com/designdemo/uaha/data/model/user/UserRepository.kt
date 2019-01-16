package com.designdemo.uaha.data.model.user

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class UserRepository(private val userInfoDao: UserInfoDao) {

    val allUserEntity: LiveData<List<UserEntity>> = userInfoDao.getAllUserInfo()

    @WorkerThread
    suspend fun insert(userEntity: UserEntity) {
        userInfoDao.insert(userEntity)
    }

}