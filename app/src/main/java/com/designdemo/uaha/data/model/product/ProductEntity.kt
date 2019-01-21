package com.designdemo.uaha.data.model.product

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_table")
data class ProductEntity (@PrimaryKey @ColumnInfo(name="title")val title:String,
                          @ColumnInfo(name="subtitle")val subTitle: String?,
                          @ColumnInfo(name="img_id")val imgId: Int?,
                          @ColumnInfo(name="wiki_query")val wikiQuery: String?,
                          @ColumnInfo(name="product_type")val productType: Int?,
                          @ColumnInfo(name="isFav")val isFav: Int? = 0)