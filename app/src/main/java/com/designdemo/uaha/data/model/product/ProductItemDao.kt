package com.designdemo.uaha.data.model.product

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductItemDao {

    @Query("SELECT * FROM product_table ORDER BY title ASC")
    fun getAllProductInfo(): LiveData<List<ProductEntity>>

    @Query("SELECT * FROM product_table WHERE product_type = 1 ORDER BY title ASC")
    fun getAllOses(): LiveData<List<ProductEntity>>

    @Query("SELECT * FROM product_table WHERE product_type = 2 ORDER BY title ASC")
    fun getAllDevices(): LiveData<List<ProductEntity>>

    @Query("SELECT * FROM product_table WHERE isFav = 1 ORDER BY title ASC")
    fun getAllFaves(): LiveData<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(productEntity: ProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItemList(entities: List<ProductEntity>)

    @Query("SELECT * FROM product_table WHERE title = :prodNameIn LIMIT 1")
    fun getProductItem(prodNameIn: String): LiveData<ProductEntity>

    @Query("DELETE FROM product_table")
    fun deleteAll()

    @Query("DELETE FROM product_table WHERE product_type = 1")
    fun deleteAllOS()

    @Query("DELETE FROM product_table WHERE product_type = 2")
    fun deleteAllDevices()

    // These are only used in  DB, LiveData objects should be used for most other cases
    @Query("SELECT * FROM product_table  WHERE product_type = 1 ORDER BY title ASC")
    fun getOsItems(): List<ProductEntity>

    @Query("SELECT * FROM product_table  WHERE product_type = 2 ORDER BY title ASC")
    fun getDeviceItems(): List<ProductEntity>
}
