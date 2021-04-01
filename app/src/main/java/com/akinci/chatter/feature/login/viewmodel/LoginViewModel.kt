package com.akinci.chatter.feature.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _isLoggedIn = MutableLiveData(false)
    val isLoggedIn : LiveData<Boolean> = _isLoggedIn

    // TODO only test purposes..... Not forget to delete initials later.
    var userName = MutableLiveData("") //akinciattila

    init {
        Timber.d("LoginViewModel created..")

    }

    fun actionLogin(){

    }

}