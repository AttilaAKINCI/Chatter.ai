package com.akinci.chatter.ui

import app.cash.turbine.test
import com.akinci.chatter.core.coroutine.MainDispatcherRule
import com.akinci.chatter.core.coroutine.TestContextProvider
import com.akinci.chatter.data.datastore.DataStorage
import com.akinci.chatter.data.exception.UserFetchError
import com.akinci.chatter.data.exception.UserNotFound
import com.akinci.chatter.data.repository.ChatSessionRepository
import com.akinci.chatter.data.repository.UserRepository
import com.akinci.chatter.domain.GetPrimaryUserUseCase
import com.akinci.chatter.domain.data.ChatSession
import com.akinci.chatter.domain.data.User
import com.akinci.chatter.ui.features.dashboard.DashboardViewContract
import com.akinci.chatter.ui.features.dashboard.DashboardViewModel
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MainDispatcherRule::class)
class DashboardViewModelTest {

    private val testContextProvider = TestContextProvider()
    private val dataStorageMock: DataStorage = mockk(relaxed = true)
    private val chatSessionRepositoryMock: ChatSessionRepository = mockk(relaxed = true)
    private val userRepositoryMock: UserRepository = mockk(relaxed = true)
    private val getPrimaryUserUseCaseMock: GetPrimaryUserUseCase = mockk(relaxed = true)

    private lateinit var testedClass: DashboardViewModel

    @Test
    fun `should show error ui state when logged in user not found`() = runTest {
        coEvery { getPrimaryUserUseCaseMock.execute() } returns Result.failure(UserNotFound())

        buildViewModel()

        testedClass.state.test {
            with(awaitItem()) {
                chatSessions shouldBe persistentListOf()
                stateType shouldBe DashboardViewContract.StateType.ERROR
            }
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `should return logged in user and chat sessions when call is successful`() = runTest {
        val user = getUser()
        val sessions = getChatSessions()
        coEvery { getPrimaryUserUseCaseMock.execute() } returns Result.success(user)
        every { chatSessionRepositoryMock.getChatSessionStream(user.id) } returns flowOf(sessions)

        buildViewModel()

        testedClass.state.test {

            // check logged in user save
            with(awaitItem()) {
                loggedInUser shouldBe user
            }

            // check messages
            with(awaitItem()) {
                chatSessions shouldBe sessions.toPersistentList()
                stateType shouldBe DashboardViewContract.StateType.CONTENT
            }

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `should return logged in user and no data for chat sessions when session is empty`() =
        runTest {
            val user = getUser()
            coEvery { getPrimaryUserUseCaseMock.execute() } returns Result.success(user)
            every { chatSessionRepositoryMock.getChatSessionStream(user.id) } returns
                    flowOf(listOf())

            buildViewModel()

            testedClass.state.test {

                // check logged in user save
                with(awaitItem()) {
                    loggedInUser shouldBe user
                }

                // check messages
                with(awaitItem()) {
                    chatSessions shouldBe persistentListOf()
                    stateType shouldBe DashboardViewContract.StateType.NO_DATA
                }

                ensureAllEventsConsumed()
            }
        }

    @Test
    fun `should logout when logout button clicked`() = runTest {
        val user = getUser()
        coEvery { getPrimaryUserUseCaseMock.execute() } returns Result.success(user)
        every { chatSessionRepositoryMock.getChatSessionStream(user.id) } returns
                flowOf(getChatSessions())
        coEvery { dataStorageMock.setLoggedInUsersName(any()) } returns Unit

        buildViewModel()

        testedClass.logout()

        testedClass.state.test {
            // hide dialog first
            with(awaitItem()) {
                isLogoutDialogVisible shouldBe false
            }

            ensureAllEventsConsumed()
        }

        testedClass.effect.test {
            assert(awaitItem() is DashboardViewContract.Effect.LogoutUser)
            ensureAllEventsConsumed()
        }

        coVerify(exactly = 1) { dataStorageMock.setLoggedInUsersName("") }
    }

    @Test
    fun `should show logout dialog when logout button clicked`() = runTest {
        val user = getUser()
        coEvery { getPrimaryUserUseCaseMock.execute() } returns Result.success(user)
        every { chatSessionRepositoryMock.getChatSessionStream(user.id) } returns
                flowOf(getChatSessions())

        buildViewModel()

        testedClass.showLogoutDialog()

        testedClass.state.test {
            with(awaitItem()) {
                isLogoutDialogVisible shouldBe true
            }

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `should return unit when logged in user is not found`() = runTest {
        coEvery { getPrimaryUserUseCaseMock.execute() } returns Result.failure(UserNotFound())

        buildViewModel()

        testedClass.findNewChatMate()

        coVerify(exactly = 0) { userRepositoryMock.generateRandomUser() }
        coVerify(exactly = 0) { userRepositoryMock.create(any()) }
        coVerify(exactly = 0) { chatSessionRepositoryMock.create(any(), any()) }
    }

    @Test
    fun `should return snack bar error when generateRandomUser got error`() = runTest {
        val user = getUser()
        coEvery { getPrimaryUserUseCaseMock.execute() } returns Result.success(user)
        every { chatSessionRepositoryMock.getChatSessionStream(user.id) } returns
                flowOf(getChatSessions())
        coEvery { userRepositoryMock.generateRandomUser() } returns Result.failure(UserFetchError())

        buildViewModel()

        testedClass.findNewChatMate()

        testedClass.effect.test {
            assert(awaitItem() is DashboardViewContract.Effect.ShowToastMessage)
            ensureAllEventsConsumed()
        }

        coVerify(exactly = 1) { userRepositoryMock.generateRandomUser() }
        coVerify(exactly = 0) { chatSessionRepositoryMock.create(any(), any()) }
        coVerify(exactly = 0) { userRepositoryMock.create(any()) }
    }

    @Test
    fun `should return snack bar error when fetched user could not be saved`() = runTest {
        val user = getUser()
        val generatedUser = getGeneratedUser()
        coEvery { getPrimaryUserUseCaseMock.execute() } returns Result.success(user)
        every { chatSessionRepositoryMock.getChatSessionStream(user.id) } returns
                flowOf(getChatSessions())
        coEvery { userRepositoryMock.generateRandomUser() } returns Result.success(generatedUser)
        coEvery { userRepositoryMock.create(generatedUser) } returns Result.failure(Throwable())

        buildViewModel()

        testedClass.findNewChatMate()

        testedClass.effect.test {
            assert(awaitItem() is DashboardViewContract.Effect.ShowToastMessage)
            ensureAllEventsConsumed()
        }

        coVerify(exactly = 1) { userRepositoryMock.generateRandomUser() }
        coVerify(exactly = 1) { userRepositoryMock.create(generatedUser) }
        coVerify(exactly = 0) { chatSessionRepositoryMock.create(any(), any()) }
    }

    @Test
    fun `should return snack bar error when chat session could not be created`() = runTest {
        val user = getUser()
        val generatedUser = getGeneratedUser()
        coEvery { getPrimaryUserUseCaseMock.execute() } returns Result.success(user)
        every { chatSessionRepositoryMock.getChatSessionStream(user.id) } returns
                flowOf(getChatSessions())
        coEvery { userRepositoryMock.generateRandomUser() } returns Result.success(generatedUser)
        coEvery { userRepositoryMock.create(generatedUser) } returns Result.success(500L)
        val expectedGeneratedUser = generatedUser.copy(id = 500L)

        coEvery {
            chatSessionRepositoryMock.create(
                primaryUserId = user.id,
                secondaryUserId = expectedGeneratedUser.id
            )
        } returns Result.failure(Throwable())

        buildViewModel()

        testedClass.findNewChatMate()

        testedClass.effect.test {
            assert(awaitItem() is DashboardViewContract.Effect.ShowToastMessage)
            ensureAllEventsConsumed()
        }

        coVerify(exactly = 1) { userRepositoryMock.generateRandomUser() }
        coVerify(exactly = 1) { userRepositoryMock.create(generatedUser) }
        coVerify(exactly = 1) {
            chatSessionRepositoryMock.create(user.id, expectedGeneratedUser.id)
        }
    }

    @Test
    fun `should add new chat mate when generation is successful`() = runTest {
        val user = getUser()
        val generatedUser = getGeneratedUser()
        coEvery { getPrimaryUserUseCaseMock.execute() } returns Result.success(user)
        every { chatSessionRepositoryMock.getChatSessionStream(user.id) } returns
                flowOf(getChatSessions())
        coEvery { userRepositoryMock.generateRandomUser() } returns Result.success(generatedUser)
        coEvery { userRepositoryMock.create(generatedUser) } returns Result.success(500L)
        val expectedGeneratedUser = generatedUser.copy(id = 500L)

        coEvery {
            chatSessionRepositoryMock.create(
                primaryUserId = user.id,
                secondaryUserId = expectedGeneratedUser.id
            )
        } returns Result.success(Unit)

        buildViewModel()

        testedClass.findNewChatMate()

        testedClass.effect.test {
            assert(awaitItem() is DashboardViewContract.Effect.ShowToastMessage)
            ensureAllEventsConsumed()
        }

        coVerify(exactly = 1) { userRepositoryMock.generateRandomUser() }
        coVerify(exactly = 1) { userRepositoryMock.create(generatedUser) }
        coVerify(exactly = 1) {
            chatSessionRepositoryMock.create(user.id, expectedGeneratedUser.id)
        }
    }

    private fun buildViewModel() {
        testedClass = DashboardViewModel(
            contextProvider = testContextProvider,
            dataStorage = dataStorageMock,
            chatSessionRepository = chatSessionRepositoryMock,
            userRepository = userRepositoryMock,
            getPrimaryUserUseCase = getPrimaryUserUseCaseMock,
        )
    }

    private fun getChatSessions() = listOf(
        ChatSession(
            sessionId = 1000L,
            chatMate = User(
                id = 101L,
                name = "Alessia Jackson",
                userName = "EarlyBirdAlessia",
                imageUrl = "http://www.youtube.com",
                phone = "+356879432313",
                nationality = "NL"
            ),
        ),
        ChatSession(
            sessionId = 1000L,
            chatMate = User(
                id = 102L,
                name = "Jessica Peter",
                userName = "NightOwlJessica",
                imageUrl = "http://www.netflix.com",
                phone = "+887123525317",
                nationality = "UK"
            ),
        ),
    )

    private fun getUser() = User(
        id = 101L,
        name = "Jack Sparrow",
        userName = "LuckyJack",
        imageUrl = "http://www.google.com",
        phone = "+32768231283",
        nationality = "GER"
    )

    private fun getGeneratedUser() = User(
        id = 102L,
        name = "Jessica Peter",
        userName = "NightOwlJessica",
        imageUrl = "http://www.netflix.com",
        phone = "+887123525317",
        nationality = "UK"
    )
}
