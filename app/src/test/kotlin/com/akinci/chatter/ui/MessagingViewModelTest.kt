package com.akinci.chatter.ui

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.akinci.chatter.core.coroutine.MainDispatcherRule
import com.akinci.chatter.core.coroutine.TestContextProvider
import com.akinci.chatter.data.exception.UserNotFound
import com.akinci.chatter.domain.ChatSimulator
import com.akinci.chatter.domain.GetPrimaryUserUseCase
import com.akinci.chatter.domain.data.ChatSession
import com.akinci.chatter.domain.data.MessageItem
import com.akinci.chatter.domain.data.User
import com.akinci.chatter.ui.features.messaging.MessagingViewModel
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MainDispatcherRule::class)
class MessagingViewModelTest {

    private val testContextProvider = TestContextProvider()
    private val getPrimaryUserUseCaseMock: GetPrimaryUserUseCase = mockk(relaxed = true)
    private val chatSimulatorMock: ChatSimulator = mockk(relaxed = true)

    private lateinit var testedClass: MessagingViewModel

    @Test
    fun `should return messages and logged in user when vm is initialised`() = runTest {
        val user = getUser()
        val messageItems = getMessageItems()

        coEvery { getPrimaryUserUseCaseMock.execute() } returns Result.success(user)
        coEvery { chatSimulatorMock.messageFlow } returns
                MutableStateFlow(messageItems).asStateFlow()

        buildViewModel()

        testedClass.state.test {
            with(awaitItem()) {
                loggedInUser shouldBe user
                messages shouldBe messageItems
            }
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `should set new typed message in state when typed message is changed`() = runTest {
        coEvery { getPrimaryUserUseCaseMock.execute() } returns Result.success(getUser())
        coEvery { chatSimulatorMock.messageFlow } returns
                MutableStateFlow(getMessageItems()).asStateFlow()

        buildViewModel()

        testedClass.onTextChanged("New Typed message")

        testedClass.state.test {
            with(awaitItem()) {
                text shouldBe "New Typed message"
            }
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `should reset message type box when user send the message`() = runTest {
        coEvery { getPrimaryUserUseCaseMock.execute() } returns Result.success(getUser())
        coEvery { chatSimulatorMock.messageFlow } returns
                MutableStateFlow(getMessageItems()).asStateFlow()

        buildViewModel()
        testedClass.onTextChanged("New Typed message")
        testedClass.onSendButtonClick()

        testedClass.state.test {
            with(awaitItem()) {
                text shouldBe ""
            }
            ensureAllEventsConsumed()
        }

        coVerify(exactly = 1) {
            chatSimulatorMock.send(
                chatSessionId = 100L,
                ownerUserId = 101L,
                text = "New Typed message",
            )
        }
    }

    @Test
    fun `should ignore send action when typed message is blank`() = runTest {
        coEvery { getPrimaryUserUseCaseMock.execute() } returns Result.success(getUser())
        coEvery { chatSimulatorMock.messageFlow } returns
                MutableStateFlow(getMessageItems()).asStateFlow()

        buildViewModel()
        testedClass.onTextChanged("")
        val result = testedClass.onSendButtonClick()

        result shouldBe Unit
    }

    @Test
    fun `should ignore send action when logged in user no found`() = runTest {
        coEvery { getPrimaryUserUseCaseMock.execute() } returns Result.failure(UserNotFound())
        coEvery { chatSimulatorMock.messageFlow } returns
                MutableStateFlow(getMessageItems()).asStateFlow()

        buildViewModel()
        testedClass.onTextChanged("Typed new message")
        val result = testedClass.onSendButtonClick()

        result shouldBe Unit
    }

    private fun buildViewModel() {
        testedClass = MessagingViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "session" to ChatSession(
                        sessionId = 100L,
                        chatMate = User(
                            id = 101L,
                            name = "Alessia Jackson",
                            userName = "EarlyBirdAlessia",
                            imageUrl = "http://www.youtube.com",
                            phone = "+356879432313",
                            nationality = "NL"
                        )
                    )
                )
            ),
            contextProvider = testContextProvider,
            getPrimaryUserUseCase = getPrimaryUserUseCaseMock,
            chatSimulator = chatSimulatorMock,
        )
    }

    private fun getUser() = User(
        id = 101L,
        name = "Jack Sparrow",
        userName = "LuckyJack",
        imageUrl = "http://www.google.com",
        phone = "+32768231283",
        nationality = "GER"
    )

    private fun getMessageItems() = listOf(
        MessageItem.OutboundMessageItem(
            text = "Message 1",
            time = "09:21",
        ),
        MessageItem.InboundMessageItem(
            text = "Response to message 1",
            time = "09:25",
        )
    )
}
