package com.akinci.chatter.data

import android.database.sqlite.SQLiteException
import app.cash.turbine.test
import com.akinci.chatter.data.repository.ChatSessionRepository
import com.akinci.chatter.data.room.chatsession.ChatSessionDao
import com.akinci.chatter.data.room.chatsession.ChatSessionEntity
import com.akinci.chatter.data.room.chatsession.ChatSessionWithUser
import com.akinci.chatter.data.room.user.UserEntity
import com.akinci.chatter.domain.data.ChatSession
import com.akinci.chatter.domain.data.User
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ChatSessionRepositoryTest {

    private val chatSessionDaoMock: ChatSessionDao = mockk(relaxed = true)

    private lateinit var testedClass: ChatSessionRepository

    @BeforeEach
    fun setup() {
        testedClass = ChatSessionRepository(
            chatSessionDao = chatSessionDaoMock
        )
    }

    @Test
    fun `should return chat session stream`() = runTest {
        coEvery { chatSessionDaoMock.getStream(sessionMemberId = 100L) } returns getChatSessionStream()

        val stream = testedClass.getChatSessionStream(memberId = 100L)

        stream.test {
            awaitItem() shouldBe listOf(
                ChatSession(
                    sessionId = 1L,
                    chatMate = User(
                        id = 101L,
                        name = "Jack",
                        userName = "LuckyJack",
                        imageUrl = "http://www.google.com",
                        phone = "+32768231283",
                        nationality = "GER"
                    )
                )
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should return Result success when successful chat session creation`() = runTest {
        coEvery {
            chatSessionDaoMock.create(
                ChatSessionEntity(primaryUserId = 100L, secondaryUserId = 101L)
            )
        } returns Unit

        val result = testedClass.create(primaryUserId = 100L, secondaryUserId = 101L)

        result shouldBeSuccess Unit
    }

    @Test
    fun `should return Result failure when exception is occurred during chat session creation`() =
        runTest {
            coEvery {
                chatSessionDaoMock.create(
                    ChatSessionEntity(primaryUserId = 100L, secondaryUserId = 101L)
                )
            } throws SQLiteException()

            val result = testedClass.create(primaryUserId = 100L, secondaryUserId = 101L)

            result shouldBeFailure {
                it shouldBe SQLiteException()
            }
        }

    private fun getChatSessionStream() = flowOf(
        listOf(
            ChatSessionWithUser(
                chatSessionEntity = ChatSessionEntity(
                    id = 1L,
                    primaryUserId = 100L,
                    secondaryUserId = 101L
                ),
                primaryUserEntity = UserEntity(
                    id = 101L,
                    name = "Jack",
                    userName = "LuckyJack",
                    imageUrl = "http://www.google.com",
                    phone = "+32768231283",
                    nationality = "GER"
                ),
                secondaryUserEntity = UserEntity(
                    id = 101L,
                    name = "Jessica Alba",
                    userName = "Alora",
                    imageUrl = "http://www.youtube.com",
                    phone = "+4453229123",
                    nationality = "NL"
                )
            )
        )
    )
}
