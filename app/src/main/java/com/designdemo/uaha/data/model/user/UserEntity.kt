package com.designdemo.uaha.data.model.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey @ColumnInfo(name = "name")val name: String,
    @ColumnInfo(name = "phone")val phone: String,
    @ColumnInfo(name = "password")val password: String
)
