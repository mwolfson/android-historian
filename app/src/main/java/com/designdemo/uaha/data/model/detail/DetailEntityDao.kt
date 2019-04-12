package com.designdemo.uaha.data.model.detail

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DetailEntityDao {

    @Query("SELECT * FROM detail_table ORDER BY DeviceName ASC")
    fun getAllDetailItems(): LiveData<List<DetailEntity>>

    @Query("SELECT * FROM detail_table WHERE productKey = :deviceNameIn LIMIT 1")
    fun getDetailItem(deviceNameIn: String): LiveData<DetailEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addDetailItem(detailItem: DetailEntity)

    @Query("DELETE FROM detail_table")
    fun deleteAll()
}
