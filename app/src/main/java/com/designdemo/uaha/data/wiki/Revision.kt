package com.designdemo.uaha.data.wiki

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Revision {

    @SerializedName("contentformat")
    @Expose
    var contentformat: String? = null
    @SerializedName("contentmodel")
    @Expose
    var contentmodel: String? = null
    @SerializedName("content")
    @Expose
    var content: String? = null

}
