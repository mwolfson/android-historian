package com.designdemo.uaha.data.model.wiki

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Query {

    @SerializedName("pages")
    @Expose
    var pages: List<Page>? = null
}
