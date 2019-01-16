package com.designdemo.uaha.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.designdemo.uaha.data.model.detail.DetailEntity
import com.designdemo.uaha.data.model.detail.DetailEntityDao
import com.designdemo.uaha.data.model.product.ProductEntity
import com.designdemo.uaha.data.model.product.ProductItemDao
import com.designdemo.uaha.data.model.user.UserEntity
import com.designdemo.uaha.data.model.user.UserInfoDao
import com.support.android.designlibdemo.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [UserEntity::class, ProductEntity::class, DetailEntity::class], version = 1)
abstract class InfoDatabase : RoomDatabase() {
    abstract fun userDao(): UserInfoDao
    abstract fun productDao() : ProductItemDao
    abstract fun detailDao() : DetailEntityDao

    companion object {
        @Volatile
        private var INSTANCE: InfoDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): InfoDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, InfoDatabase::class.java,"user_database")
                        // Wipes and rebuilds instead of migrating if no Migration object.
                        .fallbackToDestructiveMigration()
                        .addCallback(InfoDatabaseCallback(scope))
                        .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class InfoDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
            /**
             * Override the onOpen method to populate the database.
             * For this sample, we clear the database every time it is created or opened.
             */
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.userDao(), database.productDao())
                    }
                }
            }
        }

        /**
         * Populate the database in a new coroutine.
         * This is where more UserEntity data could be added to prime the database.
         */
        fun populateDatabase(userDao: UserInfoDao, productItemDao: ProductItemDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            userDao.deleteAll()
            userDao.insert(UserEntity("Preset","5555555555","presetPW"))

            productItemDao.deleteAll()
//            //Device Items Pre-load
            if(productItemDao.getDeviceItems().isEmpty()) {
                productItemDao.deleteAllDevices()
                productItemDao.insertItem(ProductEntity("Dream","HTC", R.drawable.ic_device_g1, "HTC%20G1", 2))
                productItemDao.insertItem(ProductEntity("Droid","Motorola", R.drawable.ic_device_droid, "Motorola%20Droid", 2))
                productItemDao.insertItem(ProductEntity("Nexus One","HTC", R.drawable.ic_device_n1, "Samsung%20Galaxy%20Nexus", 2))
                productItemDao.insertItem(ProductEntity("Galaxy Nexus","Samsung", R.drawable.ic_device_gnex, "HTC%20Nexus%201", 2))
                productItemDao.insertItem(ProductEntity("Nexus S","Samsung", R.drawable.ic_device_nexs, "Samsung%20Nexus%20S", 2))
                productItemDao.insertItem(ProductEntity("Nexus 4","LG", R.drawable.ic_device_n4, "LG%20Nexus%204", 2))
                productItemDao.insertItem(ProductEntity("Nexus 5","LG", R.drawable.ic_device_n5, "LG%20Nexus%205", 2))
                productItemDao.insertItem(ProductEntity("Nexus 6","Motorola", R.drawable.ic_device_n6, "Motorola%20Nexus%206", 2))
                productItemDao.insertItem(ProductEntity("Nexus 7","Asus", R.drawable.ic_device_n7, "ASUS%20Nexus%207", 2))
                productItemDao.insertItem(ProductEntity("Nexus 9","HTC", R.drawable.ic_device_n9, "HTC%20Nexus%209", 2))
                productItemDao.insertItem(ProductEntity("Pixel","Google", R.drawable.ic_device_p, "Pixel-Google", 2))
                productItemDao.insertItem(ProductEntity("Pixel XL","Google", R.drawable.ic_device_px, "Pixel XL-Google", 2))
                productItemDao.insertItem(ProductEntity("Pixel2","Google", R.drawable.ic_device_p2, "Pixel2-Google", 2))
                productItemDao.insertItem(ProductEntity("Pixel2 XL","Google", R.drawable.ic_device_p2x, "Pixel2 XL-Google", 2))
                productItemDao.insertItem(ProductEntity("Pixel3","Google", R.drawable.ic_device_p3, "Pixel3-Google", 2))
                productItemDao.insertItem(ProductEntity("Pixel3 XL","Google", R.drawable.ic_device_p3x, "HTC%20Nexus%209", 2))
            }
//
//            // OS Items Pre-load
            if(productItemDao.getOsItems().isEmpty()) {
                productItemDao.deleteAllOS()
                productItemDao.insertItem(ProductEntity("Cupcake","API Level 3", R.drawable.ic_cupcake, "Android%20Cupcake", 1))
                productItemDao.insertItem(ProductEntity("Donut","API Level 4", R.drawable.ic_donut, "Android%20Donut", 1))
                productItemDao.insertItem(ProductEntity("Eclair","API Level 5,6,7", R.drawable.ic_eclair, "Android%20Eclair", 1))
                productItemDao.insertItem(ProductEntity("Froyo","API Level 8", R.drawable.ic_froyo, "Android%20Froyo", 1))
                productItemDao.insertItem(ProductEntity("Gingerbread","API Level 9,10", R.drawable.ic_gingerbread, "Android%20Gingerbread", 1))
                productItemDao.insertItem(ProductEntity("Honeycomb","API Level 11, 12, 13", R.drawable.ic_honeycomb, "Android%20Honeycomb", 1))
                productItemDao.insertItem(ProductEntity("Ice Cream Sandwich","API Level 14, 15", R.drawable.ic_ics, "Android%20Ice%20Cream%20Sandwich", 1))
                productItemDao.insertItem(ProductEntity("Jelly Bean","API Level 16, 17, 18", R.drawable.ic_jb, "Android%20Jelly%220Bean", 1))
                productItemDao.insertItem(ProductEntity("KitKat","API Level 19, 20", R.drawable.ic_kitkat, "Android%20KitKat", 1))
                productItemDao.insertItem(ProductEntity("Lollipop","API Level 21, 22", R.drawable.ic_lollipop, "Android%20Lollipop", 1))
                productItemDao.insertItem(ProductEntity("Marshmallow","API Level 23", R.drawable.ic_marshmallow, "Android%20Marshmallow", 1))
                productItemDao.insertItem(ProductEntity("Nougat","API Level 24, 25", R.drawable.ic_nougat, "Android%20Nougat", 1))
                productItemDao.insertItem(ProductEntity("Oreo","API Level 26, 27", R.drawable.ic_oreo, "Android%20Oreo", 1))
                productItemDao.insertItem(ProductEntity("Pie","API Level 28", R.drawable.ic_pie, "Android%20Pie", 1))
            }
        }
    }
}