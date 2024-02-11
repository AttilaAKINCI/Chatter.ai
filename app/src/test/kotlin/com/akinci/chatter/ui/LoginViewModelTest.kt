package com.akinci.chatter.ui

import app.cash.turbine.test
import com.akinci.chatter.core.coroutine.MainDispatcherRule
import com.akinci.chatter.core.coroutine.TestContextProvider
import com.akinci.chatter.data.datastore.DataStorage
import com.akinci.chatter.data.exception.UserNotFound
import com.akinci.chatter.data.repository.UserRepository
import com.akinci.chatter.domain.data.User
import com.akinci.chatter.ui.features.login.LoginViewContract
import com.akinci.chatter.ui.features.login.LoginViewModel
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MainDispatcherRule::class)
class LoginViewModelTest {

    private val testContextProvider = TestContextProvider()
    private val dataStorageMock: DataStorage = mockk(relaxed = true)
    private val userRepositoryMock: UserRepository = mockk(relaxed = true)

    private lateinit var testedClass: LoginViewModel

    @BeforeEach
    fun setup() {
        testedClass = LoginViewModel(
            contextProvider = testContextProvider,
            dataStorage = dataStorageMock,
            userRepository = userRepositoryMock,
        )
    }

    @Test
    fun `should update name and state when user input changed`() = runTest {
        val newName = "Attila Akinci"

        testedClass.updateName(newName)

        testedClass.state.test {
            with(awaitItem()) {
                name shouldBe newName
                validationError shouldBe false
            }
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `should return Unit when inputs are not valid for login`() = runTest {
        testedClass.updateName("")

        val result = testedClass.tryToLogin()
        result shouldBe Unit

        testedClass.state.test {
            with(awaitItem()) {
                validationError shouldBe true
                isLoginButtonLoading shouldBe false
                isRegisterButtonLoading shouldBe false
            }
            ensureAllEventsConsumed()
        }

        coVerify(exactly = 0) { dataStorageMock.setLoggedInUsersName(any()) }
        coVerify(exactly = 0) { userRepositoryMock.get(any()) }
    }

    @Test
    fun `should log in when inputs are valid and user verified for login`() = runTest {
        val user = getUser()
        testedClass.updateName(user.name)
        coEvery { userRepositoryMock.get(user.name) } returns Result.success(user)

        testedClass.tryToLogin()

        testedClass.state.test {
            // switch to loading mode first
            with(awaitItem()) {
                validationError shouldBe false
                isLoginButtonLoading shouldBe true
            }
            ensureAllEventsConsumed()
        }

        testedClass.effect.test {
            assert(awaitItem() is LoginViewContract.Effect.NavigateToDashboard)
            ensureAllEventsConsumed()
        }

        coVerify(exactly = 1) { dataStorageMock.setLoggedInUsersName(user.name) }
        coVerify(exactly = 1) { userRepositoryMock.get(user.name) }
    }

    @Test
    fun `should open login when inputs are valid but user not verified for login`() = runTest {
        val userName = "Attila Akinci"
        testedClass.updateName(userName)
        coEvery { userRepositoryMock.get(userName) } returns Result.failure(UserNotFound())

        testedClass.tryToLogin()

        testedClass.state.test {
            // switch to loading mode first
            with(awaitItem()) {
                validationError shouldBe false
                isLoginButtonLoading shouldBe true
            }

            // after log in status
            with(awaitItem()) {
                isLoginButtonLoading shouldBe false
                isRegisterButtonLoading shouldBe false
            }

            ensureAllEventsConsumed()
        }

        testedClass.effect.test {
            assert(awaitItem() is LoginViewContract.Effect.ShowToastMessage)
            ensureAllEventsConsumed()
        }

        coVerify(exactly = 1) { userRepositoryMock.get(userName) }
        coVerify(exactly = 0) { dataStorageMock.setLoggedInUsersName(userName) }
    }

    @Test
    fun `should return Unit when inputs are not valid for register`() = runTest {
        testedClass.updateName("")

        val result = testedClass.tryToRegister()
        result shouldBe Unit

        testedClass.state.test {
            with(awaitItem()) {
                validationError shouldBe true
                isLoginButtonLoading shouldBe false
                isRegisterButtonLoading shouldBe false
            }

            ensureAllEventsConsumed()
        }

        coVerify(exactly = 0) { dataStorageMock.setLoggedInUsersName(any()) }
        coVerify(exactly = 0) { userRepositoryMock.get(any()) }
    }

    @Test
    fun `should show failure snack bar when user already registered before`() = runTest {
        val user = getUser()
        testedClass.updateName(user.name)
        coEvery { userRepositoryMock.get(user.name) } returns Result.success(user)

        testedClass.tryToRegister()

        testedClass.state.test {
            // switch to loading mode first
            with(awaitItem()) {
                validationError shouldBe false
                isRegisterButtonLoading shouldBe true
            }

            with(awaitItem()) {
                isLoginButtonLoading shouldBe false
                isRegisterButtonLoading shouldBe false
            }

            ensureAllEventsConsumed()
        }

        testedClass.effect.test {
            assert(awaitItem() is LoginViewContract.Effect.ShowToastMessage)
            ensureAllEventsConsumed()
        }

        coVerify(exactly = 0) { userRepositoryMock.generateRandomUser() }
        coVerify(exactly = 0) { userRepositoryMock.create(any()) }
        coVerify(exactly = 0) { dataStorageMock.setLoggedInUsersName(any()) }
        coVerify(exactly = 1) { userRepositoryMock.get(user.name) }
    }

    @Test
    fun `should return failure snack bar when new user could not be generated`() = runTest {
        val userName = "Attila Akinci"
        testedClass.updateName(userName)
        coEvery { userRepositoryMock.get(userName) } returns Result.failure(UserNotFound())
        coEvery { userRepositoryMock.generateRandomUser() } returns Result.failure(Throwable())

        testedClass.tryToRegister()

        testedClass.state.test {
            // switch to loading mode first
            with(awaitItem()) {
                validationError shouldBe false
                isRegisterButtonLoading shouldBe true
            }

            with(awaitItem()) {
                isLoginButtonLoading shouldBe false
                isRegisterButtonLoading shouldBe false
            }

            ensureAllEventsConsumed()
        }

        testedClass.effect.test {
            assert(awaitItem() is LoginViewContract.Effect.ShowToastMessage)
            ensureAllEventsConsumed()
        }

        coVerify(exactly = 0) { userRepositoryMock.create(any()) }
        coVerify(exactly = 0) { dataStorageMock.setLoggedInUsersName(any()) }
        coVerify(exactly = 1) { userRepositoryMock.get(userName) }
        coVerify(exactly = 1) { userRepositoryMock.generateRandomUser() }
    }

    @Test
    fun `should return failure snack bar when new user could not be created on DB`() = runTest {
        val userName = "Attila Akinci"
        val user = getUser()
        testedClass.updateName(userName)
        coEvery { userRepositoryMock.get(userName) } returns Result.failure(UserNotFound())
        coEvery { userRepositoryMock.generateRandomUser() } returns Result.success(user)
        val expectedUser = user.copy(name = userName)
        coEvery { userRepositoryMock.create(expectedUser) } returns Result.failure(Throwable())

        testedClass.tryToRegister()

        testedClass.state.test {
            // switch to loading mode first
            with(awaitItem()) {
                validationError shouldBe false
                isRegisterButtonLoading shouldBe true
            }

            with(awaitItem()) {
                isLoginButtonLoading shouldBe false
                isRegisterButtonLoading shouldBe false
            }

            ensureAllEventsConsumed()
        }

        testedClass.effect.test {
            assert(awaitItem() is LoginViewContract.Effect.ShowToastMessage)
            ensureAllEventsConsumed()
        }

        coVerify(exactly = 0) { dataStorageMock.setLoggedInUsersName(any()) }
        coVerify(exactly = 1) { userRepositoryMock.get(userName) }
        coVerify(exactly = 1) { userRepositoryMock.generateRandomUser() }
        coVerify(exactly = 1) { userRepositoryMock.create(expectedUser) }
    }

    @Test
    fun `should login when register is successful`() = runTest {
        val userName = "Attila Akinci"
        val user = getUser()
        testedClass.updateName(userName)
        coEvery { userRepositoryMock.get(userName) } returns Result.failure(UserNotFound())
        coEvery { userRepositoryMock.generateRandomUser() } returns Result.success(user)
        val expectedUser = user.copy(name = userName)
        coEvery { userRepositoryMock.create(expectedUser) } returns Result.success(100L)

        testedClass.tryToRegister()

        testedClass.state.test {
            // switch to loading mode first
            with(awaitItem()) {
                validationError shouldBe false
                isRegisterButtonLoading shouldBe true
            }

            ensureAllEventsConsumed()
        }

        testedClass.effect.test {
            assert(awaitItem() is LoginViewContract.Effect.NavigateToDashboard)
            ensureAllEventsConsumed()
        }

        coVerify(exactly = 1) { userRepositoryMock.get(userName) }
        coVerify(exactly = 1) { userRepositoryMock.generateRandomUser() }
        coVerify(exactly = 1) { userRepositoryMock.create(expectedUser) }
        coVerify(exactly = 1) { dataStorageMock.setLoggedInUsersName(any()) }
    }

    private fun getUser() = User(
        id = 100L,
        name = "Jack Sparrow",
        userName = "LuckyJack",
        imageUrl = "http://www.google.com",
        phone = "+32768231283",
        nationality = "GER"
    )
}
