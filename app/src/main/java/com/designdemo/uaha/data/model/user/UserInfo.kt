package com.designdemo.uaha.data.model.user

data class UserInfo(val name: String, val phone: String, val password: String) {

    override fun toString(): String {
        return "$name - $phone - $password"
    }
}