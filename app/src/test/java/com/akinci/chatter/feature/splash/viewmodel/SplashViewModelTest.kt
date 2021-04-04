package com.akinci.chatter.feature.splash.viewmodel

import com.akinci.chatter.ahelper.InstantExecutorExtension
import com.akinci.chatter.common.storage.Preferences
import com.google.common.truth.Truth.assertThat
import com.squareup.moshi.Moshi
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
class SplashViewModelTest {

    @MockK
    lateinit var preferences: Preferences

    lateinit var splashViewModel: SplashViewModel

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        every { preferences.getStoredTag(any()) } returns "Logged-In"

        splashViewModel = SplashViewModel(preferences)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `fetch all recipes called returns Resource Success`() {
        //no need to call something in VM's init block live data is set.
        splashViewModel.isLoggedIn.observeForever{
            assertThat(it).isEqualTo("Logged-In")
        }
    }
}