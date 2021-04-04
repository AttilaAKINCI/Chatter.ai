package com.akinci.chatter.feature.acommon.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.akinci.chatter.ahelpers.TestContextProvider
import com.akinci.chatter.di.TestAppModule
import com.akinci.chatter.feature.acommon.data.local.ChatterDatabase
import com.akinci.chatter.feature.acommon.data.local.entities.MessageEntity
import com.akinci.chatter.feature.acommon.data.local.entities.UserEntity
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@SmallTest
@ExperimentalCoroutinesApi
class MessageDaoTest{

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @TestAppModule.TestDB
    lateinit var db: ChatterDatabase

    private val coroutineContext = TestContextProvider()
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
    fun insertSingleMessageAndGetAllMessages() {
        val messageOwnerUser = UserEntity(
            id = 4L,
            loggedIdUser = true,
            avatarURL = "https://avatar.png",
            nickname = "Nick"
        )

        val message = MessageEntity(
            dataOwnerId = 1L,
            id = "Deneme",
            text = "Message COntent",
            timestamp = 10L,
            messageOwnerId = 4L
        )

        runBlockingTest {
            userDao.insertUser(messageOwnerUser)
            messageDao.insertMessage(message)
        }

        messageDao.getAllMessages(1L).observeForever{ allMessages ->
            assertThat(allMessages[0].messageEntity.id).isEqualTo(message.id)
        }

        coroutineContext.testCoroutineDispatcher.advanceUntilIdle()
    }

    @Test
    fun insertMultipleMessageAndGetAllMessages() {
        val messageOwnerUser = UserEntity(
            id = 4L,
            loggedIdUser = true,
            avatarURL = "https://avatar.png",
            nickname = "Nick"
        )

        val messages = listOf(
                MessageEntity(
                    dataOwnerId = 1L,
                    id = "Deneme",
                    text = "Message Content",
                    timestamp = 10L,
                    messageOwnerId = 4L
                ),
                MessageEntity(
                    dataOwnerId = 1L,
                    id = "Deneme 2",
                    text = "Message Content 2",
                    timestamp = 20L,
                    messageOwnerId = 4L
                ),
        )

        runBlockingTest {
            userDao.insertUser(messageOwnerUser)
            messageDao.insertAllMessages(messages)
        }

        messageDao.getAllMessages(1L).observeForever{ allMessages ->
            assertThat(allMessages.size).isEqualTo(2)
            assertThat(allMessages[0].messageEntity.id).isEqualTo(messages[0].id)
            assertThat(allMessages[1].messageEntity.id).isEqualTo(messages[1].id)
        }

        coroutineContext.testCoroutineDispatcher.advanceUntilIdle()

    }
}