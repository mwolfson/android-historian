package com.designdemo.uaha.data.wiki

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Page {

    @SerializedName("pageid")
    @Expose
    var pageid: Int? = null
    @SerializedName("ns")
    @Expose
    var ns: Int? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("revisions")
    @Expose
    var revisions: List<Revision>? = null

}
