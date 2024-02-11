package com.akinci.chatter.data

import app.cash.turbine.test
import com.akinci.chatter.data.repository.MessageRepository
import com.akinci.chatter.data.room.message.MessageDao
import com.akinci.chatter.data.room.message.MessageEntity
import com.akinci.chatter.data.room.message.MessageWithUser
import com.akinci.chatter.data.room.user.UserEntity
import com.akinci.chatter.domain.data.Message
import com.akinci.chatter.domain.data.User
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import timber.log.Timber

class MessageRepositoryTest {

    companion object {
        const val CHAT_SESSION_ID = 1000L
    }

    private val messageDaoMock: MessageDao = mockk(relaxed = true)

    private lateinit var testedClass: MessageRepository

    @BeforeEach
    fun setup() {
        mockkObject(Timber)
        every { messageDaoMock.get(CHAT_SESSION_ID) } returns flowOf(getMessageEntities())

        testedClass = MessageRepository(
            messageDao = messageDaoMock
        )
    }

    @AfterEach
    fun release() {
        unmockkObject(Timber)
    }

    @Test
    fun `should return domain model message stream`() = runTest {
        val stream = testedClass.get(CHAT_SESSION_ID)
        val expectedMessages = getExpectedMessages()

        stream.test {
            val messages = awaitItem()
            messages shouldBe expectedMessages
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should return failure and log exception when exception occurred during message save`() =
        runTest {
            val exception = Throwable()
            coEvery { messageDaoMock.save(any()) } throws exception

            val result = testedClass.save(
                chatSessionId = CHAT_SESSION_ID,
                primaryUserId = 100L,
                text = "Sample text to save as message"
            )

            verify(exactly = 1) { Timber.e(any() as Throwable, any() as String) }
            result shouldBeFailure exception
        }

    @Test
    fun `should return success when call is successful`() = runTest {
        coEvery { messageDaoMock.save(any()) } returns Unit

        val result = testedClass.save(
            chatSessionId = CHAT_SESSION_ID,
            primaryUserId = 100L,
            text = "Sample text to save as message"
        )

        verify(exactly = 0) { Timber.e(any() as Throwable, any() as String) }
        result shouldBeSuccess Unit
    }

    private fun getExpectedMessages() = listOf(
        Message(
            id = 1L,
            date = "1961-11-29T09:21:48.552Z",
            sender = User(
                id = 100L,
                name = "Jack Sparrow",
                userName = "LuckyJack",
                imageUrl = "http://www.google.com",
                phone = "+32768231283",
                nationality = "GER"
            ),
            text = "Message 1",
        ),
        Message(
            id = 2L,
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

    private fun getMessageEntities() = listOf(
        MessageWithUser(
            messageEntity = MessageEntity(
                id = 1L,
                chatSessionId = 1000L,
                senderUserId = 100L,
                date = "1961-11-29T09:21:48.552Z",
                text = "Message 1",
            ),
            userEntity = UserEntity(
                id = 100L,
                name = "Jack Sparrow",
                userName = "LuckyJack",
                imageUrl = "http://www.google.com",
                phone = "+32768231283",
                nationality = "GER"
            )
        ),
        MessageWithUser(
            messageEntity = MessageEntity(
                id = 2L,
                chatSessionId = 1000L,
                senderUserId = 101L,
                date = "1961-11-29T09:25:50.552Z",
                text = "Response to message 1",
            ),
            userEntity = UserEntity(
                id = 101L,
                name = "Alessia Jackson",
                userName = "EarlyBirdAlessia",
                imageUrl = "http://www.youtube.com",
                phone = "+356879432313",
                nationality = "NL"
            )
        ),
    )
}
