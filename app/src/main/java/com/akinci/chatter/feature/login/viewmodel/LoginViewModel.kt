package com.akinci.chatter.feature.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akinci.chatter.common.coroutine.CoroutineContextProvider
import com.akinci.chatter.common.extensions.getRandomLong
import com.akinci.chatter.common.helper.Resource
import com.akinci.chatter.common.storage.PreferenceConfig
import com.akinci.chatter.common.storage.Preferences
import com.akinci.chatter.feature.acommon.data.local.entities.UserEntity
import com.akinci.chatter.feature.login.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val coroutineContext : CoroutineContextProvider,
    private val loginRepository: LoginRepository,
    private val preferences: Preferences
) : ViewModel() {

    private val _loginEventHandler = MutableLiveData<Resource<Boolean>>()
    val loginEventHandler : LiveData<Resource<Boolean>> = _loginEventHandler

    // this live data is assigned to login page xml layout.
    // and adjusted as updatable from layout
    var userName = MutableLiveData<String>()

    init {
        Timber.d("LoginViewModel created..")
    }

    fun actionLogin() = viewModelScope.launch(coroutineContext.IO) {
        Timber.tag("actionLogin-VMScope").d("Top-level: current thread is ${Thread.currentThread().name}")
        // save user related information to shared preferences. And return a navigation action.
        userName.value?.let {

            // After setting a shared preferences. Create a user.
            try {
                /**
                 *  Normally in this stage login rest function should be called and
                 *  User should be created at server side. Return of the service call provides
                 *  user profile to clients.
                 *
                 *  Because of the limited rest endpoints I created user in room db myself.
                 *
                 * **/
                var userProfile = loginRepository.getLoggedInUser(it)
                if(userProfile == null){
                    // This user not signed up before. Create a new user
                    val user = UserEntity(
                        id = getRandomLong(),
                        loggedIdUser = true,
                        avatarURL = "https://picsum.photos/128/128", // get random profile image.
                        nickname = it
                    )

                    loginRepository.insertUser(user)
                    // User Created
                    _loginEventHandler.postValue(Resource.Info("Successfully Signed up. Navigating to Chat Dashboard"))
                    delay(1500)
                    userProfile = user
                }else{
                    _loginEventHandler.postValue(Resource.Info("User Previously Signed up. Logging in. Navigating to Chat Dashboard"))
                    delay(1500)
                }

                // navigate user to chat dashboard
                // save user specific values into shared preferences
                preferences.setStoredTag(PreferenceConfig.USER_NAME, userProfile.nickname)
                preferences.setStoredTag(PreferenceConfig.IS_LOGGED_IN, "Logged-in")

                _loginEventHandler.postValue(Resource.Success(true))
            }catch (ex : Exception){
                Timber.d("Login Error: ${ex.message}")
                _loginEventHandler.postValue(Resource.Error("User couldn't be created. Please try again later"))
            }
        }
    }
}