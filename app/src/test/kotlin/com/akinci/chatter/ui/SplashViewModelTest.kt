package com.akinci.chatter.ui

import app.cash.turbine.test
import com.akinci.chatter.core.coroutine.MainDispatcherRule
import com.akinci.chatter.core.coroutine.TestContextProvider
import com.akinci.chatter.data.datastore.DataStorage
import com.akinci.chatter.ui.features.splash.SplashViewContract
import com.akinci.chatter.ui.features.splash.SplashViewModel
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MainDispatcherRule::class)
class SplashViewModelTest {

    private val testContextProvider = TestContextProvider()
    private val dataStorageMock: DataStorage = mockk(relaxed = true)

    private lateinit var testedClass: SplashViewModel

    @BeforeEach
    fun setup() {
        testedClass = SplashViewModel(
            contextProvider = testContextProvider,
            dataStorage = dataStorageMock,
        )
    }

    @Test
    fun `should return dashboard as initial screen when user logged in before`() = runTest {
        coEvery { dataStorageMock.getLoggedInUsersName() } returns "Attila Akinci"

        testedClass.effect.test {
            val effect = awaitItem()
            assert(effect is SplashViewContract.Effect.NavigateToRoute)
            (effect as SplashViewContract.Effect.NavigateToRoute).route shouldBe
                    SplashViewContract.Route.Dashboard

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `should return login as initial screen when user not logged in before`() = runTest {
        coEvery { dataStorageMock.getLoggedInUsersName() } returns null

        testedClass.effect.test {
            val effect = awaitItem()
            assert(effect is SplashViewContract.Effect.NavigateToRoute)
            (effect as SplashViewContract.Effect.NavigateToRoute).route shouldBe
                    SplashViewContract.Route.Login

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `should return login as initial screen when user name is blank`() = runTest {
        coEvery { dataStorageMock.getLoggedInUsersName() } returns ""

        testedClass.effect.test {
            val effect = awaitItem()
            assert(effect is SplashViewContract.Effect.NavigateToRoute)
            (effect as SplashViewContract.Effect.NavigateToRoute).route shouldBe
                    SplashViewContract.Route.Login

            ensureAllEventsConsumed()
        }
    }
}
