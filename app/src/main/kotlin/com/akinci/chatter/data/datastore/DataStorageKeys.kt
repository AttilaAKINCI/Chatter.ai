package com.akinci.chatter.data.datastore

import androidx.datastore.preferences.core.stringPreferencesKey

object DataStorageKeys {
    val LOGGED_IN_USER = stringPreferencesKey("logged_in_user")
}