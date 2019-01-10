package com.designdemo.uaha.data.model.user

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class UserRepository(private val userInfoDao: UserInfoDao) {

    val allUserInfo: LiveData<List<UserInfo>> = userInfoDao.getAllUserInfo()

    @WorkerThread
    suspend fun insert(userInfo: UserInfo) {
        userInfoDao.insert(userInfo)
    }

}