package com.akinci.chatter.feature.splash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.akinci.chatter.common.storage.PreferenceConfig
import com.akinci.chatter.common.storage.Preferences
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    preferences: Preferences
) : ViewModel() {

    private val _isLoggedIn = MutableLiveData<String>()
    val isLoggedIn : LiveData<String> = _isLoggedIn

    init {
        Timber.d("LoginViewModel created..")

        // check user previously logged in
        _isLoggedIn.postValue(preferences.getStoredTag(PreferenceConfig.IS_LOGGED_IN))
    }
}