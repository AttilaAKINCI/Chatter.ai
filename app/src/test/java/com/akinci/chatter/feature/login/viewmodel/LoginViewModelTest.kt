package com.akinci.chatter.feature.login.viewmodel

import com.akinci.chatter.ahelper.InstantExecutorExtension
import com.akinci.chatter.ahelper.TestContextProvider
import com.akinci.chatter.common.helper.Resource
import com.akinci.chatter.common.storage.Preferences
import com.akinci.chatter.feature.acommon.data.local.entities.UserEntity
import com.akinci.chatter.feature.dashboard.data.output.User
import com.akinci.chatter.feature.dashboard.data.output.convertUserToUserEntity
import com.akinci.chatter.feature.login.repository.LoginRepository
import com.akinci.chatter.jsonresponses.GetUserResponse
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import com.squareup.moshi.Moshi
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
class LoginViewModelTest {

    @MockK
    lateinit var loginRepository: LoginRepository

    @MockK
    lateinit var preferences: Preferences

    lateinit var loginViewModel: LoginViewModel

    private val coroutineContext = TestContextProvider()
    private val moshi = Moshi.Builder().build()

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        loginViewModel = LoginViewModel(coroutineContext, loginRepository, preferences)

        loginViewModel.userName.value = "Nick 1"
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `input user couldn't find already logged in users, insert this user, return successful signed up message, update preferences and Return Resource Success`() {
        coEvery { loginRepository.getLoggedInUser(any()) } returns null
        coJustRun { loginRepository.insertUser(any()) }

        loginViewModel.loginEventHandler.observeForever {
            when (it) {
                is Resource.Info -> {
                    assertThat(it.message).isEqualTo("Successfully Signed up. Navigating to Chat Dashboard")
                }
                is Resource.Success -> {
                    assertThat(it.data).isTrue()
                }
                is Resource.Error -> {
                    assertThat(it.message).isEqualTo("User couldn't be created. Please try again later")
                }
            }
        }

        loginViewModel.actionLogin()
        coroutineContext.testCoroutineDispatcher.advanceUntilIdle()

        coVerify { loginRepository.insertUser(any()) }
        verify(exactly = 2) { preferences.setStoredTag(any(), any()) }

    }

    @Test
    fun `input user founded in already logged in users, return successful login message, update preferences and Return Resource Success`() {
        coEvery { loginRepository.getLoggedInUser(any()) } returns
                moshi.adapter(User::class.java).fromJson(GetUserResponse.userResponse)?.convertUserToUserEntity()
        coJustRun { loginRepository.insertUser(any()) }

        loginViewModel.loginEventHandler.observeForever {
            when (it) {
                is Resource.Info -> {
                    assertThat(it.message).isEqualTo("User Previously Signed up. Logging in. Navigating to Chat Dashboard")
                }
                is Resource.Success -> {
                    assertThat(it.data).isTrue()
                }
                is Resource.Error -> {
                    assertThat(it.message).isEqualTo("User couldn't be created. Please try again later")
                }
            }
        }

        loginViewModel.actionLogin()
        coroutineContext.testCoroutineDispatcher.advanceUntilIdle()

        coVerify(exactly = 0) { loginRepository.insertUser(any()) }
        verify(exactly = 2) { preferences.setStoredTag(any(), any()) }
    }

}
