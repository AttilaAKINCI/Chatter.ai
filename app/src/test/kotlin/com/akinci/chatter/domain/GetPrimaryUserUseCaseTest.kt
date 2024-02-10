package com.akinci.chatter.domain

import com.akinci.chatter.data.datastore.DataStorage
import com.akinci.chatter.data.exception.UserNotFound
import com.akinci.chatter.data.repository.UserRepository
import com.akinci.chatter.data.room.user.UserEntity
import com.akinci.chatter.domain.data.User
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetPrimaryUserUseCaseTest {

    private val dataStorageMock: DataStorage = mockk(relaxed = true)
    private val userRepositoryMock: UserRepository = mockk(relaxed = true)

    private lateinit var testedClass: GetPrimaryUserUseCase

    @BeforeEach
    fun setup() {
        testedClass = GetPrimaryUserUseCase(
            dataStorage = dataStorageMock,
            userRepository = userRepositoryMock,
        )
    }

    @Test
    fun `should return failure without logged in user`() = runTest {
        coEvery { dataStorageMock.getLoggedInUsersName() } returns null
        coEvery { userRepositoryMock.get(any() as String) } returns Result.success(getUser())

        val result = testedClass.execute()

        coVerify(exactly = 1) { dataStorageMock.getLoggedInUsersName() }
        coVerify(exactly = 0) { userRepositoryMock.get(any() as String) }
        result shouldBeFailure {
            assert(it is UserNotFound)
        }
    }

    @Test
    fun `should return failure when user an error occurred on userRepository`() = runTest {
        val userName = "Jack Sparrow"
        coEvery { dataStorageMock.getLoggedInUsersName() } returns userName
        coEvery { userRepositoryMock.get(userName) } returns Result.failure(UserNotFound())

        val result = testedClass.execute()

        coVerify(exactly = 1) { dataStorageMock.getLoggedInUsersName() }
        coVerify(exactly = 1) { userRepositoryMock.get(userName) }
        result shouldBeFailure {
            assert(it is UserNotFound)
        }
    }

    @Test
    fun `should return user when call is successful`() = runTest {
        val user = getUser()
        coEvery { dataStorageMock.getLoggedInUsersName() } returns user.name
        coEvery { userRepositoryMock.get(any() as String) } returns Result.success(user)

        val result = testedClass.execute()

        coVerify(exactly = 1) { dataStorageMock.getLoggedInUsersName() }
        coVerify(exactly = 1) { userRepositoryMock.get(user.name) }
        result shouldBeSuccess user
    }

    private fun getUser() = User(
        id = 101L,
        name = "Jack Sparrow",
        userName = "LuckyJack",
        imageUrl = "http://www.google.com",
        phone = "+32768231283",
        nationality = "GER"
    )
}
