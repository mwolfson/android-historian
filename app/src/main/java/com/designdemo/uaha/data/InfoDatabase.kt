package com.designdemo.uaha.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.designdemo.uaha.data.model.user.UserInfo
import com.designdemo.uaha.data.model.user.UserInfoDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [UserInfo::class], version = 1)
public abstract class InfoDatabase : RoomDatabase() {
    abstract fun userDao(): UserInfoDao

    companion object {
        @Volatile
        private var INSTANCE: InfoDatabase? = null

        fun getDatabase(
                context: Context,
                scope: CoroutineScope
        ): InfoDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        InfoDatabase::class.java,
                        "user_database"
                )
                        // Wipes and rebuilds instead of migrating if no Migration object.
                        // Migration is not part of this codelab.
                        .fallbackToDestructiveMigration()
                        .addCallback(InfoDatabaseCallback(scope))
                        .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class InfoDatabaseCallback(
                private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
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
                        populateDatabase(database.userDao())
                    }
                }
            }
        }

        /**
         * Populate the database in a new coroutine.
         * This is where more UserInfo data could be added to prime the database.
         */
        fun populateDatabase(userDao: UserInfoDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            userDao.deleteAll()

            var userInfo = UserInfo("Preset","5555555555","presetPW")
            userDao.insert(userInfo)
        }
    }
}