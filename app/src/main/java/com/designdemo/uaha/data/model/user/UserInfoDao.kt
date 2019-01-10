package com.designdemo.uaha.data.model.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface UserInfoDao {

    @Query("SELECT * FROM user_table ORDER BY name ASC")
    fun getAllUserInfo(): LiveData<List<UserInfo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userInfo: UserInfo)

    @Query("DELETE FROM user_table")
    fun deleteAll()

}