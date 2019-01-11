package com.designdemo.uaha.data.model.product

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductItemDao {

    @Query("SELECT * FROM product_table ORDER BY title ASC")
    fun getAllProductInfo(): LiveData<List<ProductItem>>

    @Query("SELECT * FROM product_table WHERE product_type = 1 ORDER BY title ASC")
    fun getAllOses(): LiveData<List<ProductItem>>

    @Query("SELECT * FROM product_table WHERE product_type = 2 ORDER BY title ASC")
    fun getAllDevices(): LiveData<List<ProductItem>>

    @Query("SELECT * FROM product_table WHERE isFav = 1  ORDER BY title ASC")
    fun getAllFaves(): LiveData<List<ProductItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(productItem: ProductItem)

    @Query("DELETE FROM product_table")
    fun deleteAll()
}