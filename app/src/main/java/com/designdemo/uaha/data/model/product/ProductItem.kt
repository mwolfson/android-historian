package com.designdemo.uaha.data.model.product

data class ProductItem (val title:String, val subTitle: String, val imgId: Int, val wikiQuery: String) {
    override fun toString(): String {
        return "$title - $subTitle - $imgId - $wikiQuery"
    }
}