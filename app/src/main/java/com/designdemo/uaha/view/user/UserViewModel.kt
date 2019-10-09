package com.designdemo.uaha.view.user

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.designdemo.uaha.data.InfoDatabase
import com.designdemo.uaha.data.model.user.UserEntity
import com.designdemo.uaha.data.model.user.UserRepository
import com.designdemo.uaha.util.TAG_WORK_NOTIF
import com.designdemo.uaha.util.NAME_MIN
import com.designdemo.uaha.util.NAME_MAX
import com.designdemo.uaha.util.PHONE_LENGTH
import com.designdemo.uaha.util.PASSWORD_MIN
import com.designdemo.uaha.util.PREF_DARK_MODE
import com.designdemo.uaha.workers.NotifWorker
import com.support.android.designlibdemo.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class UserViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        private const val TAG = "UserViewModel"
    }

    private var parentJob = Job()
    private val couroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(couroutineContext)

    val repository: UserRepository
    val allUserEntity: LiveData<List<UserEntity>>

    internal val outputWorkInfos: LiveData<List<WorkInfo>>

    private val workManager: WorkManager = WorkManager.getInstance()

    init {
        val userInfoDao = InfoDatabase.getDatabase(application, scope).userDao()
        repository = UserRepository(userInfoDao)
        allUserEntity = repository.allUserEntity
        outputWorkInfos = workManager.getWorkInfosByTagLiveData(TAG_WORK_NOTIF)
    }

    // Note, we are calling this from the addUserData, after validation is performed
    fun insert(userEntity: UserEntity) = scope.launch(Dispatchers.IO) {
        repository.insert(userEntity)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    private val saveStatusCode = MutableLiveData<Int>()

    fun getAddUserStatus() = saveStatusCode

    /**
     * This method will validate the input, and either return a error
     */
    fun addUserData(userEntity: UserEntity) {
        val isNameValid: () -> Int = {
            var retVal = 0
            if (!(userEntity.name.length in NAME_MIN..NAME_MAX)) {
                retVal = R.string.name_input_error
            }
            retVal
        }

        val isPhoneValid: () -> Int = {
            var retVal = 0
            if (userEntity.phone.length != PHONE_LENGTH) {
                retVal = R.string.phone_input_error
            }
            retVal
        }

        val isPasswordValid: () -> Int = {
            var retVal = 0
            if (userEntity.password.length != PASSWORD_MIN) {
                retVal = R.string.invalid_password
            }
            retVal
        }

        // Check if each item is valid, then show apply error as appropriate
        val phoneError = isPhoneValid()
        val nameError = isNameValid()
        val passwordError = isPasswordValid()

        if (phoneError != 0 || nameError != 0 || passwordError != 0) {
            if (nameError != 0) {
                saveStatusCode.postValue(nameError)
            } else if (phoneError != 0) {
                saveStatusCode.postValue(phoneError)
            } else if (passwordError != 0) {
                saveStatusCode.postValue(passwordError)
            }
        } else {
            insert(userEntity)
            saveStatusCode.postValue(R.string.profile_saved_confirm)
        }
    }

    fun startNotif() {
        val notifRequest =
                PeriodicWorkRequestBuilder<NotifWorker>(15, TimeUnit.MINUTES)
                        .addTag(TAG_WORK_NOTIF)
                        .build()

        workManager.enqueue(notifRequest)
    }

    fun cancelNotif() {
        workManager.cancelAllWorkByTag(TAG_WORK_NOTIF)
    }

    fun setDarkMode(radioClicked: Int, activity: Activity) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val editor = sharedPref.edit()

        var defaultNightMode = 0
        when (radioClicked) {
            R.id.radio_dark -> {
                Log.d(TAG, "Dark pressed " + radioClicked)
                defaultNightMode = MODE_NIGHT_YES
            }
            R.id.radio_light -> {
                Log.d(TAG, "Light pressed " + radioClicked)
                defaultNightMode = MODE_NIGHT_NO
            }
            R.id.radio_setting -> {
                Log.d(TAG, "Setting pressed " + radioClicked)
                // The default value is different after Android Q
                when (Build.VERSION.SDK_INT) {
                    in Int.MIN_VALUE..Build.VERSION_CODES.P -> defaultNightMode = MODE_NIGHT_AUTO_BATTERY
                    else -> defaultNightMode = MODE_NIGHT_FOLLOW_SYSTEM
                }
            }
        }

        val currentNightMode = AppCompatDelegate.getDefaultNightMode()
        Log.d(TAG, "MSW NightMode comparison is: $currentNightMode VS $defaultNightMode")

        if (currentNightMode != defaultNightMode) {
            AppCompatDelegate.setDefaultNightMode(defaultNightMode)

            Log.d("MSW", "the value we set is: " + defaultNightMode)
            editor.putInt(PREF_DARK_MODE, defaultNightMode)
            editor.commit()
        }
    }
}
