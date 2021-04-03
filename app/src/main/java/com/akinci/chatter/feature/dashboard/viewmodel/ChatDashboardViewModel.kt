package com.akinci.chatter.feature.dashboard.viewmodel

import androidx.lifecycle.*
import com.akinci.chatter.common.coroutine.CoroutineContextProvider
import com.akinci.chatter.common.helper.Resource
import com.akinci.chatter.common.storage.PreferenceConfig
import com.akinci.chatter.common.storage.Preferences
import com.akinci.chatter.feature.acommon.data.local.entities.UserEntity
import com.akinci.chatter.feature.acommon.data.local.entities.relations.MessageWithUser
import com.akinci.chatter.feature.dashboard.data.output.MessageHistoryResponse
import com.akinci.chatter.feature.dashboard.repository.MessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ChatDashboardViewModel @Inject constructor(
    private val coroutineContext : CoroutineContextProvider,
    private val messageRepository: MessageRepository,
    private val preferences: Preferences
) : ViewModel() {

    private val _eventHandler = MutableLiveData<Resource<String>>()
    val eventHandler : LiveData<Resource<String>> = _eventHandler

    private val _loggedInUser = MutableLiveData<UserEntity>()
    val loggedInUser : LiveData<UserEntity> = _loggedInUser

    private val _recentMessages = MutableLiveData<Resource<List<MessageWithUser>>>()
    val recentMessages : LiveData<Resource<List<MessageWithUser>>> = _recentMessages

    private var messageHistoryResponse : MessageHistoryResponse? = null

    init {
        Timber.d("ChatDashboardViewModel created..")
    }

    fun fetchInitials() = viewModelScope.launch(coroutineContext.IO) {
        Timber.tag("fetchInitials-VMScope").d("Top-level: current thread is ${Thread.currentThread().name}")
        // fetch logged in user information from DB
        val loggedInUser = messageRepository.getLoggedInUser(preferences.getStoredTag(PreferenceConfig.USER_NAME))

        loggedInUser?.let {
            // send user info to UI layer
            withContext(Dispatchers.Main){
                _loggedInUser.value = it // immediately set the result and inform observers
            }
        }

        // check rest response for new message history
        fetchMessageHistory()

        // fetch message data from DB
        fetchRecentMessages()
    }

    suspend fun fetchRecentMessages(){
        // fetch recently updated messages once from ROOM DB
        if (_recentMessages.value == null) {
            // owner user should be known before fetching history data
            _loggedInUser.value?.let {
                //fetch recent message data
                val messageResponse = messageRepository.getUserRecentMessages(it.id)
                _recentMessages.postValue(Resource.Success(messageResponse))
            }
        }
    }

    suspend fun fetchMessageHistory() {
        // fectch history data once
        if (messageHistoryResponse == null) {
            // owner user should be known before fetching history data
            _loggedInUser.value?.let {
                when (val messageResponse = messageRepository.getUserMessageHistory()) {
                    is Resource.Success -> {
                        // Chat history is fetched
                        Timber.d("Chat history is fetched...")
                        messageResponse.data?.let { historyData ->
                            messageHistoryResponse = historyData
                            try {
                                //insert messaging data to ROOM DB
                                messageRepository.insertUserHistoryMessages(
                                    it.id,
                                    historyData.messages
                                )
                            } catch (ex: Exception) {
                                Timber.d("Message history data couldn't be inserted ${ex.message}")
                                _eventHandler.postValue(Resource.Error("Message History data couldn't be inserted."))
                            }
                        }
                    }
                    is Resource.Error -> {
                        // error occurred while fetching chat history
                        _eventHandler.postValue(Resource.Error(messageResponse.message))
                    }
                    else -> { // NO MORE NEEDED }
                    }
                }
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