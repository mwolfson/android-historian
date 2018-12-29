package com.designdemo.uaha.view.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.designdemo.uaha.data.model.user.UserInfo
import com.designdemo.uaha.data.model.user.UserRepository
import com.support.android.designlibdemo.R

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    //This method provides way for View to observe repository live data
    fun getUserData() = userRepository.getUserData()

    private val saveStatusCode = MutableLiveData<Int>()

    fun getAddUserStatus() : LiveData<Int> {
        return saveStatusCode
    }

    fun addUserData(userInfo: UserInfo) {
        val isNameValid: () -> Int = {
            var retVal = 0
            if (!(userInfo.name.length in 4..10)) {
                retVal = R.string.name_input_error
            }
            retVal
        }

        val isPhoneValid: () -> Int = {
            var retVal = 0
            if (userInfo.phone.length != 14) {
                retVal = R.string.phone_input_error
            }
            retVal
        }

        val isPasswordValid: () -> Int = {
            var retVal = 0
            if (userInfo.password.length != 8) {
                retVal = R.string.invalid_password
            }
            retVal
        }

        //Check if each item is valid, then show apply error as appropriate
        val phoneError = isPhoneValid()
        val nameError = isNameValid()
        val passwordError = isPasswordValid()

        if (phoneError != 0 || nameError != 0 || passwordError != 0) {
            if (nameError != 0) {
                saveStatusCode.postValue(nameError)
            } else if (phoneError != 0) {
                saveStatusCode.postValue(phoneError)
            } else if ( passwordError != 0) {
                saveStatusCode.postValue(passwordError)
            }
        } else {
            //There weren't validation issues, so move forward
            userRepository.addUser(userInfo)
            saveStatusCode.postValue(R.string.profile_saved_confirm)

        }
    }

}