package com.akinci.chatter.data

import com.akinci.chatter.core.application.AppConfig
import com.akinci.chatter.core.network.HttpClientFactory
import com.akinci.chatter.core.network.HttpEngineFactoryMock
import com.akinci.chatter.data.exception.UserFetchError
import com.akinci.chatter.data.exception.UserNotFound
import com.akinci.chatter.data.mapper.toData
import com.akinci.chatter.data.repository.UserRepository
import com.akinci.chatter.data.room.user.UserDao
import com.akinci.chatter.domain.data.User
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.string.shouldContain
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import timber.log.Timber

class UserRepositoryTest {
    private val userDaoMock: UserDao = mockk(relaxed = true)
    private val appConfigMock: AppConfig = mockk(relaxed = true)

    private val httpEngineFactory = HttpEngineFactoryMock()
    private lateinit var httpClient: HttpClient

    private lateinit var testedClass: UserRepository

    @BeforeEach
    fun setup() {
        mockkObject(Timber)
        every { appConfigMock.getServiceEndpointBaseUrl() } returns "https://www.testBaseUrl.com/"

        httpClient = HttpClientFactory(httpEngineFactory, appConfigMock).create()
        testedClass = UserRepository(
            httpClient = httpClient,
            userDao = userDaoMock
        )
    }

    @AfterEach
    fun release() {
        unmockkObject(Timber)
    }

    @Test
    fun `should return random user when call is successful`() = runTest {
        httpEngineFactory.statusCode = HttpStatusCode.OK
        httpEngineFactory.simulateException = false

        val expectedUser = getExpectedUser()

        val result = testedClass.generateRandomUser()

        result shouldBeSuccess expectedUser
    }

    @Test
    fun `should return failure when call is failed with 400`() = runTest {
        httpEngineFactory.statusCode = HttpStatusCode.BadRequest
        httpEngineFactory.simulateException = false

        val result = testedClass.generateRandomUser()

        verify(exactly = 1) { Timber.e(any() as Throwable) }
        result shouldBeFailure {
            it.message shouldContain "Bad Request"
        }
    }

    @Test
    fun `should return UserFetchError when call is failed with simulated exception`() = runTest {
        httpEngineFactory.statusCode = HttpStatusCode.OK
        httpEngineFactory.simulateException = true

        val result = testedClass.generateRandomUser()

        verify(exactly = 1) { Timber.e(any() as Throwable) }
        result shouldBeFailure {
            assert(it is UserFetchError)
        }
    }

    @Test
    fun `should create given user when call is successful`() = runTest {
        val user = getExpectedUser()
        val userEntity = user.toData()
        coEvery { userDaoMock.create(userEntity) } returns 0L

        val result = testedClass.create(user)

        result shouldBeSuccess 0L
    }

    @Test
    fun `should return throwable when create is failed`() = runTest {
        val user = getExpectedUser()
        val userEntity = user.toData()
        val exception = Throwable()
        coEvery { userDaoMock.create(userEntity) } throws exception

        val result = testedClass.create(user)

        result shouldBeFailure exception
    }

    @Test
    fun `should return UserNotFound and log exception when user's name is not in DB`() = runTest {
        val user = getExpectedUser()
        coEvery { userDaoMock.get(user.name) } returns null

        val result = testedClass.get(user.name)

        verify(exactly = 1) { Timber.e(any() as Throwable, any() as String) }
        result shouldBeFailure {
            assert(it is UserNotFound)
        }
    }

    @Test
    fun `should return Throwable and log exception when exception occurred on db`() = runTest {
        val user = getExpectedUser()
        val exception = Throwable()
        coEvery { userDaoMock.get(user.name) } throws exception

        val result = testedClass.get(user.name)

        verify(exactly = 1) { Timber.e(any() as Throwable, any() as String) }
        result shouldBeFailure exception
    }

    @Test
    fun `should return domain model when call is successful`() = runTest {
        val user = getExpectedUser()
        val userEntity = getExpectedUser().toData()
        coEvery { userDaoMock.get(user.name) } returns userEntity

        val result = testedClass.get(user.name)

        result shouldBeSuccess user
    }

    private fun getExpectedUser() = User(
        id = 0L,
        name = "David Mota",
        userName = "greenkoala365",
        imageUrl = "https://randomuser.me/api/portraits/men/29.jpg",
        phone = "(694) 591 9018",
        nationality = "MX",
    )
}
