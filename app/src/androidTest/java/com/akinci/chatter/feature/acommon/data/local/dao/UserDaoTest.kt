package com.akinci.chatter.feature.acommon.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.akinci.chatter.di.TestAppModule
import com.akinci.chatter.feature.acommon.data.local.ChatterDatabase
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
class UserDaoTest{

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @TestAppModule.TestDB
    lateinit var db: ChatterDatabase

    private lateinit var userDao: UserDao

    @Before
    fun setup() {
        hiltRule.inject()
        userDao = db.getUserDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertMultipleUserAndGetUser() = runBlockingTest {
        val users = listOf(
            UserEntity(
                id = 1L,
                loggedIdUser = false,
                avatarURL = "https://avatar1.png",
                nickname = "Nick 1"
            ),
            UserEntity(
                id = 2L,
                loggedIdUser = false,
                avatarURL = "https://avatar2.png",
                nickname = "Nick 2"
            )
        )

        userDao.insertAllUsers(users)

        val fetchedUser1 = userDao.getUser("Nick 1")
        assertThat(fetchedUser1?.id).isEqualTo(users[0].id)

        val fetchedUser2 = userDao.getUser("Nick 2")
        assertThat(fetchedUser2?.id).isEqualTo(users[1].id)
    }

    @Test
    fun insertSingleLoginUserAndGetLoginUser() = runBlockingTest {
        val user = UserEntity(
            id = 1L,
            loggedIdUser = true,
            avatarURL = "https://avatar.png",
            nickname = "Nick"
        )

        userDao.insertUser(user)

        val fetchedUser = userDao.getLoggedInUser("Nick")
        assertThat(fetchedUser?.id).isEqualTo(user.id)
    }

}