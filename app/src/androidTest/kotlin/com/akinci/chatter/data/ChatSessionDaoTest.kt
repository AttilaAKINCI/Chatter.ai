package com.akinci.chatter.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.akinci.chatter.data.room.AppDatabase
import com.akinci.chatter.data.room.chatsession.ChatSessionDao
import com.akinci.chatter.data.room.chatsession.ChatSessionEntity
import com.akinci.chatter.data.room.chatsession.ChatSessionWithUser
import com.akinci.chatter.data.room.user.UserDao
import com.akinci.chatter.data.room.user.UserEntity
import com.akinci.chatter.di.TestAppModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@SmallTest
class ChatSessionDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Inject
    @TestAppModule.TestDB
    lateinit var db: AppDatabase

    private lateinit var chatSessionDao: ChatSessionDao
    private lateinit var userDao: UserDao

    @Before
    fun setup() {
        hiltRule.inject()
        chatSessionDao = db.getChatSessionDao()
        userDao = db.getUserDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun createChatSessionAndFetchAsStream() = runTest {
        val session = getChatSessionEntity()
        val primaryUserEntity = getPrimaryUserEntity()
        val secondaryUserEntity = getSecondaryUserEntity()

        userDao.create(primaryUserEntity)
        userDao.create(secondaryUserEntity)
        chatSessionDao.create(session)

        val expectedChatSessionContent = getExpectedChatSessionContent()

        val stream = chatSessionDao.getStream(primaryUserEntity.id)

        stream.test {
            awaitItem() shouldBe expectedChatSessionContent
        }
    }

    private fun getExpectedChatSessionContent() = listOf(
        ChatSessionWithUser(
            chatSessionEntity = getChatSessionEntity(),
            userEntity = getSecondaryUserEntity(),
        )
    )

    private fun getChatSessionEntity() = ChatSessionEntity(
        id = 1000L,
        primaryUserId = 101L,
        secondaryUserId = 102L,
    )

    private fun getPrimaryUserEntity() = UserEntity(
        id = 101L,
        name = "Jack",
        userName = "LuckyJack",
        imageUrl = "http://www.google.com",
        phone = "+32768231283",
        nationality = "GER"
    )

    private fun getSecondaryUserEntity() = UserEntity(
        id = 102L,
        name = "Alessia Jackson",
        userName = "EarlyBirdAlessia",
        imageUrl = "http://www.youtube.com",
        phone = "+356879432313",
        nationality = "NL"
    )
}