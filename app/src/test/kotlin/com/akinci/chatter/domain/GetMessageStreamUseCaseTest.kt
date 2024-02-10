package com.akinci.chatter.domain

import app.cash.turbine.test
import com.akinci.chatter.data.exception.UserNotFound
import com.akinci.chatter.data.repository.MessageRepository
import com.akinci.chatter.domain.data.Message
import com.akinci.chatter.domain.data.MessageItem
import com.akinci.chatter.domain.data.User
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import timber.log.Timber

class GetMessageStreamUseCaseTest {

    private val getPrimaryUserUseCaseMock: GetPrimaryUserUseCase = mockk(relaxed = true)
    private val messageRepositoryMock: MessageRepository = mockk(relaxed = true)

    private lateinit var testedClass: GetMessageStreamUseCase

    @BeforeEach
    fun setup() {
        mockkObject(Timber)
        testedClass = GetMessageStreamUseCase(
            getPrimaryUserUseCase = getPrimaryUserUseCaseMock,
            messageRepository = messageRepositoryMock,
        )
    }

    @AfterEach
    fun release() {
        unmockkObject(Timber)
    }

    @Test
    fun `should log failure of getPrimaryUserUseCase when an exception occurred`() = runTest {
        val chatSessionId = 100L
        coEvery { getPrimaryUserUseCaseMock.execute() } returns Result.failure(UserNotFound())

        val result = testedClass.execute(chatSessionId = chatSessionId)

        verify(exactly = 1) { Timber.e(any() as Throwable, any() as String) }
        verify(exactly = 0) { messageRepositoryMock.get(chatSessionId) }
        result shouldBe emptyFlow()
    }

    @Test
    fun `should get domain massage model when call is successful`() = runTest {
        val chatSessionId = 100L
        val user = getUser()
        coEvery { getPrimaryUserUseCaseMock.execute() } returns Result.success(user)
        coEvery { messageRepositoryMock.get(chatSessionId) } returns flowOf(getMessages())
        val expectedMessages = getExpectedMessages()

        val result = testedClass.execute(chatSessionId = chatSessionId)

        verify(exactly = 0) { Timber.e(any() as Throwable, any() as String) }
        verify(exactly = 1) { messageRepositoryMock.get(chatSessionId) }

        result.test {
            val messages = awaitItem()
            messages.size shouldBe 2
            messages[0] shouldBe expectedMessages[0]
            messages[1] shouldBe expectedMessages[1]

            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun getUser() = User(
        id = 100L,
        name = "Jack Sparrow",
        userName = "LuckyJack",
        imageUrl = "http://www.google.com",
        phone = "+32768231283",
        nationality = "GER"
    )

    private fun getExpectedMessages() = listOf(
        MessageItem.OutboundMessageItem(
            text = "Message 1",
            time = "09:21",
        ),
        MessageItem.InboundMessageItem(
            text = "Response to message 1",
            time = "09:25",
        )
    )

    private fun getMessages() = listOf(
        Message(
            id = 1000L,
            sender = User(
                id = 100L,
                name = "Jack Sparrow",
                userName = "LuckyJack",
                imageUrl = "http://www.google.com",
                phone = "+32768231283",
                nationality = "GER"
            ),
            date = "1961-11-29T09:21:48.552Z",
            text = "Message 1",
        ),
        Message(
            id = 1001L,
            sender = User(
                id = 101L,
                name = "Alessia Jackson",
                userName = "EarlyBirdAlessia",
                imageUrl = "http://www.youtube.com",
                phone = "+356879432313",
                nationality = "NL"
            ),
            date = "1961-11-29T09:25:50.552Z",
            text = "Response to message 1",
        ),
    )
}
