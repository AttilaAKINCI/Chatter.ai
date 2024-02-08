package com.akinci.chatter.core.utils

import io.kotest.matchers.shouldBe
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import timber.log.Timber
import java.time.ZonedDateTime

class DateTimeFormatTest {

    @BeforeEach
    fun setup() {
        mockkObject(Timber)
    }

    @AfterEach
    fun release() {
        unmockkObject(Timber)
    }

    @Test
    fun `should format string date with cLock24 format`() = runTest {
        val dateTimeString = "1961-11-29T09:21:48.552Z"
        val expectedFormat = "09:21"
        val formattedString = DateTimeFormat.CLOCK_24.format(dateTimeString)

        verify(exactly = 0) { Timber.e(any() as Throwable, any() as String) }
        formattedString shouldBe expectedFormat
    }

    @Test
    fun `should return null when string format is broken for clock24`() = runTest {
        val dateTimeString = "1961-11-2909:21:48.552Z"
        val formattedString = DateTimeFormat.CLOCK_24.format(dateTimeString)

        verify(exactly = 1) { Timber.e(any() as Throwable, any() as String) }
        formattedString shouldBe null
    }

    @Test
    fun `should format ZonedDateTime date with cLock24 format`() = runTest {
        val dateTimeString = ZonedDateTime.parse("1961-11-29T09:21:48.552Z")
        val expectedFormat = "09:21"
        val formattedString = DateTimeFormat.CLOCK_24.format(dateTimeString)

        verify(exactly = 0) { Timber.e(any() as Throwable, any() as String) }
        formattedString shouldBe expectedFormat
    }

    @Test
    fun `should return null when null received as a parameter for clock24`() = runTest {
        val dateTimeString = null
        val formattedString = DateTimeFormat.CLOCK_24.format(dateTimeString)

        verify(exactly = 1) { Timber.e(any() as Throwable, any() as String) }
        formattedString shouldBe null
    }
}
