package com.designdemo.uaha.view.user

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.designdemo.uaha.data.InfoDatabase
import com.designdemo.uaha.data.model.user.UserEntity
import com.designdemo.uaha.data.model.user.UserRepository
import com.support.android.designlibdemo.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private var parentJob = Job()
    private val couroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(couroutineContext)

    val repository : UserRepository
    val allUserEntity: LiveData<List<UserEntity>>

    init {
        val userInfoDao = InfoDatabase.getDatabase(application, scope).userDao()
        repository = UserRepository(userInfoDao)
        allUserEntity = repository.allUserEntity
    }

    //Note, we are calling this from the addUserData, after validation is performed
    fun insert(userEntity: UserEntity) = scope.launch(Dispatchers.IO){
        repository.insert(userEntity)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    private val saveStatusCode = MutableLiveData<Int>()

    fun getAddUserStatus() : LiveData<Int> {
        return saveStatusCode
    }

    /**
     * This method will validate the input, and either return a error
     */
    fun addUserData(userEntity: UserEntity) {
        val isNameValid: () -> Int = {
            var retVal = 0
            if (!(userEntity.name.length in 4..10)) {
                retVal = R.string.name_input_error
            }
            retVal
        }

        val isPhoneValid: () -> Int = {
            var retVal = 0
            if (userEntity.phone.length != 14) {
                retVal = R.string.phone_input_error
            }
            retVal
        }

        val isPasswordValid: () -> Int = {
            var retVal = 0
            if (userEntity.password.length != 8) {
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
            insert(userEntity)
            saveStatusCode.postValue(R.string.profile_saved_confirm)

        }
    }

}