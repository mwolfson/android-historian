package com.designdemo.uaha.data.model.wiki

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WikiResponse {

    @SerializedName("batchcomplete")
    @Expose
    var batchcomplete: Boolean? = null
    @SerializedName("warnings")
    @Expose
    var warnings: Warnings? = null
    @SerializedName("query")
    @Expose
    var query: Query? = null

}
