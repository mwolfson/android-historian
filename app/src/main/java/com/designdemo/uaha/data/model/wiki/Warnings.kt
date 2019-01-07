package com.designdemo.uaha.data.model.wiki

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Warnings {

    @SerializedName("main")
    @Expose
    var main: Main? = null
    @SerializedName("revisions")
    @Expose
    var revisions: Revisions? = null

}
