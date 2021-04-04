package com.akinci.chatter.common.extension

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

/********************************* Keyboard management related  *********************************/
fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}
/*************************************************************************************************/



/*********************************** Randomized Char & Digits  ***********************************/
private val charPool : List<Char> = ('A'..'Z').toList()
fun getRandomString(charCount: Int)
    = (1..charCount).map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("");

fun getRandomThreeDigits() = (100 .. 999).random()
fun getRandomLong() = (0 until Long.MAX_VALUE).random()
/*************************************************************************************************/



/************************************** Date Time related  ***************************************/
fun getCurrentDateAsTimeStamp() = Date().time / 1000
fun getFormattedDateString(format: String, timeStamp: Long) : String {
    val sdf = SimpleDateFormat(format, Locale.ENGLISH)
    val netDate = Date(timeStamp * 1000)
    return sdf.format(netDate)
}
/*************************************************************************************************/