package com.designdemo.uaha.data.wiki

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Revisions {

    @SerializedName("warnings")
    @Expose
    var warnings: String? = null

}
