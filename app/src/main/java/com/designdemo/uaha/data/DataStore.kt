package com.designdemo.uaha.data

import com.designdemo.uaha.data.model.detail.DetailEntityDao
import com.designdemo.uaha.data.model.product.ProductItemDao
import com.designdemo.uaha.data.model.user.UserInfoDao

class DataStore private constructor() {

    var productDao = ProductItemDao()
        private set

    var userDao = UserInfoDao()
        private set

    var detailDao = DetailEntityDao()
        private set

    companion object {
        // @Volatile - Writes to this property are immediately visible to other threads
        @Volatile
        private var instance: DataStore? = null


        // The only way to get hold of the FakeDatabase object
        fun getInstance() =
        // Already instantiated? - return the instance
        // Otherwise instantiate in a thread-safe manner
                instance ?: synchronized(this) {
                    // If it's still not instantiated, finally create an object
                    // also set the "instance" property to be the currently created one
                    instance
                            ?: DataStore().also { instance = it }
                }
    }
}