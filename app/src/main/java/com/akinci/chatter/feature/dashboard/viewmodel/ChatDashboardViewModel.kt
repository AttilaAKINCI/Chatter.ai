package com.akinci.chatter.feature.dashboard.viewmodel

import androidx.lifecycle.*
import com.akinci.chatter.common.coroutine.CoroutineContextProvider
import com.akinci.chatter.common.extension.getCurrentDateAsTimeStamp
import com.akinci.chatter.common.extension.getRandomString
import com.akinci.chatter.common.extension.getRandomThreeDigits
import com.akinci.chatter.common.helper.Resource
import com.akinci.chatter.common.storage.PreferenceConfig
import com.akinci.chatter.common.storage.Preferences
import com.akinci.chatter.feature.acommon.data.local.entities.MessageEntity
import com.akinci.chatter.feature.acommon.data.local.entities.UserEntity
import com.akinci.chatter.feature.acommon.data.local.entities.relations.MessageWithUser
import com.akinci.chatter.feature.dashboard.data.output.Message
import com.akinci.chatter.feature.dashboard.data.output.MessageHistoryResponse
import com.akinci.chatter.feature.dashboard.repository.MessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChatDashboardViewModel @Inject constructor(
    private val coroutineContext : CoroutineContextProvider,
    private val messageRepository: MessageRepository,
    private val preferences: Preferences
) : ViewModel() {

    companion object{
        const val SUCCESS_SIGN_OUT = "SUCCESS_SIGN_OUT"
        const val SUCCESS_HISTORY_FETCH = "SUCCESS_HISTORY_FETCH"
    }

    private val _eventHandler = MutableLiveData<Resource<String>>()
    val eventHandler : LiveData<Resource<String>> = _eventHandler

    private var loggedUser : UserEntity? = null
    private val _loggedInUser = MutableLiveData<UserEntity>()
    val loggedInUser : LiveData<UserEntity> = _loggedInUser

    var firstMessageUserId : Long = 0

    private var messageHistoryResponse : MessageHistoryResponse? = null

    init {
        Timber.d("ChatDashboardViewModel created..")
    }

    fun sendMessage(message: String, messageOwner: Long? = null) = viewModelScope.launch(coroutineContext.IO) {
        Timber.tag("sendMessage-VMScope").d("Top-level: current thread is ${Thread.currentThread().name}")
        // send logged in user message to DB
        // normally I should send it remote server but rest endpoints are limited.

        loggedInUser.value?.let {
            if(message.isNotEmpty() && message.isNotBlank()){
                val userMessage = MessageEntity(
                        dataOwnerId = it.id,
                        id = "${getRandomString(3)}_${getRandomThreeDigits()}",
                        text = message,
                        timestamp = getCurrentDateAsTimeStamp(),
                        messageOwnerId = messageOwner ?: it.id
                )

                messageRepository.insertMessage(userMessage)
            } else{
                _eventHandler.postValue(Resource.Info("Message can not be empty and blank"))
            }
        }
    }

    fun fetchInitials() = viewModelScope.launch(coroutineContext.IO) {
        Timber.tag("fetchInitials-VMScope").d("Top-level: current thread is ${Thread.currentThread().name}")
        // fetch logged in user information from DB
        messageRepository.getLoggedInUser(preferences.getStoredTag(PreferenceConfig.USER_NAME))?.let {
            loggedUser = it
        }

        loggedUser?.let {
            // send user info to UI layer
            _loggedInUser.postValue(it)
        }

        // check rest response for new message history
        fetchMessageHistory()
    }

    // fetch message data from DB
    fun fetchRecentMessages() = messageRepository.getUserRecentMessages(loggedUser!!.id)

    suspend fun fetchMessageHistory() {
        // fectch history data once
        if (messageHistoryResponse == null) {
            // owner user should be known before fetching history data
            loggedUser?.let {
                when (val messageResponse = messageRepository.getUserMessageHistory()) {
                    is Resource.Success -> {
                        // Chat history is fetched
                        Timber.d("Chat history is fetched...")
                        messageResponse.data?.let { historyData ->
                            messageHistoryResponse = historyData
                            try {
                                //insert messaging data to ROOM DB

                                //store first message owner id for fake chatting feature..
                                if(historyData.messages.size > 0){
                                    firstMessageUserId = historyData.messages[0].user.id.toLong()
                                }

                                messageRepository.insertUserHistoryMessages(
                                    it.id,
                                    historyData.messages
                                )
                                _eventHandler.postValue(Resource.Success(SUCCESS_HISTORY_FETCH))
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
                    else -> { /* NO MORE NEEDED */ }
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
        _eventHandler.postValue(Resource.Success(SUCCESS_SIGN_OUT))
    }

}