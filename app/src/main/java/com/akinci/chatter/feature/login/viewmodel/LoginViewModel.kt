package com.akinci.chatter.feature.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akinci.chatter.common.coroutine.CoroutineContextProvider
import com.akinci.chatter.common.helper.Resource
import com.akinci.chatter.common.storage.PreferenceConfig
import com.akinci.chatter.common.storage.Preferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val coroutineContext : CoroutineContextProvider,
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
            preferences.setStoredTag(PreferenceConfig.USER_NAME, it)
            preferences.setStoredTag(PreferenceConfig.IS_LOGGED_IN, "Logged-in")


            // After setting a shared preferences. Create a user.
            // TODO

            // navigate user to chat dashboard
            _loginEventHandler.postValue(Resource.Info("Successfully Logged in. Navigating to Chat Dashboard"))
            delay(1500)
            _loginEventHandler.postValue(Resource.Success(true))
        }
    }
}