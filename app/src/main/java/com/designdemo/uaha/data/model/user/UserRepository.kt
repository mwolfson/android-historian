package com.designdemo.uaha.data.model.user

class UserRepository private constructor(private val userInfoDao: UserInfoDao) {

    //Seems redundant, but would be needed if there were additional update operations
    fun addUser(userInfo: UserInfo) {
        userInfoDao.addUser(userInfo)
    }

    fun getUserData() = userInfoDao.getUserData()

    companion object {
        @Volatile private var instance: UserRepository? = null

        fun getInstance(quoteDao: UserInfoDao) =
                instance ?: synchronized(this) {
                    instance ?: UserRepository(quoteDao).also { instance = it }
                }
    }
}