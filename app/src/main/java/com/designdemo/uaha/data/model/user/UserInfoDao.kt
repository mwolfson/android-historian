package com.designdemo.uaha.data.model.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class UserInfoDao {

    //Store list of all user info here, instead of using a DB
    private val userList = mutableListOf<UserInfo>()

    // what to call this better?!
    private val userInfoList = MutableLiveData<List<UserInfo>>()

    init {
        userInfoList.value = userList
    }

    fun addUser(userInfo: UserInfo) {
        userList.add(userInfo)

        //after adding the item to the "data store", update value of MutableLiveData
        //This will notify observers that this has changed
        userInfoList.value = userList
    }

    // Casting this "Mutable" value, to LiveData, since it's value
    // shouldn't be changed from other classes
    fun getUserData() = userInfoList as LiveData<List<UserInfo>>

}