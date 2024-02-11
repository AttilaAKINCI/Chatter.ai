package com.akinci.chatter.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.akinci.chatter.data.room.AppDatabase
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
class UserDaoTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Inject
    @TestAppModule.TestDB
    lateinit var db: AppDatabase

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
    fun createUserAndGetById() = runTest {
        val user = getUserEntity()

        userDao.create(user)

        val fetchedUser = userDao.get(user.name)

        user shouldBe fetchedUser
    }

    @Test
    fun createUserAndGetByName() = runTest {
        val user = getUserEntity()

        userDao.create(user)

        val fetchedUser = userDao.get(user.id)

        user shouldBe fetchedUser
    }

    private fun getUserEntity() = UserEntity(
        id = 101L,
        name = "Jack",
        userName = "LuckyJack",
        imageUrl = "http://www.google.com",
        phone = "+32768231283",
        nationality = "GER"
    )
}
