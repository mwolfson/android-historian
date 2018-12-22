package com.designdemo.uaha.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class UserViewMode(
        context: Application) : AndroidViewModel(context) {

    val nameTextVal: MutableLiveData<String>? = null

    var nameText:String = "BoogerMike"
    public var phoneText:String ="5555555555"

//    public fun getNameText(): String { return nameText }
//    public fun setNameText(nameIn: String) {
//        nameText = nameIn
//    }
//
//    public fun getPhoneText(): String { return phoneText }
//    public fun setPhoneText(phoneIn: String) {
//        phoneText = phoneIn
//    }



//    @Nullable
//    @get:Nullable
//    var startTime: Long? = null
//        private set
//
//    fun setStartTime(startTime: Long) {
//        this.startTime = startTime
//    }
}