package com.akinci.chatter.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.akinci.chatter.data.room.AppDatabase
import com.akinci.chatter.data.room.message.MessageDao
import com.akinci.chatter.data.room.message.MessageEntity
import com.akinci.chatter.data.room.message.MessageWithUser
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
class MessageDaoTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Inject
    @TestAppModule.TestDB
    lateinit var db: AppDatabase

    private lateinit var messageDao: MessageDao
    private lateinit var userDao: UserDao

    @Before
    fun setup() {
        hiltRule.inject()
        messageDao = db.getMessageDao()
        userDao = db.getUserDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun saveMessageAndGetBackAsStream() = runTest {
        val primaryUserEntity = getPrimaryUserEntity()
        val secondaryUserEntity = getSecondaryUserEntity()
        val sessionId = 1000L

        userDao.create(primaryUserEntity)
        userDao.create(secondaryUserEntity)
        messageDao.save(getFirstMessageEntity())
        messageDao.save(getSecondMessageEntity())

        val expectedMessageStreamContent = getExpectedMessageStreamContent().reversed()

        val stream = messageDao.get(sessionId)

        stream.test {
            awaitItem() shouldBe expectedMessageStreamContent
        }
    }

    private fun getExpectedMessageStreamContent() = listOf(
        MessageWithUser(
            messageEntity = getFirstMessageEntity(),
            userEntity = getPrimaryUserEntity(),
        ),
        MessageWithUser(
            messageEntity = getSecondMessageEntity(),
            userEntity = getSecondaryUserEntity(),
        )
    )

    private fun getFirstMessageEntity() = MessageEntity(
        id = 1L,
        chatSessionId = 1000L,
        senderUserId = 101L,
        date = "1961-11-29T09:25:50.552Z",
        text = "First Message"
    )

    private fun getSecondMessageEntity() = MessageEntity(
        id = 2L,
        chatSessionId = 1000L,
        senderUserId = 102L,
        date = "1961-11-29T09:30:50.552Z",
        text = "Second Message"
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
