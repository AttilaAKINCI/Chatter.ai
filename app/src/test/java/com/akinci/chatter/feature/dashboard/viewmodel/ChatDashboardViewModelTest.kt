package com.akinci.chatter.feature.dashboard.viewmodel

import com.akinci.chatter.ahelper.InstantExecutorExtension
import com.akinci.chatter.ahelper.TestContextProvider
import com.akinci.chatter.common.helper.Resource
import com.akinci.chatter.common.storage.Preferences
import com.akinci.chatter.feature.dashboard.data.output.MessageHistoryResponse
import com.akinci.chatter.feature.dashboard.data.output.User
import com.akinci.chatter.feature.dashboard.data.output.convertUserToUserEntity
import com.akinci.chatter.feature.dashboard.repository.MessageRepository
import com.akinci.chatter.jsonresponses.GetChatMessagesResponse
import com.akinci.chatter.jsonresponses.GetUserResponse
import com.google.common.truth.Truth
import com.squareup.moshi.Moshi
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
class ChatDashboardViewModelTest {

    @MockK
    private lateinit var messageRepository: MessageRepository

    @MockK
    private lateinit var preferences: Preferences

    private lateinit var chatDashboardViewModel: ChatDashboardViewModel

    private val coroutineContext = TestContextProvider()
    private val moshi = Moshi.Builder().build()

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        chatDashboardViewModel = ChatDashboardViewModel(coroutineContext, messageRepository, preferences)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `logout and check parameters`() {
        justRun { preferences.clear() }

        chatDashboardViewModel.eventHandler.observeForever {
            when (it) {
                is Resource.Info -> {
                    Truth.assertThat(it.message).isEqualTo("Signing out. Navigating to Login")
                }
                is Resource.Success -> {
                    Truth.assertThat(it.data).isEqualTo(ChatDashboardViewModel.SUCCESS_SIGN_OUT)
                }
            }
        }

        //fire logout action
        chatDashboardViewModel.logout()
        coroutineContext.testCoroutineDispatcher.advanceUntilIdle()

        verify { preferences.clear() }
    }

    @Test
    fun `send message and check DB call`() {
        coJustRun { messageRepository.insertMessage(any()) }
        coEvery { messageRepository.getLoggedInUser(any()) } returns
                moshi.adapter(User::class.java).fromJson(GetUserResponse.userResponse)?.convertUserToUserEntity(true)
        coEvery { messageRepository.getUserMessageHistory() } returns Resource.Success(
                moshi.adapter(MessageHistoryResponse::class.java).fromJson(GetChatMessagesResponse.chatMessages)
        )
        coJustRun { messageRepository.insertUserHistoryMessages(any(), any()) }
        every { preferences.getStoredTag(any()) } returns "Nick 1"

        // this function fetches need parameters first
        chatDashboardViewModel.fetchInitials()
        coroutineContext.testCoroutineDispatcher.advanceUntilIdle()

        // check history is fetch successfully
        coVerify { messageRepository.insertUserHistoryMessages(any(), any()) }

        /** Check send message **/
        chatDashboardViewModel.loggedInUser.observeForever {
            chatDashboardViewModel.sendMessage("Sample Message", 1L)
            coVerify { messageRepository.insertMessage(any()) }
        }
    }




}