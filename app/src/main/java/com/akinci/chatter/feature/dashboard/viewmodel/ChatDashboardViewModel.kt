package com.akinci.chatter.feature.dashboard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akinci.chatter.common.coroutine.CoroutineContextProvider
import com.akinci.chatter.common.helper.Resource
import com.akinci.chatter.common.storage.PreferenceConfig
import com.akinci.chatter.common.storage.Preferences
import com.akinci.chatter.feature.dashboard.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChatDashboardViewModel @Inject constructor(
    private val coroutineContext : CoroutineContextProvider,
    private val chatRepository: ChatRepository,
    private val preferences: Preferences
) : ViewModel() {

    private val _eventHandler = MutableLiveData<Resource<String>>()
    val eventHandler : LiveData<Resource<String>> = _eventHandler

    private val _userName = MutableLiveData<String>()
    val userName : LiveData<String> = _userName

    init {
        Timber.d("ChatDashboardViewModel created..")

        // fetch user name
        _userName.postValue(preferences.getStoredTag(PreferenceConfig.USER_NAME))
    }

    fun fetchChatHistory() = viewModelScope.launch(coroutineContext.IO) {

        // this function should be call once ... Prevent dublicate calls.

        Timber.tag("chatHistory-VMScope").d("Top-level: current thread is ${Thread.currentThread().name}")
        when(val chatResponse = chatRepository.getUserChatHistory()) {
            is Resource.Success -> {
                // Chat history is fetched
                Timber.d("Chat history is fetched...")

            }
            is Resource.Error -> {
                // error occurred while fetching chat history
                _eventHandler.postValue(Resource.Error(chatResponse.message))
            }
        }
    }

    fun logout() = viewModelScope.launch(coroutineContext.IO) {
        // clear shared preferences data
        preferences.clear()

        // send information to UI in order to logout action
        _eventHandler.postValue(Resource.Info("Signing out. Navigating to Login"))
        delay(2000)
        _eventHandler.postValue(Resource.Success("Signed out...."))
    }

}