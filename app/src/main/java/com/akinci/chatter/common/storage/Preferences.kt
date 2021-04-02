package com.akinci.chatter.common.storage

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class Preferences @Inject constructor(
        @ApplicationContext val context : Context
) {
    private val prefTag  = context.packageName + "_preferences"
    private val prefs = context.getSharedPreferences(prefTag, Context.MODE_PRIVATE)

    fun getStoredTag(key: String): String {
        return prefs.getString(key, "") ?: ""
    }
    fun setStoredTag(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }
    fun clear() {
        prefs.edit().clear().apply()
    }
}